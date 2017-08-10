package actors

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorLogging, OneForOneStrategy}
import messages.{Msg, Ping}

import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since Aug-2017
  */
class Worker extends Actor with ActorLogging with Msg {
  val retries = 10

  override val supervisorStrategy: OneForOneStrategy =
    OneForOneStrategy(maxNrOfRetries = retries, withinTimeRange = 1.minute) {
      case _: Exception => Restart
    }

  override def preStart(): Unit = {
    log.info("Prestarting")
  }

  override def postStop(): Unit = {
    log.info("Poststopping")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.error(reason,
              s"${self.path} Restarting due to [{}] when processing [{}]",
              reason.getMessage,
              message.getOrElse(""))
  }

  override def postRestart(reason: Throwable): Unit = {
    log.error(reason, s"${self.path} Restarting due to [{}]", reason.getMessage)
  }

  def receive: PartialFunction[Any, Unit] = {
    case Ping =>
      log.info(s"Ping")
    case msg: Msg =>
      log.info(msg)
      sender() ! "Hello World!"
    case any @ _ =>
      log.warning(any.toString)
  }

}
