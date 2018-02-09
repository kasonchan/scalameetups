import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.io.StdIn

/**
  * @author kasonchan
  * @since 2018-02
  *
  */
object SimpleStreamDemo {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("system")

    implicit val materializer: ActorMaterializer = ActorMaterializer()

    // Create a source with 1 to 10
    val source = Source(1 to 10)

    // Create a flow to multiple two to each element
    val flow = Flow[Int].map(_ * 2)

    // Create a sink to print each element in a new line
    val sink = Sink.foreach(println)

    // Connect the source via flow twice and then to the sink
    val runnable = source.via(flow).via(flow).to(sink)

    // Execute the stream
    runnable.run()

    StdIn.readLine()

    // Shutdown the system
    system.terminate()
  }
}
