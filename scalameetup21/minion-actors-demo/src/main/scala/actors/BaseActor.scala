package actors

import akka.actor.Actor
import logger.MyLogger
import messages.{Packet, Request, Response}

/**
  * @author kasonchan
  * @since 2018-02
  */
class BaseActor extends Actor with MyLogger {

  override def preStart(): Unit = {
    log.info(s"$self have been created at ${self.path.address.hostPort}")
  }

  override def receive: Receive = {
    case n: Int =>
      log.info(s"$self received $n")
    case p: Packet =>
      p match {
        case Request(msg) =>
          log.info(s"$self received $p")
          sender ! Response(s"Received $msg by $self")
        case Response(msg) =>
          log.info(s"$self received $p")
      }
  }

}
