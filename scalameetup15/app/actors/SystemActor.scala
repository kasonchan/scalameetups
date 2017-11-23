package actors

import javax.inject.Inject

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import models.{ChatRoom, Join}
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

/**
  * @author kasonchan
  * @since Nov-2017
  */
class SystemActor @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(
    implicit exec: ExecutionContext)
    extends Actor
    with ActorLogging {

  override def preStart() = {
    log.info("Prestart")
  }

  override def postStop() = {
    log.info("Poststop")
  }

  def receive = {
    case s: String =>
      log.info(s)
    case j: Join =>
      log.info(j.toString)
    case x =>
      log.error(x.toString)
  }

}

object SystemActor {

  // Create actor system
  val system: ActorSystem = ActorSystem("system")

  // Actor for logging
  val log: ActorRef = system.actorOf(Props[SystemActor], name = "log")

  // Actor for chatBody room
  val chatRoom: ActorRef = system.actorOf(Props[ChatRoom], name = "chatRoom")

}
