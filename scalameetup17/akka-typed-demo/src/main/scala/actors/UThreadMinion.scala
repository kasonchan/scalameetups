package actors

import akka.actor.{Actor, ActorLogging}
import threads.actions._

/**
  * @author kasonchan
  * @since Jan-2018
  */
class UThreadMinion extends Actor with ActorLogging {
  private var jobs = 0

  def receive: PartialFunction[Any, Unit] = {
    case Create =>
      log.info("[NEW] -Create> [READY] {}", jobs)
      context.become(READY)
    case _ @msg =>
      log.info("[NEW] {}: {}", jobs, msg.toString)
  }

  private def READY: Receive = {
    case Start =>
      log.info("[READY] -Start> [RUNNING] {}", jobs)
      context.become(RUNNING)
    case Stop =>
      log.info("[READY] -Stop> [TERMINATED] {}", jobs)
      context.become(TERMINATED)
    case _ @msg =>
      log.info("[READY] {}: {}", jobs, msg.toString)
  }

  private def RUNNING: Receive = {
    case Stop =>
      log.info("[RUNNING] -Stop> [TERMINATED] {}", jobs)
      context.become(TERMINATED)
    case UWork =>
      log.info("[RUNNING] {}: {} + 1 = {}", jobs, jobs, jobs + 1)
      jobs += 1
      sender() ! jobs
    case _ @msg =>
      log.info("[RUNNING] {}: {}", jobs, msg.toString)
  }

  private def TERMINATED: Receive = {
    case _ @msg =>
      log.info("[TERMINATED] {}: {}", jobs, msg.toString)
  }
}
