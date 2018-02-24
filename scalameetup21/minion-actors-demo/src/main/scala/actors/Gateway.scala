package actors

import akka.actor.{Actor, Props, Terminated}
import logger.MyLogger

/**
  * @author kasonchan
  * @since 2018-02
  */
class Gateway extends Actor with MyLogger {

  override def preStart(): Unit = {
    val master = context.system.actorOf(Props[BaseActor], "master")
    context.watch(master)
    log.info(s"I have been created at ${self.path.address.hostPort}")
  }

  override def receive: Receive = {
    case Ping =>
      log.info(s"$self received $Ping")
      sender() ! Pong
    case Pong => log.info(s"$self received $Pong")
    case Terminated(master) =>
      val master = context.system.actorOf(Props[BaseActor], "master")
      context.watch(master)
    case msg @ _ => log.info(s"$self received $msg")
  }

}
