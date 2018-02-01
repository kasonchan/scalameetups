package demos

import actors.UThreadMinion
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import threads.actions._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn

/**
  * @author kasonchan
  * @since Jan-2018
  */
object UThreadDemo {

  def main(args: Array[String]): Unit = {
    implicit val timeout = Timeout(5 seconds)

    val threadpool: ActorSystem = akka.actor.ActorSystem("threadpool")

    val utm: ActorRef = threadpool.actorOf(Props[UThreadMinion], "utm")

    utm ! Start
    utm ! Create
    utm ! State
    utm ! "What is your state?"
    utm ! Start
    utm ! State
    val futureUtmJobs1 = (utm ? UWork).mapTo[Int]
    val utmJobs1 = Await.result(futureUtmJobs1, 5.seconds)
    println(utmJobs1)
    utm ! State
    val futureUtmJobs2 = (utm ? UWork).mapTo[Int]
    val utmJobs2 = Await.result(futureUtmJobs2, 5.seconds)
    println(utmJobs2)
    utm ! Stop
    utm ! State
    utm ! State

    StdIn.readLine() // let it run until user presses return
    threadpool.terminate()
  }

}
