import actors.Logger
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import messages.Message
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.concurrent.ExecutionContextExecutor

/**
  * @author kason.chan
  * @since Jan-2018
  */
object Server {
  implicit val system: ActorSystem = ActorSystem("system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val logger = system.actorOf(Props[Logger], "logger")

  def main(args: Array[String]): Unit = {
    val (host, port) = ("0.0.0.0", 9000)

    val bindingFuture =
      Http().bindAndHandle(routes, host, port)

    bindingFuture.failed.foreach { ex =>
      System.err.println(s"Failed to bind to $host:$port!")
    }

    println("Hello World!")
    println("This is my first rpm deployed to a docker image!")
    println(s"Server online at http://$host:$port/")
  }

  implicit val messageFormat: RootJsonFormat[Message] = jsonFormat2(Message)

  implicit def myRejectionHandler: RejectionHandler =
    RejectionHandler
      .newBuilder()
      .handleNotFound {
        complete((NotFound, Message(NotFound.intValue, "Not here!")))
      }
      .handle {
        case MissingCookieRejection(cookieName) =>
          logger ! Message(BadRequest.intValue, "No cookies, no service!")
          complete((BadRequest,
            Message(BadRequest.intValue, "No cookies, no service!")))
      }
      .handle {
        case AuthorizationFailedRejection =>
          logger ! Message(Forbidden.intValue, "You're out of your depth!")
          complete((Forbidden,
            Message(Forbidden.intValue, "You're out of your depth!")))
      }
      .handle {
        case ValidationRejection(msg, _) =>
          logger ! Message(InternalServerError.intValue,
            "That wasn't valid! " + msg)
          complete(
            (InternalServerError,
              Message(InternalServerError.intValue,
                "That wasn't valid! " + msg)))
      }
      .handleAll[MethodRejection] { methodRejections =>
      val names = methodRejections.map(_.supported.name)
      logger ! Message(MethodNotAllowed.intValue,
        s"Can't do that! Supported: ${names mkString " or "}!")
      complete(
        (MethodNotAllowed,
          Message(MethodNotAllowed.intValue,
            s"Can't do that! Supported: ${names mkString " or "}!")))
    }
      .result()

  final def routes: Route =
    path("") {
      get {
        logger ! Message(StatusCodes.OK.intValue, "Akka HTTP RPM Docker")
        complete(Message(StatusCodes.OK.intValue, "Akka HTTP RPM Docker"))
      }
    }

}
