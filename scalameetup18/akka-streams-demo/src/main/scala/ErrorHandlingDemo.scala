import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.io.StdIn

case class RestartException() extends Exception("Restart exception")

case class ResumeException() extends Exception("Resume exception")

case class StopException() extends Exception("Stop exception")

/**
  * @author kasonchan
  * @since 2018-02
  * References from Akka Cookbook
  */
object ErrorHandlingDemo {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("system")

    val streamDecider: Supervision.Decider = {
      case e: IndexOutOfBoundsException =>
        system.log.warning("Dropping element because of {} and resuming.",
                           e.getLocalizedMessage)
        Supervision.Resume
      case e @ _ =>
        system.log.error("Dropping element because of {} and stop.",
                         e.getLocalizedMessage)
        Supervision.Stop
    }

    val flowDecider: Supervision.Decider = {
      case e: IllegalArgumentException =>
        system.log.warning("Dropping element because of {} and restarting.",
                           e.getLocalizedMessage)
        Supervision.Restart
      case e @ _ =>
        system.log.error("Dropping element because of {} and stop.",
                         e.getLocalizedMessage)
        Supervision.Stop
    }

    val materializerSettings = ActorMaterializerSettings(system = system)
      .withSupervisionStrategy(streamDecider)

    implicit val materializer: Materializer = ActorMaterializer(
      materializerSettings)

    val proLangs = List("C",
                        "C++",
                        "Error",
                        "Python",
                        "Java",
                        "Scala",
                        "",
                        "JavaScript",
                        "Ruby",
                        "Stop",
                        "PHP",
                        "ScalaJS")

    val flow = Flow[String]
      .map(proLang => {
        if (proLang.length == 0)
          throw new IllegalArgumentException("Empty string is not allowed")
        else if (proLang == "Stop") throw new StopException
        else proLang
      })
      .withAttributes(ActorAttributes.supervisionStrategy(flowDecider))

    Source(proLangs)
      .via(flow)
      .map(proLang => (proLang, proLang(2)))
      .to(Sink.foreach(t => system.log.info(t.toString())))
      .run()

    StdIn.readLine()
    system.terminate()
  }

}
