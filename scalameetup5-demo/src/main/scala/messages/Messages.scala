package messages

/**
  * @author kasonchan
  * @since Aug-2017
  */
case object Ping

case object Pong

case object Hit

trait Messages {

  type Msg = String

}
