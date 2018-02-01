package actors

import akka.typed.scaladsl.Actor
import akka.typed.{Behavior, PostStop, PreRestart, SupervisorStrategy}
import messages._

import scala.concurrent.duration._

/**
  * @author kason.chan
  * @since Feb-2018
  */
object TTeam {
  private val maxNrOfRetries = 10

  Actor
    .supervise(TDirector.behavior)
    .onFailure[RestartException](
      SupervisorStrategy.restartWithLimit(maxNrOfRetries = maxNrOfRetries,
                                          withinTimeRange = 1.minute))
  Actor
    .supervise(TDirector.behavior)
    .onFailure[ResumeException](SupervisorStrategy.resume)

  def behavior: Behavior[Messages] =
    Actor.immutable[Messages] { (ctx, msg) =>
      val tdirectors = (1 to 2).map { i =>
        ctx.spawn(behavior, "tdirector-" + i)
      }
      val specialWatchedWorker = tdirectors.head
      ctx.watch(specialWatchedWorker)
      msg match {
        case Work =>
          ctx.system.log.info("Team Work")
          for (tdirector <- tdirectors) {
            tdirector ! Work
          }
          Actor.same
        case KillSpecialWatched =>
          ctx.system.log.warning("Kill specail watched {}",
                                 specialWatchedWorker.path)
          ctx.stop(specialWatchedWorker)
          Actor.same
      }
    } onSignal {
      case (ctx, PreRestart) =>
        ctx.system.log.info("Prerestart")
        Actor.same
      case (ctx, PostStop) =>
        ctx.system.log.info("PostStop")
        Actor.same
      case (ctx, akka.typed.Terminated(ref)) =>
        ctx.system.log
          .warning("{} IS DEAD!", ctx.self.path.name, ref.path.name)
        Actor.same
    }
}
