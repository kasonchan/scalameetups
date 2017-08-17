package actors

import akka.actor.SupervisorStrategy.Escalate
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props}
import akka.routing._

import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since Aug-2017
  */
class Supervisor extends Actor with ActorLogging {

  val maxNrOfRetries = 3

  override val supervisorStrategy: OneForOneStrategy =
    OneForOneStrategy(maxNrOfRetries = maxNrOfRetries,
                      withinTimeRange = 1 minute) {
      case _: Exception => Escalate
    }

  private val router: Router = {
    val routees = Vector {
      val cw1 = context.actorOf(Props[Worker], "childWorker1")
      context watch cw1
      ActorRefRoutee(cw1)
    } ++ Vector {
      val cw2 = context.actorOf(Props[Worker], "childWorker2")
      context watch cw2
      ActorRefRoutee(cw2)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive: PartialFunction[Any, Unit] = {
    case any @ _ =>
      log.info(s"${sender().path} ${any.toString}")
      router.route(any, self)
  }

}
