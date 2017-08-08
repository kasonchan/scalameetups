package actors

import akka.actor.{Actor, ActorLogging}
import messages.{Msg, Ping}

/**
  * @author kasonchan
  * @since Aug-2017
  */
class Slave extends Actor with ActorLogging with Msg {

  override def preStart(): Unit = {
    log.info("Prestart")
  }

  override def postStop(): Unit = {
    log.info("Poststop")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.error(reason, s"${self.path} Restarting due to [{}] when processing [{}]",
      reason.getMessage, message.getOrElse(""))
  }

  def receive: PartialFunction[Any, Unit] = {
    case Ping =>
      log.info(s"Ping")
    case msg: Msg =>
      log.info(msg)
    case any @ _ =>
      log.warning(any.toString)
  }

}
