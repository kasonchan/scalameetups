package actor

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import msg.{Exit, ExitAll, Msg}

/**
  * @author kasonchan
  * @since 2018-05
  */
class ClusterListener extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  // Subscribes to cluster changes
  override def preStart(): Unit = {
    cluster.subscribe(self,
                      initialStateMode = InitialStateAsEvents,
                      classOf[MemberEvent],
                      classOf[UnreachableMember])
  }

  // Resubscribes when restart
  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case MemberUp(member) =>
      log.info("Member is Up: {}", member.address)
    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}", member.address, previousStatus)
    case Exit =>
      val address = Cluster(context.system).selfAddress
      Cluster(context.system).leave(address)
    case ExitAll =>
      context.system.terminate()
    case Msg(text: String) =>
      log.info("received: {}", text)
    case _: MemberEvent =>
  }

}
