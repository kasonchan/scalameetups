import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{
  Broadcast,
  FileIO,
  Flow,
  GraphDSL,
  Keep,
  RunnableGraph,
  Sink,
  Source
}
import akka.util.ByteString

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn

/**
  * @author kason.chan
  * @since 2018-02
  */
object BackpressureDemo {

  def main(args: Array[String]): Unit = {

    def lineSink(filename: String,
                 streamName: String): Sink[String, Future[IOResult]] =
      Flow[String]
        .map { s =>
          println(s"${streamName}: $s")
          ByteString(s + "\n")
        }
        .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)

    implicit val system: ActorSystem = ActorSystem("factorials")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val source: Source[Int, NotUsed] = Source(1 to 100)

    // Create factorials from source
    val factorials: Source[BigInt, NotUsed] =
      source.scan(BigInt(1))((acc, next) => acc * next)

    val sink1 = lineSink("slowSinkFactorial.txt", "slowSink")
    val sink2 = lineSink("bufferedSinkFactorial.txt", "bufferedSink")
    val sink3 = lineSink("backpressureSinkFactorial.txt", "backpressureSink")

    val slowSink = Flow[String]
      .via(Flow[String]
        .throttle(1, 1.second, 1, ThrottleMode.shaping))
      .toMat(sink1)(Keep.right)

    val bufferedSink = Flow[String]
      .buffer(10, OverflowStrategy.dropNew)
      .via(Flow[String]
        .throttle(1, 1.second, 1, ThrottleMode.shaping))
      .toMat(sink2)(Keep.right)

    val backpressureSink = Flow[String]
      .buffer(10, OverflowStrategy.backpressure)
      .via(Flow[String]
        .throttle(1, 1.second, 1, ThrottleMode.shaping))
      .toMat(sink3)(Keep.right)

    val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val broadcast = b.add(Broadcast[String](3))
      factorials.map(_.toString) ~> broadcast.in

      broadcast.out(0) ~> slowSink
      broadcast.out(1) ~> bufferedSink
      broadcast.out(2) ~> backpressureSink

      ClosedShape
    })

    g.run()

    StdIn.readLine()
    system.terminate()
  }

}
