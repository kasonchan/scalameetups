package messages

import akka.actor.ActorRef

/**
  * @author kasonchan
  * @since Aug-2017
  */
case object Ping

case object Pong

trait Msg {
  type Msg = String
}

case class DeadLetter(message: Any, sender: ActorRef, recipient: ActorRef)
