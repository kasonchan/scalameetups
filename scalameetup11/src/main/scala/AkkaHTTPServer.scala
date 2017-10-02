import akka.actor.ActorSystem
import akka.http.scaladsl.Http
<<<<<<< HEAD
=======
import akka.http.scaladsl.server.Directives._
>>>>>>> 1e0d535a7897921c6847aff88579d8bc4f66a82f
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

<<<<<<< HEAD
    val routes = new RoutesService().routes ~ SwaggerService.routes
=======
    val routes = new HarryPotterService().routes ~ SwaggerService.routes
>>>>>>> 1e0d535a7897921c6847aff88579d8bc4f66a82f

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
