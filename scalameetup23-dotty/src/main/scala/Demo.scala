/**
  * @author kasonchan
  * @since 2018-06
  */
trait DayOfWeek

enum Weekday(dayOfWeek: Int) {
  case Mon extends Weekday(1) with DayOfWeek
  case Tues extends Weekday(2) with DayOfWeek
  case Wed extends Weekday(3) with DayOfWeek
  case Thurs extends Weekday(4) with DayOfWeek
  case Fri extends Weekday(5) with DayOfWeek
}

enum Weekend(dayOfWeek: Int) {
  case Sat extends Weekend(6) with DayOfWeek
  case Sun extends Weekend(7) with DayOfWeek
}

object Demo {

  def main(args: Array[String]): Unit = {
    val mon = Weekday.Mon
    println(mon)
    println(Weekday.Mon.enumTag)
    
    Weekday.Mon match {
      case d : Weekday => println(s"${d} is on weekday.")
      case e : Weekend => println(s"${e} is on weekend.")
      case _ => println("I don't recognize it.")
    }

    Weekend.Sat match {
      case d : Weekday => println(s"${d} is on weekday.")
      case e : Weekend => println(s"${e} is on weekend.")
      case _ => println("I don't recognize it.")
    }
  }

}
