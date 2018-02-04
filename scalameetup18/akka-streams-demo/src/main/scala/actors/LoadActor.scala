package actors

import akka.actor.Actor
import messages.Load
import org.slf4j.{Logger, LoggerFactory}

/**
  * @author kasonchan
  * @since Feb-2018
  */
class LoadActor extends Actor {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def receive: Receive = {
    case Load =>
      logger.info("Handling loads of requests")
      logger.warn("Handling loads of requests")
      logger.error("Handling loads of requests")
      logger.debug("Handling loads of requests")
  }

}
