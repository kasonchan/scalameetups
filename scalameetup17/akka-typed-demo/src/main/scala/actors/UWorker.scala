package actors

import akka.actor.{Actor, ActorLogging}
import messages._

/**
  * @author kasonchan
  * @since Jan-2018
  */
class UWorker extends Actor with ActorLogging {

  private val sleepTime = 500

  override def preStart(): Unit = {
    log.info("Prestart")
  }

  override def postStop(): Unit = {
    log.info("Poststop")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.warning("Prerestart: {}", reason.getLocalizedMessage)
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    log.warning("Postrestart: {}", reason.getLocalizedMessage)
    super.postRestart(reason)
  }

  def receive: PartialFunction[Any, Unit] = {
    case Work =>
      log.info("WORKING")
    case RestartException =>
      log.warning("RESTART")
      throw new RestartException
      Thread.sleep(sleepTime)
    case EscalateException =>
      log.warning("ESCALATE")
      throw new EscalateException
      Thread.sleep(sleepTime)
  }

}
