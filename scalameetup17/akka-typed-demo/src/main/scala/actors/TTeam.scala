package actors

import akka.typed.scaladsl.{Actor, ActorContext}
import akka.typed.{
  ActorRef,
  Behavior,
  PostStop,
  PreRestart,
  SupervisorStrategy,
  scaladsl
}
import messages._

import scala.concurrent.duration._

/**
  * @author kason.chan
  * @since Feb-2018
  */
object TTeam {
  private val maxNrOfRetries = 10

  private def spawnAndWatchWorkers(
      ctx: ActorContext[Messages]): Seq[ActorRef[Messages]] = {
    (1 to 2).map { i =>
      val w = ctx.spawn(
        Actor
          .supervise(TDirector.initial)
          .onFailure[RestartException](
            SupervisorStrategy.restartWithLimit(maxNrOfRetries = maxNrOfRetries,
                                                withinTimeRange = 1.minute)),
        "tdirector-" + i
      )
      ctx.watch(w)
      w
    }
  }

  def initial: Behavior[Messages] =
    Actor.immutable[Messages] { (ctx: scaladsl.ActorContext[Messages], msg) =>
      msg match {
        case Work =>
          val tdirectors = spawnAndWatchWorkers(ctx)
          ctx.system.log.info("{} [INITIAL] Team Work", ctx.self.path)
          for { tdirector <- tdirectors } {
            ctx.watch(tdirector)
            tdirector ! Work
          }
          ready(tdirectors)
      }
    } onSignal {
      case (ctx, PreRestart) =>
        ctx.system.log.info("Prerestart")
        Actor.same
      case (ctx, PostStop) =>
        ctx.system.log.info("PostStop")
        Actor.stopped
      case (ctx, akka.typed.Terminated(ref)) =>
        ctx.system.log
          .warning("{} IS DEAD!", ctx.self.path, ref.path)
        Actor.same
    }

  def ready(tdirectors: Seq[ActorRef[Messages]]): Behavior[Messages] =
    Actor.immutable[Messages] { (ctx: scaladsl.ActorContext[Messages], msg) =>
      msg match {
        case Work =>
          ctx.system.log.info("{} [READY] Team Work", ctx.self.path)
          for { tdirector <- tdirectors } tdirector ! Work
          Actor.same
        case KillSpecialWatched =>
          ctx.system.log.warning("{} [READY] Kill special watched {}",
                                 ctx.self.path,
                                 ctx.children.head.path)
          ctx.stop(ctx.children.head)
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
