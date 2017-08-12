package actors

import akka.actor.{Actor, ActorLogging, Props}

/**
  * @author kasonchan
  * @since Aug-2017
  */
class DefaultConstructorWorker(defaultValues: (Int, Boolean, String)) extends Actor with ActorLogging {

  import context._
  import messages.Hit

  var internalInteger: Int = defaultValues._1
  var internalBoolean: Boolean = defaultValues._2
  var internalString: String = defaultValues._3

  def receive: PartialFunction[Any, Unit] = {
    case "internalInteger" =>
      log.info(s"${sender().path}: internalInteger")
//      context.actorSelection("**/me") ! internalString
      sender() ! internalInteger
    case "internalBoolean" =>
      log.info(s"${sender().path}: internalBoolean")
      sender() ! internalBoolean
    case "internalString" =>
      log.info(s"${sender().path}: internalString")
      sender() ! internalString
    case Hit =>
      log.info("Becoming angry")
      become(angry)
    case others @ _ =>
      log.warning(s"${sender().path}: $others")
      sender() ! "I don't understand"
  }

  def angry: Receive = {
    case any @ _ =>
      log.warning(s"${sender().path}: $any")
      sender() ! "I'm in angry state, ask me nothing!"
  }

}

object DefaultConstructorWorker {

  def props(defaultValues: (Int, Boolean, String)): Props = Props(new DefaultConstructorWorker(defaultValues))

}
