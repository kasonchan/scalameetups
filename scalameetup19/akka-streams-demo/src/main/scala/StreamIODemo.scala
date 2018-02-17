import java.security.{KeyPair, KeyPairGenerator, PrivateKey, PublicKey}

import akka.actor.ActorSystem
import akka.stream.scaladsl.Tcp.{IncomingConnection, ServerBinding}
import akka.stream.scaladsl.{Flow, Framing, Source, Tcp}
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.ByteString
import messages._
import pdi.jwt.{Jwt, JwtAlgorithm}
import upickle.default._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
  * @author kasonchan
  * @since 2018-02
  */
object StreamIODemo {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer: Materializer = ActorMaterializer()

    val keyGenerator = KeyPairGenerator.getInstance("RSA")

    val keyPair: KeyPair = keyGenerator.generateKeyPair
    val privateKey: PrivateKey = keyPair.getPrivate
    val publicKey: PublicKey = keyPair.getPublic

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
          read[Packet](e.utf8String)
        } match {
          case Success(j) =>
            j.status match {
              case "encode" =>
                system.log.info(s"Encoding ${j.msg}")
                val encodedMsg =
                  Jwt.encode(j.msg, privateKey, JwtAlgorithm.RS512)
                system.log.info(s"Encoded $encodedMsg")
                system.log.info(
                  s"Sent ${write(Packet("encoded", encodedMsg)).toString}")
                write(Packet("encoded", encodedMsg))
              case "decode" =>
                system.log.info(s"Decoding ${j.msg}")
                val decodedPacket: Try[(String, String, String)] =
                  Jwt.decodeRawAll(j.msg, publicKey, Seq(JwtAlgorithm.RS512))
                system.log.info(s"Decoded $decodedPacket")
                decodedPacket match {
                  case Success(d) =>
                    system.log.info(
                      s"Sent ${write(Packet("decoded", d._2)).toString}")
                    write(Packet("decoded", d._2))
                  case Failure(_) =>
                    system.log.warning(
                      s"Invalid json format, missing element? ${e.utf8String}")
                    system.log.warning(
                      s"Sent ${write(Packet("meh", "Invalid status")).toString}")
                    write(
                      Packet("meh", "Invalid json format, missing element?"))
                }
              case _ =>
                system.log.warning(s"Meh $j")
                system.log.warning(
                  s"Sent ${write(Packet("meh", "Invalid status")).toString}")
                write(Packet("meh", "Invalid status"))
            }

          case Failure(_) =>
            system.log.warning(
              s"Invalid json format, missing element? ${e.utf8String}")
            system.log.warning(
              s"Sent ${write(Packet("meh", "Invalid status")).toString}")
            write(Packet("meh", "Invalid json format, missing element?"))
        }
      }
      .map(ByteString(_))

    connections.runForeach(connection => connection.handleWith(connectionFlow))
  }

}
