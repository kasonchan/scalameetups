import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.xml.Elem

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
        .handleNotFound {
          complete((NotFound, "Not here!"))
        }
        .result()

    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    case class Character(name: String)
    implicit val characterFormat: RootJsonFormat[Character] = jsonFormat1(
      Character)

    case class DO(data: String)
    implicit val doFormat: RootJsonFormat[DO] = jsonFormat1(DO)

    val unsecureRoutes =
      path("hello") {
        get {
          complete("Akka HTTP")
        }
      } ~ path("character") {
        post {
          entity(as[Character]) { character =>
            complete(character.name)
          }
        }
      } ~ path("xml") {
        post {
          entity(as[DO]) { xml =>
            complete(xml.data)
          }
        }
      }

    //    val secureRoutes =
    //      path("hello") {
    //        get {
    //          complete("Akka HTTPS")
    //        }
    //      }

    //    val password
    //      : Array[Char] = "change me".toCharArray // do not store passwords in code, read them from somewhere safe!
    //
    //    val ks: KeyStore = KeyStore.getInstance("PKCS12")
    //    val keystore: InputStream =
    //      getClass.getClassLoader.getResourceAsStream("server.p12")
    //
    //    require(keystore != null, "Keystore required!")
    //    ks.load(keystore, password)
    //
    //    val keyManagerFactory: KeyManagerFactory =
    //      KeyManagerFactory.getInstance("SunX509")
    //    keyManagerFactory.init(ks, password)
    //
    //    val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    //    tmf.init(ks)
    //
    //    val sslContext: SSLContext = SSLContext.getInstance("TLS")
    //    sslContext.init(keyManagerFactory.getKeyManagers,
    //                    tmf.getTrustManagers,
    //                    new SecureRandom)
    //    val https: HttpsConnectionContext = ConnectionContext.https(sslContext)

    val (host, securedPort, unsecuredPort) = ("localhost", 80, 9000)

    val unsecureBindingFuture =
      Http().bindAndHandle(unsecureRoutes, host, unsecuredPort)

    unsecureBindingFuture.failed.foreach { ex =>
      println(ex, "Failed to bind to {}:{}!", host, unsecuredPort)
    }

    println(
      s"Unsecured server online at https://$host:$unsecuredPort/\n" +
        s"Press RETURN to stop...")

    //    Http().setDefaultServerHttpContext(https)
    //    val secureBindingFuture = Http().bindAndHandle(secureRoutes,
    //                                                   "localhost",
    //                                                   securedPort,
    //                                                   connectionContext = https)

    //    println(
    //      s"Secured server online at https://localhost:9090/\nPress RETURN to stop...")

    //    secureBindingFuture.failed.foreach { ex =>
    //      println(ex, "Failed to bind to {}:{}!", host, securedPort)
    //    }

    StdIn.readLine() // let it run until user presses return

    unsecureBindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

    //    secureBindingFuture
    //      .flatMap(_.unbind()) // trigger unbinding from the port
    //      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}
