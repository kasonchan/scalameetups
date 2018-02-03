package actors

import akka.typed.scaladsl.Actor
import akka.typed.{Behavior, PostStop, PreRestart}
import messages.{EscalateException, Messages, RestartException, Work}

/**
  * @author kason.chan
  * @since Feb-2018
  */
object TWorker {
  private val sleepTime = 500

  def ready: Behavior[Messages] =
    Actor.immutable[Messages] { (ctx, msg) =>
      msg match {
        case Work =>
          ctx.system.log.info("{} [READY] WORKING", ctx.self.path)
          Actor.same
        case restart: RestartException =>
          ctx.system.log.info("{} [READY] RESTART", ctx.self.path)
          throw restart
          Thread.sleep(sleepTime)
          Actor.same
        case escalate: EscalateException =>
          ctx.system.log.info("{} [READY] ESCALATE", ctx.self.path)
          throw escalate
          Thread.sleep(sleepTime)
          Actor.same
      }
    } onSignal {
      case (ctx, PreRestart) =>
        ctx.system.log.info("{} Prerestart", ctx.self.path)
        Actor.same
      case (ctx, PostStop) =>
        ctx.system.log.info("{} PostStop", ctx.self.path)
        Actor.stopped
    }
}
