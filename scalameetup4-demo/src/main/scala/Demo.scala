import actors.Slave
import akka.actor.{ActorSystem, Props}
import messages.Ping

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since Aug-2017
  */
object Demo {

  def main(args: Array[String]): Unit = {

    val system: ActorSystem = ActorSystem("system")

    val slave = system.actorOf(Props[Slave], "slave")

    slave ! Ping

    slave ! "Bye"

    val sleepTime = 60

    Thread.sleep(sleepTime)

    system.terminate()

    Await.result(system.terminate, Duration.Inf)

  }

}
