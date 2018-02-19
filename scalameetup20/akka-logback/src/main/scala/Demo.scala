import akka.actor.ActorSystem
import logger.MyLogger

import scala.io.StdIn

/**
  * @author kasonchan
  * @since 2018-02
  */
object Demo extends MyLogger {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("system")

    log.info("This is a info message")
    log.warn("This is a warning message")
    log.error("This is an error message")
    log.debug("This is a debug message")

    foo.info("This is a info message")

    StdIn.readLine()

    system.log.info("This is a info message")

    system.terminate()
  }

}
