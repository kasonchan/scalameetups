import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

/**
  * @author kasonchan
  * @since Sep-2017
  */
object AkkaHTTPServer {

  implicit val system: ActorSystem = ActorSystem("system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  def main(args: Array[String]): Unit = {
    val (host, port) = ("localhost", 9000)

    val routes = new RoutesService().routes ~ SwaggerService.routes

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

}
