import akka.actor.ActorSystem
import akka.stream.scaladsl.Tcp.{IncomingConnection, ServerBinding}
import akka.stream.scaladsl.{Flow, Framing, Source, Tcp}
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.ByteString
import messages.{Encode, Encoded, Meh}
import pdi.jwt.{Jwt, JwtAlgorithm}
import upickle.default._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
  * @author kasonchan
  * @since 2018-02-15
  */
object StreamIODemo {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer: Materializer = ActorMaterializer()

    val connections: Source[IncomingConnection, Future[ServerBinding]] =
      Tcp().bind(interface = "127.0.0.1", port = 19999)

    val connectionFlow = Flow[ByteString]
      .via(
        Framing.delimiter(ByteString("\n"),
                          maximumFrameLength = 1024,
                          allowTruncation = true))
      .map { e =>
        system.log.info(s"Received ${e.utf8String}")
        Try {
          read[Encode](e.utf8String).msg
        } match {
          case Success(j) =>
            system.log.info(s"Encoding $j")
            val encoded = Jwt.encode(j, "secretKey", JwtAlgorithm.HS512)
            system.log.info(s"Encoded $encoded")
            write(Encoded(encoded))
          case Failure(_) =>
            system.log.error(
              s"Invalid json format, missing element? ${e.utf8String}")
            write(Meh("Invalid json format, missing element?"))
        }
      }
      .map(ByteString(_))

    connections.runForeach(connection => connection.handleWith(connectionFlow))
  }

}
