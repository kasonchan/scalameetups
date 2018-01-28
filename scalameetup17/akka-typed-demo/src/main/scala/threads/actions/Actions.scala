package threads.actions

import akka.typed.ActorRef

/**
  * @author kasonchan
  * @since Jan-2018
  */
trait Action
case object Create extends Action
case object Start extends Action
case object Stop extends Action
case object UWork extends Action
case class TWork(replyTo: ActorRef[Action]) extends Action
case class TJobs(jobs: Int) extends Action
case object State extends Action
