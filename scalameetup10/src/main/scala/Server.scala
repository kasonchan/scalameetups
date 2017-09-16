import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.{Success, Try}

/**
  * @author kasonchan
  * @since Sep-2017
  */
object Server {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

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
        .handleNotFound {
          complete((NotFound, "Not here!"))
        }
        .result()

    case class JsonCharacter(name: String, job: String)
    implicit val characterFormat: RootJsonFormat[JsonCharacter] = jsonFormat2(
      JsonCharacter)

    val routes =
      path("hello") {
        get {
          complete("Akka HTTP")
        }
      } ~ path("jsoncharacter") {
        post {
          entity(as[JsonCharacter]) { character =>
            complete(s"${character.name} is a ${character.job}.")
          }
        }
      } ~ path("xmlcharacter") {
        post {
          entity(as[String]) { xmlString =>
            Try {
              val xml = scala.xml.XML.loadString(xmlString)
              (xml \\ "name").text + " is a " + (xml \\ "job").text + "."
            } match {
              case Success(s) => complete(s)
              case _          => complete(BadRequest, "Invalid xml format!")
            }
          }
        }
      }

    val (host, port) = ("localhost", 9000)

    val bindingFuture =
      Http().bindAndHandle(routes, host, port)

    bindingFuture.failed.foreach { ex =>
      System.err.println(s"Failed to bind to $host:$port!")
    }

    println(s"Server online at http://$host:$port/")
    println("Press RETURN to stop...")

    StdIn.readLine() // let it run until user presses return

    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}
