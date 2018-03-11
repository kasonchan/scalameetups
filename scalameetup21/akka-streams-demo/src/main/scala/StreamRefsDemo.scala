import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source, StreamRefs}
import akka.stream.{ActorMaterializer, Materializer, SourceRef}
import logger.MyLogger

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since 2018-02-24
  */
object StreamRefsDemo extends MyLogger {

  case class Offer(sr: SourceRef[Int])

  def main(args: Array[String]): Unit = {
    log.info("StreamRefDemo is starting...")

    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer: Materializer = ActorMaterializer

    val source: Source[Int, NotUsed] = Source(1 to 10)

    // Materialize the SourceRef
    val ref: Future[SourceRef[Int]] = source.runWith(StreamRefs.sourceRef())

    ref.onComplete {
      case Success(s) => s.source.runWith(Sink.foreach(println))
      case Failure(e) =>
    }
  }

}
