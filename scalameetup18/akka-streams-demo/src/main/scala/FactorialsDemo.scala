import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult, ThrottleMode}
import akka.util.ByteString

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn
import scala.util.{Failure, Success}

import scala.concurrent.duration._

/**
  * Reference from https://doc.akka.io/docs/akka/2.5/stream/stream-quickstart.html
  */
object FactorialsDemo {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("factorials")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val source: Source[Int, NotUsed] = Source(1 to 100)

    // Create factorials from source
    val factorials = source.scan(BigInt(1))((acc, next) ⇒ acc * next)

    // Write 1 factorial as ByteString per line to a file named factorials1.txt
    val result1: Future[IOResult] =
      factorials
        .map(num => ByteString(s"$num\n"))
        .runWith(FileIO.toPath(Paths.get("factorials1.txt")))

    // When result1 is completed successfully, system log info "Finished writing factorial to file
    // Otherwise system log warning error message
    result1.onComplete {
      case Success(r)     => system.log.info("Finished writing factorial to file.")
      case Failure(error) => system.log.warning("{}.", error.getMessage)
    }

    StdIn.readLine()

    // Create a Sink to repeat the same behavior as result1
    def lineSink(filename: String): Sink[String, Future[IOResult]] =
      Flow[String]
        .map(s ⇒ ByteString(s + "\n"))
        .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)

    // Run factorials with linkSink with input factorials2.txt
    val result2 =
      factorials.map(_.toString).runWith(lineSink("factorials2.txt"))

    // When result2 is completed successfully, system log info "Finished writing factorial to file
    // Otherwise system log warning error message
    result2.onComplete {
      case Success(r)     => system.log.info("Finished writing factorial to file.")
      case Failure(error) => system.log.warning("{}.", error.getMessage)
    }

    StdIn.readLine()

    factorials
      .zipWith(source)((num, idx) ⇒ s"$idx! = $num")
      .throttle(1, 1.second, 1, ThrottleMode.shaping)
      .runForeach(println)

    StdIn.readLine()
    system.terminate()

  }

}
