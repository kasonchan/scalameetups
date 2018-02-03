package actors

import akka.typed.scaladsl.Actor
import akka.typed.{ActorRef, Behavior, PostStop, PreRestart, SupervisorStrategy}
import messages._

import scala.concurrent.duration._

/**
  * @author kason.chan
  * @since Feb-2018
  */
object TDirector {
  private val maxNrOfRetries = 10

  def initial: Behavior[Messages] =
    Actor.immutable[Messages] { (ctx, msg) =>
      msg match {
        case Work =>
          ctx.system.log.info("{} [INITIAL] Director Work", ctx.self.path)
          val tmanagers = ctx.spawn(
            Actor
              .supervise(TManager.initial)
              .onFailure[Exception](
                SupervisorStrategy.restartWithLimit(
                  maxNrOfRetries = maxNrOfRetries,
                  withinTimeRange = 1.minute)),
            "tmanager"
          )
          ctx.watch(tmanagers)
          tmanagers ! Work
          ready(tmanagers)
      }
    } onSignal {
      case (ctx, PreRestart) =>
        ctx.system.log.info("{} Prerestart", ctx.self.path)
        Actor.same
      case (ctx, PostStop) =>
        ctx.system.log.info("{} PostStop", ctx.self.path)
        Actor.stopped
      case (ctx, akka.typed.Terminated(ref)) =>
        ctx.system.log
          .warning("{} {} IS DEAD!", ctx.self.path, ref.path)
        Actor.same
    }

  def ready(tmanagers: ActorRef[Messages]): Behavior[Messages] =
    Actor.immutable[Messages] { (ctx, msg) =>
      msg match {
        case Work =>
          ctx.system.log.info("{} [READY] Director Work", ctx.self.path)
          tmanagers ! Work
          Actor.same
      }
    } onSignal {
      case (ctx, PreRestart) =>
        ctx.system.log.info("{} Prerestart", ctx.self.path)
        Actor.same
      case (ctx, PostStop) =>
        ctx.system.log.info("{} PostStop", ctx.self.path)
        Actor.stopped
      case (ctx, akka.typed.Terminated(ref)) =>
        ctx.system.log
          .warning("{} {} IS DEAD!", ctx.self.path, ref.path)
        Actor.same
    }
}
