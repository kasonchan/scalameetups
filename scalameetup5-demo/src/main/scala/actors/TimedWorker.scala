package actors

import akka.actor.{Actor, ActorLogging, Timers}
import messages.Messages

import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since Aug-2017
  */
class TimedWorker extends Actor with ActorLogging with Messages with Timers {

  import TimedWorker._
  timers.startSingleTimer(TickKey, FirstTick, 5.seconds)

  def receive: PartialFunction[Any, Unit] = {
    case FirstTick =>
      timers.startPeriodicTimer(TickKey, Tick, 5.seconds)
    case Tick =>
      log.info("Tick")
    case others @ _ =>
      log.warning(s"${sender().path}: $others")
      sender() ! "I don't understand"
  }

  def angry: Receive = {
    case any @ _ =>
      log.warning(s"${sender().path}: $any")
      sender() ! "I'm in angry state, ask me nothing!"
  }

}

object TimedWorker {
  case object TickKey
  case object FirstTick
  case object Tick
}
