import actors.LoadActor
import akka.actor.{ActorSystem, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}
import messages.Load

/**
  * @author kasonchan
  * @since Feb-2018
  */
object Demo {
  def main(args: Array[String]): Unit = {
    val system: ActorSystem = ActorSystem("system")
    val resizer = DefaultResizer(lowerBound = 1, upperBound = 10)
    val router =
      system.actorOf(RoundRobinPool(5, Some(resizer)).props(Props[LoadActor]))
    router ! Load
    system.terminate()
  }
}
