import actors.Gateway
import akka.actor.{ActorRef, ActorSystem, Kill, Props}
import logger.MyLogger
import messages.Request

import scala.annotation.tailrec
import scala.io.StdIn

/**
  * @author kasonchan
  * @since 2018-02
  */
object Demo extends MyLogger {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("minionsystem")

    val gateway = system.actorOf(Props[Gateway], "gateway")

    io(system, gateway)
  }

  @tailrec
  def io(system: ActorSystem, actor: ActorRef): Any = {
    StdIn.readLine() match {
      case "quit" | "q" =>
        actor ! Kill
        system.terminate()
      case x =>
        actor ! Request(x)
        io(system, actor)
    }
  }

}
