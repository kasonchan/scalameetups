import actors.Gateway
import akka.actor.{ActorRef, ActorSystem, Kill, Props}
import logger.MyLogger
import messages.Request

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Failure, Success, Try}

/**
  * @author kasonchan
  * @since 2018-02
  */
object Demo extends MyLogger {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("mastersystem")

    val gateway = system.actorOf(Props[Gateway], "gateway")

    io(system, gateway)
  }

  @tailrec
  def io(system: ActorSystem, actor: ActorRef): Any = {
    StdIn.readLine() match {
      case "quit" | "q" =>
        log.info("Quitting...")
        actor ! Kill
        system.terminate()
      case x =>
        Try {
          x.toInt
        } match {
          case Success(num) =>
            log.info(num.toString)
            actor ! num
            io(system, actor)
          case Failure(e) =>
            log.info(x.toString)
            actor ! Request(x)
            io(system, actor)
        }
    }
  }

}
