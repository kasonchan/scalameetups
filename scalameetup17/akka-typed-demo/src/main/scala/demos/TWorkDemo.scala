package demos

import actors.TTeam
import akka.actor.ActorSystem
import akka.typed
import akka.typed.scaladsl.adapter._
import akka.util.Timeout
import messages._

import scala.concurrent.duration._
import scala.io.StdIn

/**
  * @author kason.chan
  * @since Feb-2018
  */
object TWorkDemo {
  def main(args: Array[String]): Unit = {
    implicit val timeout = Timeout(5 seconds)

    val toffice: ActorSystem = akka.actor.ActorSystem("toffice")

    val tteam: typed.ActorRef[Messages] = toffice.spawn(TTeam.initial, "tteam")

    tteam ! Work

    StdIn.readLine()

    toffice.actorSelection("user/tteam/tdirector-1/tmanager/tworker-1") ! EscalateException

    StdIn.readLine()

    tteam ! Work

    StdIn.readLine()

    toffice.actorSelection("user/tteam/tdirector-1/tmanager/tworker-1") ! RestartException

    StdIn.readLine()

    tteam ! Work

    StdIn.readLine()

    tteam ! KillSpecialWatched

    StdIn.readLine()

    tteam ! Work

    StdIn.readLine()

    toffice.terminate()
  }
}
