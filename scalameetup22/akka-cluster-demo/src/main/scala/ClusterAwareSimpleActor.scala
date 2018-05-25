import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster

/**
  * @author kasonchan
  * @since 2018-05
  */
class ClusterAwareSimpleActor extends Actor with ActorLogging {
  val cluster = Cluster(context.system)

  def receive = {
    case _ =>
      log.info(s"Created at ${cluster.selfUniqueAddress}")
  }
}
