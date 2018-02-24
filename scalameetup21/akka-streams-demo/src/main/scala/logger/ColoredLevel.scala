package logger

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent

/**
  * @author kasonchan
  * @since 2018-02-24
  */
class ColoredLevel extends ClassicConverter {

  override def convert(event: ILoggingEvent): String = {
    event.getLevel match {
      case Level.TRACE => mergeString(Level.TRACE, Console.BLUE)
      case Level.DEBUG => mergeString(Level.DEBUG, Console.CYAN)
      case Level.INFO  => mergeString(Level.INFO, Console.WHITE)
      case Level.WARN  => mergeString(Level.WARN, Console.YELLOW)
      case Level.ERROR => mergeString(Level.ERROR, Console.RED)
    }
  }

  private def mergeString(level: Level, color: String): String = {
    val mergedString = s"$color$level${Console.RESET}"
    f"$mergedString%-16s"
  }

}
