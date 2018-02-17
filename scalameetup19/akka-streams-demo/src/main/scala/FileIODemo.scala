import akka.actor.ActorSystem
import akka.stream.scaladsl.{Compression, FileIO, Flow, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult, Materializer}
import akka.util.ByteString
import akka.{Done, NotUsed}
import better.files.Dsl._
import better.files._

import scala.concurrent.Future
import scala.io.StdIn

/**
  * @author kasonchan
  * @since 2018-02
  */
object FileIODemo {

  def main(args: Array[String]): Unit = {

    val readFile = File("src/main/resources/helloworld.md.gz")

    val writeFile = File("src/main/resources/helloworld.md")

    val zipFile = File("src/main/resources/processedhelloworld.md.gz")

    println(s"Ungzipped $writeFile")
    ungzip(readFile)(writeFile)

    StdIn.readLine()

    println(s"Appended Processed by better-files! to $writeFile")
    writeFile.appendLines("Processed by better-files!")

    StdIn.readLine()

    println(s"Removed $writeFile")
    rm(writeFile)

    StdIn.readLine()

    // ------ //

    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer: Materializer = ActorMaterializer()

    // Create a source to read from path of readFile
    val source: Source[ByteString, Future[IOResult]] =
      FileIO.fromPath(readFile.path)

    // Create a printFlow to gunzip and map the elements as utf8String elements
    val printFlow: Flow[ByteString, String, NotUsed] =
      Flow[ByteString].via(Compression.gunzip()).map(_.utf8String)

    // Create a writeFlow to gunzip and map the elements as utf8String and convert to ByteString
    // And Append "Processed with Akka Streams!"
    val writeFlow: Flow[ByteString, ByteString, NotUsed] = Flow[ByteString]
      .via(Compression.gunzip())
      .map(_.utf8String)
      .map(s => ByteString(s.toString + s"Processed with Akka Streams!"))

    // Create a printSink to println each element in the stream
    val printSink: Sink[Any, Future[Done]] = Sink.foreach(println)

    // Create a writeSink to write gunzipped element to path of writeFile
    val writeSink: Sink[ByteString, Future[IOResult]] =
      FileIO.toPath(writeFile.path)

    // Create a printRunnable to connect source to printFlow and then to printSink
    val printRunnable = source.via(printFlow).to(printSink)

    system.log.info("Running source->writeFlow->writeSink")

    // Connect source via writeFlow and runWith writeSink
    source.via(writeFlow).runWith(writeSink)

    system.log.info("Running source->printFlow->printSink")

    // Run printRunnable
    printRunnable.run

    StdIn.readLine()

    system.log.info("Running zipSource->zipFlow->zipSink")

    // Create an unzippedSource to read from writeFile
    val zipSource: Source[ByteString, Future[IOResult]] =
      FileIO.fromPath(writeFile.path)

    // Create a zipFlow to gzip the ByteString
    val zipFlow = Flow[ByteString].via(Compression.gzip)

    // Create a zipSink to write to the path of zipFile
    val zipSink: Sink[ByteString, Future[IOResult]] =
      FileIO.toPath(zipFile.path)

    // Connect zipSource via zipFlow and runWith zipSink
    zipSource.via(zipFlow).runWith(zipSink)

    StdIn.readLine()

    // Terminate system
    system.terminate()
  }

}
