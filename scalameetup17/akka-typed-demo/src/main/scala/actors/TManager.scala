package actors

import akka.typed.scaladsl.Actor
import akka.typed.{Behavior, PostStop, PreRestart, SupervisorStrategy}
import messages.{Messages, RestartException, ResumeException, Work}

import scala.concurrent.duration._

/**
  * @author kason.chan
  * @since Feb-2018
  */
object TManager {
  private val maxNrOfRetries = 5

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
      val tworkers = (1 to 2).map { i =>
        ctx.spawn(behavior, "tworker-" + i)
      }
      msg match {
        case Work =>
          ctx.system.log.info("Manager Work")
          for (tworker <- tworkers) {
            tworker ! Work
          }
          Actor.same
      }
    } onSignal {
      case (ctx, PreRestart) =>
        ctx.system.log.info("Prerestart")
        Actor.same
      case (ctx, PostStop) =>
        ctx.system.log.info("PostStop")
        Actor.same
    }
}
