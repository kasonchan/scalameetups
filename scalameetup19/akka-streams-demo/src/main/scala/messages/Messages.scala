package messages

import upickle.default.{macroRW, ReadWriter => RW}

/**
  * @author kasonchan
  * @since 2018-02
  */
case class Packet(status: String, msg: String)

object Packet {
  implicit def rw: RW[Packet] = macroRW
}
