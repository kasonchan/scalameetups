package actors

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{
  Actor,
  ActorLogging,
  AllForOneStrategy,
  OneForOneStrategy,
  Props
}
import messages._

import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since Jan-2018
  */
class UManager extends Actor with ActorLogging {

  private val maxNrOfRetries = 5

  private val uworkers = (1 to 2).map { i =>
    context.actorOf(Props[UWorker], "uworker-" + i)
  }

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

  override val supervisorStrategy: AllForOneStrategy =
    AllForOneStrategy(maxNrOfRetries = maxNrOfRetries,
                      withinTimeRange = 1.minute) {
      case _: RestartException  => Restart
      case _: ResumeException   => Resume
      case _: StopException     => Stop
      case _: EscalateException => Escalate
    }

  def receive: PartialFunction[Any, Unit] = {
    case Work =>
      log.info("Manager Work")
      for (uworker <- uworkers) {
        uworker ! Work
      }
  }

}
