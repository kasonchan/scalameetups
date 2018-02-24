import actors.{BaseActor, Gateway, Ping}
import akka.actor.{ActorRef, ActorSystem, Kill, Props}
import logger.MyLogger

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.io.StdIn

/**
  * @author kasonchan
  * @since 2018-02
  */
object Demo extends MyLogger {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("mastersystem")

    system.log.info("Master is starting...")

    val gateway = system.actorOf(Props[Gateway], "gateway")

//    system.scheduler.schedule(1 second, 1 second, gateway, Ping)

    msg(gateway)
  }

  @tailrec
  def msg(actor: ActorRef): Any = {
    StdIn.readLine() match {
      case "quit" | "q" =>
        actor ! Kill
      case x =>
        actor ! x
        msg(actor)
    }
  }

}
