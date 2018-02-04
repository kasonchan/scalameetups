package logger

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent

/**
  * @author kasonchan
  * @since Feb-2018
  */
class ColoredLevel extends ClassicConverter {

  override def convert(event: ILoggingEvent): String = {
    event.getLevel match {
      case Level.TRACE => "[" + Console.BLUE + "TRACE" + Console.RESET + "]"
      case Level.DEBUG => "[" + Console.CYAN + "DEBUG" + Console.RESET + "]"
      case Level.INFO  => "[" + Console.WHITE + "INFO" + Console.RESET + "]"
      case Level.WARN  => "[" + Console.YELLOW + "WARN" + Console.RESET + "]"
      case Level.ERROR => "[" + Console.RED + "ERROR" + Console.RESET + "]"
    }
  }

}
