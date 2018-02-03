package actors

import akka.typed.Behavior
import akka.typed.scaladsl.Actor
import threads.actions._

/**
  * @author kasonchan
  * @since Jan-2018
  */
object TThreadMinion {
  val ttmBehavior: Behavior[Action] = NEW(jobs = 0)

  def NEW(jobs: Int): Behavior[Action] =
    Actor.immutable[Action] { (ctx, action) =>
      action match {
        case Create =>
          ctx.system.log.info("[New] -Create> [READY] {}", jobs)
          READY(jobs)
        case _ @msg =>
          ctx.system.log.info("[New] {}: {}", jobs, msg.toString)
          Actor.same
      }
    }

  private def READY(jobs: Int): Behavior[Action] =
    Actor.immutable[Action] { (ctx, action) =>
      action match {
        case Start =>
          ctx.system.log.info("[READY] -Start> [RUNNING] {}", jobs)
          RUNNING(jobs)
        case Stop =>
          ctx.system.log.info("[READY] -Stop> [TERMINATED] {}", jobs)
          TERMINATED(jobs)
        case _ @msg =>
          ctx.system.log.info("[READY] {}: {}", jobs, msg.toString)
          Actor.same
      }
    }

  private def RUNNING(jobs: Int): Behavior[Action] = {
    Actor.immutable[Action] { (ctx, action) =>
      action match {
        case Stop =>
          ctx.system.log.info("[RUNNING] -Stop> [TERMINATED] {}", jobs)
          TERMINATED(jobs)
        case TWork(replyTo) =>
          ctx.system.log.info("[RUNNING] {}: {} + 1 = {}", jobs, jobs, jobs + 1)
          replyTo ! TJobs(jobs + 1)
          RUNNING(jobs + 1)
        case _ @msg =>
          ctx.system.log.info("[RUNNING] {}: {}", jobs, msg.toString)
          Actor.same
      }
    }
  }

  private def TERMINATED(jobs: Int): Behavior[Action] = {
    Actor.immutable[Action] { (ctx, action) =>
      action match {
        case _ @msg =>
          ctx.system.log.info("[TERMINATED] {}: {}", jobs, msg.toString)
          Actor.same
      }
    }
  }

}
