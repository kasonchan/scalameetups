package actors

import akka.actor.{Actor, ActorRef, Terminated}
import com.typesafe.config.ConfigFactory
import logger.MyLogger
import messages.{Packet, Request, Response}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since 2018-02
  */
class Gateway extends Actor with MyLogger {

  override def preStart(): Unit = {
    log.info(s"$self prestarting...")
  }

  override def receive: Receive = {
    case p: Packet =>
      p match {
        case Request(msg) =>
          log.info(s"$self received $p")
          val conf = ConfigFactory.load()
          context.system
            .actorSelection(
              s"${conf.getString("master.remote.selection.path")}")
            .resolveOne(5 seconds)
            .onComplete {
              case Success(m: ActorRef) =>
                log.info(s"$self forwarding $p")
                m.forward(p)
              case Failure(e) =>
                log.warn(s"${e.getMessage}")
            }
        case Response(msg) =>
          log.info(s"$self received $p")
      }
    case Terminated(minion: ActorRef) =>
      log.warn(s"$minion is terminated")
  }

}
