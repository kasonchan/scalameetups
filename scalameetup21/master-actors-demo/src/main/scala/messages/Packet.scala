package messages

/**
  * @author kasonchan
  * @since 2018-02
  */
trait Packet
case class Request(msg: String) extends Packet
case class Response(msg: String) extends Packet
