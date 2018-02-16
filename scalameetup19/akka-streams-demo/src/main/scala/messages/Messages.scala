package messages

import upickle.default.{ReadWriter => RW, macroRW}

/**
  * @author kasonchan
  * @since 2018-02-16
  */
sealed trait Messages

case class Encode(msg: String) extends Messages

object Encode {
  implicit def rw: RW[Encode] = macroRW

}

case class Encoded(msg: String) extends Messages

object Encoded {
  implicit def rw: RW[Encoded] = macroRW

}

case class Decode(msg: String) extends Messages

object Decode {
  implicit def rw: RW[Decode] = macroRW

}

case class Decoded(msg: String) extends Messages

object Decoded {
  implicit def rw: RW[Decoded] = macroRW

}

case class Meh(msg: String) extends Messages

object Meh {
  implicit def rw: RW[Meh] = macroRW

}
