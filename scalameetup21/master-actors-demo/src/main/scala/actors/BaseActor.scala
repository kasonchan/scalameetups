package actors

import akka.actor.Actor
import logger.MyLogger
import messages.{Packet, Request, Response}

/**
  * @author kasonchan
  * @since 2018-02
  */
case object Ping

case object Pong

class BaseActor extends Actor with MyLogger {

  override def preStart(): Unit = {
    log.info(s"$self has been created at ${self.path.address.hostPort}")
  }

  override def receive: Receive = {
    case p: Packet =>
      p match {
        case Request(msg) =>
          log.info(s"$self received $p")
          sender ! Response(s"Received $msg")
        case Response(msg) =>
          log.info(s"$self received $p")
      }
  }

}
