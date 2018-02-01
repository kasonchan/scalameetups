package messages

/**
  * @author kasonchan
  * @since Jan-2018
  */
trait Messages

case class RestartException() extends Exception("Restart") with Messages

case class ResumeException() extends Exception("Resume") with Messages

case class StopException() extends Exception("Stop") with Messages

case class EscalateException() extends Exception("Escalate") with Messages

case object Work extends Messages

case object KillSpecialWatched extends Messages
