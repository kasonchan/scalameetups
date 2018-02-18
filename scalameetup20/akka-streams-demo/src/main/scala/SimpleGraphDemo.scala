import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape, Materializer}

import scala.io.StdIn

/**
  * @author kasonchan
  * @since 2018-02-17
  */
object SimpleGraphDemo {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer: Materializer = ActorMaterializer()

    val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._
      val in = Source(1 to 10)
      val out1 = Sink.foreach(println)
      val out2 = Sink.foreach(println)

      val bcast = builder.add(Broadcast[Int](3))
      val merge = builder.add(Merge[Int](2))

      val f1, f2, f3, f4, f5 = Flow[Int].map(_ + 10)

      in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> out1
                  bcast ~> f4 ~> merge
                  bcast ~> f5 ~> out2
      ClosedShape
    })

    graph.run()

    StdIn.readLine()
    system.terminate()

  }

}
