package actors

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.model.StatusCodes
import messages.Message

/**
  * @author kason.chan
  * @since Jan-2018
  */
class Logger extends Actor with ActorLogging {

  override def preStart(): Unit = {
    log.info("Prestarting")
  }

  override def postStop(): Unit = {
    log.info("Poststopping")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.error(reason,
      "${} Restarting due to [{}] when processing [{}]",
      self.path,
      reason.getMessage,
      message.getOrElse(""))
  }

  override def postRestart(reason: Throwable): Unit = {
    log.error(reason,
      s"${} Restarting due to [{}]",
      self.path,
      reason.getMessage)
  }

  def receive: PartialFunction[Any, Unit] = {
    case msg: Message =>
      msg match {
        case Message(StatusCodes.OK.intValue, _) => log.info(msg.toString)
        case Message(_, _)                       => log.error(msg.toString)
      }
    case any @ _ =>
      log.error(any.toString)
  }

}
