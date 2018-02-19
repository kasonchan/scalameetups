package logger

import org.slf4j.{Logger, LoggerFactory}

/**
  * @author kasonchan
  * @since 2018-02-19
  */
class MyLogger {

  val log: Logger = LoggerFactory.getLogger(this.getClass)

  val foo: Logger = LoggerFactory.getLogger("foo")

}
