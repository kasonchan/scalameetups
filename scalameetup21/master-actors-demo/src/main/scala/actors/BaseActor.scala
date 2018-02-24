package actors

import akka.actor.Actor
import logger.MyLogger

/**
  * @author kasonchan
  * @since 2018-02
  */
case object Ping

case object Pong

class BaseActor extends Actor with MyLogger {

  override def preStart(): Unit = {
    log.info(s"I have been created at ${self.path.address.hostPort}")
  }

  override def receive: Receive = {
    case msg@_ =>
      log.info(s"$self received $msg")
  }


}
