import actors.Worker
import akka.actor.{ActorRef, ActorSystem, Kill, Props}
import com.typesafe.config.ConfigFactory
import messages.Ping

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since Aug-2017
  */
object Demo {

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()

    val system = ActorSystem(name = "actorsystem")

    val worker: ActorRef = system.actorOf(Props[Worker], "worker")

    worker ! Ping

    worker ! Kill

    worker ! "Bye"

    val sleepTime = 60

    Thread.sleep(sleepTime)

    Await.result(system.terminate, Duration.Inf)

  }

}
