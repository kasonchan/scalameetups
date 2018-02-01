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

  def behavior: Behavior[Messages] =
    Actor.immutable[Messages] { (ctx, msg) =>
      msg match {
        case Work =>
          ctx.system.log.info("WORKING")
          Actor.same
        case restart: RestartException =>
          ctx.system.log.info("RESTART")
          throw restart
          Thread.sleep(sleepTime)
          Actor.same
        case escalate: EscalateException =>
          ctx.system.log.info("ESCALATE")
          throw escalate
          Thread.sleep(sleepTime)
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
