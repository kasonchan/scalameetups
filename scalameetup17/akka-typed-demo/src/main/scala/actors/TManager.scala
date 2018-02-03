package actors

import akka.typed.scaladsl.Actor
import akka.typed.{ActorRef, Behavior, PostStop, PreRestart, SupervisorStrategy}
import messages.{Messages, Work}

import scala.concurrent.duration._

/**
  * @author kason.chan
  * @since Feb-2018
  */
object TManager {
  private val maxNrOfRetries = 5

  def initial: Behavior[Messages] =
    Actor.immutable[Messages] { (ctx, msg) =>
      msg match {
        case Work =>
          ctx.system.log.info("{} [INITIAL] Manager Work", ctx.self.path)
          val tworkers = (1 to 2).map { i =>
            ctx.spawn(
              Actor
                .supervise(TWorker.ready)
                .onFailure[Exception](
                  SupervisorStrategy.restartWithLimit(
                    maxNrOfRetries = maxNrOfRetries,
                    withinTimeRange = 1.minute)),
              "tworker-" + i
            )
          }
          for (tworker <- tworkers) {
            ctx.watch(tworker)
            tworker ! Work
          }
          ready(tworkers)
      }
    } onSignal {
      case (ctx, PreRestart) =>
        ctx.system.log.info("{} Prerestart")
        Actor.same
      case (ctx, PostStop) =>
        ctx.system.log.info("{} PostStop")
        Actor.stopped
      case (ctx, akka.typed.Terminated(ref)) =>
        ctx.system.log
          .warning("{} {} IS DEAD!", ctx.self.path, ref.path)
        Actor.same
    }

  def ready(tworkers: Seq[ActorRef[Messages]]): Behavior[Messages] =
    Actor.immutable[Messages] { (ctx, msg) =>
      msg match {
        case Work =>
          ctx.system.log.info("{} [READY] Manager Work", ctx.self.path)
          for (tworker <- tworkers) {
            tworker ! Work
          }
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
