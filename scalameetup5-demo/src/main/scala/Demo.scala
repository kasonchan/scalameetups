import actors.TimedWorker.FirstTick
import actors.{DefaultConstructorWorker, TimedWorker, Worker}
import akka.actor.{ActorRef, ActorSystem, Inbox, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import messages.{Hit, Fine, Ping}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since Aug-2017
  */
object Demo {

  implicit val timeout = Timeout(5 seconds)

  def main(args: Array[String]): Unit = {

    val system: ActorSystem = ActorSystem(name = "actorsystem")

    val inbox = Inbox.create(system)

    val me: ActorRef = system.actorOf(Props[Worker], "me")

    val you: ActorRef = system.actorOf(Props[Worker], "you")

    me.tell(Ping, you)

    inbox.send(me, Ping)
    val reply = inbox.receive(5.seconds)
    print(s"$reply\n")

    val result1: Future[Any] = ask(me, Ping)
    result1.onComplete(println(_))

    val result2: Future[Any] = me ? Ping
    result2.onComplete(println(_))
    result2.pipeTo(you)
    pipe(result2) to you

    val dcw1: ActorRef = system.actorOf(DefaultConstructorWorker.props((1, false, "dc1")), "dcw1")

    inbox.send(dcw1, "internalInteger")
    val dcw1Integer = inbox.receive(10.seconds)
    print(s"$dcw1Integer\n")

    dcw1 ! Hit

    inbox.send(dcw1, "internalString")
    val dcw1String = inbox.receive(10.seconds)
    print(s"$dcw1String\n")

    dcw1 ! Fine

    inbox.send(dcw1, "internalBoolean")
    val dcw1Boolean = inbox.receive(50.seconds)
    print(s"$dcw1Boolean\n")

    val timer: ActorRef = system.actorOf(Props[TimedWorker], "timer")

    timer ! FirstTick

  }

}
