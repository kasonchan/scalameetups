package actors

import akka.typed.scaladsl.Actor
import akka.typed.{Behavior, PostStop, PreRestart, SupervisorStrategy}
import messages._

import scala.concurrent.duration._

/**
  * @author kason.chan
  * @since Feb-2018
  */
object TDirector {
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
      val tmanagers = ctx.spawn(behavior, "tmanager")
      msg match {
        case Work =>
          ctx.system.log.info("Director Work")
          tmanagers ! Work
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
