package demos

import actors.UTeam
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import messages.{EscalateException, KillSpecialWatched, RestartException, Work}

import scala.concurrent.duration._
import scala.io.StdIn

/**
  * @author kasonchan
  * @since Jan-2018
  */
object UWorkDemo {
  def main(args: Array[String]): Unit = {
    implicit val timeout = Timeout(5 seconds)

    val uoffice: ActorSystem = akka.actor.ActorSystem("uoffice")

    val uteam: ActorRef = uoffice.actorOf(Props[UTeam], "uteam")

    uteam ! Work

    StdIn.readLine()

    uoffice.actorSelection("user/uteam/udirector-1/umanager/uworker-1") ! EscalateException

    StdIn.readLine()

    uteam ! Work

    StdIn.readLine()

    uoffice.actorSelection("user/uteam/udirector-1/umanager/uworker-1") ! RestartException

    StdIn.readLine()

    uteam ! Work

    StdIn.readLine()

    uteam ! KillSpecialWatched

    StdIn.readLine()

    uteam ! Work

    StdIn.readLine()

    uoffice.terminate()
  }
}
