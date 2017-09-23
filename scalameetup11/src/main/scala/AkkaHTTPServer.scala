import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

/**
  * @author kasonchan
  * @since Sep-2017
  */
case class Message(statusCode: Int, messages: Seq[String])

object AkkaHTTPServer {

  implicit val system: ActorSystem = ActorSystem("system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  def main(args: Array[String]): Unit = {
    val (host, port) = ("localhost", 9000)

    val bindingFuture =
      Http().bindAndHandle(routes, host, port)

    bindingFuture.failed.foreach { ex =>
      System.err.println(s"Failed to bind to $host:$port!")
    }

    println("Hello World!")
    println("This is the Akka HTTP server!")
    println(s"Server online at http://$host:$port/")

    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  implicit val messageFormat: RootJsonFormat[Message] = jsonFormat2(Message)

  final def routes: Route =
    pathPrefix("harrypotter") {
      pathEnd {
        get {
          println(Message(StatusCodes.OK.intValue, Seq("Harry Potter Series!")))
          complete(
            Message(StatusCodes.OK.intValue, Seq("Harry Potter Series!")))
        }
      } ~ path("helloworld") {
        get {
          println(
            Message(StatusCodes.OK.intValue,
                    Seq("Harry Potter Series!", "Hello World!")))
          complete(
            Message(StatusCodes.OK.intValue,
                    Seq("Harry Potter Series!", "Hello World!")))
        }
      }
    }

}
