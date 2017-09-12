import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer

import scala.io.StdIn

/**
  * @author kasonchan
  * @since Sep-2017
  */
object Server {
  def main(args: Array[String]): Unit = {
    implicit def myRejectionHandler =
      RejectionHandler
        .newBuilder()
        .handle {
          case MissingCookieRejection(cookieName) =>
            complete(
              HttpResponse(BadRequest, entity = "No cookies, no service!!!"))
        }
        .handle {
          case AuthorizationFailedRejection =>
            complete((Forbidden, "You're out of your depth!"))
        }
        .handle {
          case ValidationRejection(msg, _) =>
            complete((InternalServerError, "That wasn't valid! " + msg))
        }
        .handleAll[MethodRejection] { methodRejections =>
          val names = methodRejections.map(_.supported.name)
          complete(
            (MethodNotAllowed,
             s"Can't do that! Supported: ${names mkString " or "}!"))
        }
        .handleNotFound { complete((NotFound, "Not here!")) }
        .result()

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route =
      path("hello") {
        get {
          complete("Akka HTTP")
        }
      }

    val (host, port) = ("localhost", 80)

    val bindingFuture = Http().bindAndHandle(route, host, port)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

    bindingFuture.failed.foreach { ex =>
      log.error(ex, "Failed to bind to {}:{}!", host, port)
    }

    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
