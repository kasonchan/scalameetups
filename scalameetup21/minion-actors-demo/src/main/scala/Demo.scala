import actors.Gateway
import akka.actor.{ActorSystem, Props}
import logger.MyLogger

/**
  * @author kasonchan
  * @since 2018-02
  */
object Demo extends MyLogger {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("minionsystem")

    val gateway = system.actorOf(Props[Gateway], "gateway")
  }

}
