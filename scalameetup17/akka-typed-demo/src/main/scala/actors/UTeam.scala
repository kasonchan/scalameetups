package actors

import akka.actor.SupervisorStrategy.{Restart, Resume, Stop}
import akka.actor.{
  Actor,
  ActorLogging,
  ActorRef,
  OneForOneStrategy,
  Props,
  Terminated
}
import messages._

import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since Jan-2018
  */
class UTeam extends Actor with ActorLogging {

  private val maxNrOfRetries = 10

  private val udirectors = (1 to 2).map { i =>
    context.actorOf(Props[UDirector], "udirector-" + i)
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

  private val specialWatchedWorker: ActorRef = udirectors.head

  context.watch(specialWatchedWorker)

  override val supervisorStrategy: OneForOneStrategy =
    OneForOneStrategy(maxNrOfRetries = maxNrOfRetries,
                      withinTimeRange = 1.minute) {
      case _: RestartException  => Restart
      case _: ResumeException   => Resume
      case _: StopException     => Stop
      case _: EscalateException => Restart
    }

  def receive: PartialFunction[Any, Unit] = {
    case Work =>
      log.info("Team Work")
      for (udirector <- udirectors) {
        udirector ! Work
      }
    case KillSpecialWatched =>
      log.warning("Kill special watched {}", specialWatchedWorker.path)
      context.stop(specialWatchedWorker)
    case Terminated(actor) =>
      if (actor == specialWatchedWorker) {
        log.warning("{} IS DEAD!", specialWatchedWorker.path)
      }
  }

}
