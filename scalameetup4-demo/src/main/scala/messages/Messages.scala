package messages

/**
  * @author kasonchan
  * @since Aug-2017
  */
case object Ping

case object Pong

trait Msg {
  type Msg = String
}
