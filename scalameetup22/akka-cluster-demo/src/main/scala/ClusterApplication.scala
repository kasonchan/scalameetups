import actor.{ClusterListener, Exit}
import akka.actor.{ActorSystem, Props}

/**
  * @author kasonchan
  * @since 2018-05-25
  */

object ClusterApplication {

  def main(args: Array[String]): Unit = {
    if (args.isEmpty)
      startup(Seq("2551"))
    else
      startup(args)
  }

  def startup(ports: Seq[String]): Unit = {
    ports foreach { port =>

      // Creates an Akka system
      val system = ActorSystem("ClusterSystem")

      // Creates an actor that handles cluster domain events
      val clusterListener = system.actorOf(Props[ClusterListener], name = "clusterListener")

      clusterListener ! Exit
    }
  }

}
