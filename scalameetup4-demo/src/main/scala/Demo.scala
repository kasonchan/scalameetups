import actors.Worker
import akka.actor.{ActorRef, ActorSystem, DeadLetter, Inbox, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since Aug-2017
  */
object Demo {

  def main(args: Array[String]): Unit = {

    // Default it is load from the file called application.conf
    val config = ConfigFactory.load()

    val system = ActorSystem(name = "actorsystem",
                             config = config.getConfig("application"))

    val worker: ActorRef = system.actorOf(Props[Worker], "worker")

    val deadLetter = system.actorOf(Props[Worker], "deadLetter")

    system.eventStream.subscribe(deadLetter, classOf[DeadLetter])

    // Create an "actor-in-a-box"
    val inbox = Inbox.create(system)

    inbox.send(worker, "Hello Actor!")
    val slaveReply = inbox.receive(5.seconds)
    print(s"$slaveReply\n")

//    worker ! Ping

//    worker ! PoisonPill
//    worker ! Kill

    worker ! "Bye"

    val sleepTime = 60

    Thread.sleep(sleepTime)

    system.terminate()

    Await.result(system.terminate, Duration.Inf)

  }

}
