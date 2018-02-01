package demos

import actors.TThreadMinion
import akka.actor.{ActorSystem, Scheduler}
import akka.typed.ActorRef
import akka.typed.scaladsl.AskPattern._
import akka.typed.scaladsl.adapter._
import akka.util.Timeout
import threads.actions._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn

/**
  * @author kasonchan
  * @since Jan-2018
  */
object TThreadDemo {

  def main(args: Array[String]): Unit = {
    implicit val timeout: Timeout = Timeout(5 seconds)

    implicit val typedThreadPool: ActorSystem = akka.actor.ActorSystem("ttp")

    implicit val scheduler: Scheduler = typedThreadPool.scheduler

    val ttm: ActorRef[Action] =
      typedThreadPool.spawn(TThreadMinion.ttmBehavior, "ttm")

    ttm ! State
    ttm ! Create
    ttm ! State
    ttm ! Start
    ttm ! State
    val futureTtmJobs1 = ttm ? TWork
    val ttmJobs1 = Await.result(futureTtmJobs1, 5.seconds)
    println(ttmJobs1)
    ttm ! State
    val futureTtmJobs2 = ttm ? TWork
    val ttmJobs2 = Await.result(futureTtmJobs2, 5.seconds)
    println(ttmJobs2)
    ttm ! Stop
    ttm ! State
    ttm ! State

    StdIn.readLine() // let it run until user presses return
    typedThreadPool.terminate()
  }

}
