package logger

import org.slf4j.{Logger, LoggerFactory}

/**
  * @author kasonchan
  * @since 2018-02
  */
trait MyLogger {

  val log: Logger = LoggerFactory.getLogger(this.getClass)

}
