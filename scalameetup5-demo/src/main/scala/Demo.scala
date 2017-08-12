import actors.{DefaultConstructorWorker, Worker}
import akka.actor.{ActorRef, ActorSystem, Inbox, Props}
import akka.pattern.ask
import akka.util.Timeout
import messages.{Hit, Ping}

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

    val dcw1: ActorRef = system.actorOf(DefaultConstructorWorker.props((1, false, "dc1")), "dcw1")

    inbox.send(dcw1, "internalInteger")
    val dcw1Reply1 = inbox.receive(5.seconds)
    print(s"$dcw1Reply1\n")

    dcw1 ! Hit

    inbox.send(dcw1, "internalInteger")
    val dcw1Reply2 = inbox.receive(200.seconds)
    print(s"$dcw1Reply2\n")

  }

}
