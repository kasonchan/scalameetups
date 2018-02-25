package actors

import akka.actor.{Actor, ActorRef, Props, Terminated}
import logger.MyLogger
import messages.{Packet, Request, Response}

/**
  * @author kasonchan
  * @since 2018-02
  */
class Gateway extends Actor with MyLogger {

  override def preStart(): Unit = {
    log.info(s"$self prestarting...")
    val minionR = context.system.actorOf(Props[BaseActor], "rminion")
    context.watch(minionR)
  }

  override def receive: Receive = {
    case Terminated(minion: ActorRef) =>
      log.warn(s"$minion is terminated")
    case p: Packet =>
      val minion = context.actorSelection(context.system.child("rminion"))
      p match {
        case Request(msg) =>
          log.info(s"$self received $p")
          minion ! Request(msg)
        case Response(msg) =>
          log.info(s"$self received $p")
      }
  }

}
