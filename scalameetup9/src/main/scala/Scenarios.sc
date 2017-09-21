case class Person(firstNames: String, lastNames: String)

import scala.annotation.tailrec
import scala.util.{Success, Try}

def makePeople(firstNames: Seq[String], lastNames: Seq[String]): Seq[Person] = {
  @tailrec
  def helper(firstNames: Seq[String], lastNames: Seq[String],
             people: Vector[Person]): Seq[Person] = {
    if (firstNames.isEmpty) people
    else {
      val newPerson = Person(firstNames.head, lastNames.head)
      helper(firstNames.tail, lastNames.tail, people :+ newPerson)
    }
  }

  helper(firstNames, lastNames, Vector[Person]())
}

val firstNames = Seq("Harry", "Ron", "Hermione", "Draco")
val lastNames = Seq("Potter", "Weasley", "Granger", "Malfoy")

makePeople(firstNames, lastNames)

val names = List("Viggo Mortensen", "Orlando Bloom", "Elijah Wood", "Ian McKellen", "Billy Boyd")

val initials = names map (_.split(" ") map (_.toUpperCase) map (_.charAt(0)) mkString)

case class Video(title: String, video_type: String, length: Int)

val v1 = Video("Pianocat Plays Carnegie Hall", "cat", 300)
val v2 = Video("Paint Drying", "home-improvement", 600)
val v3 = Video("Lord of the Rings", "Fantasy Fiction", 500)
val v4 = Video("Fuzzy McMittens Live At the Apollo", "cat", 200)

val videos = Seq(v1, v2, v3, v4)

def catVideosSum(videos: Seq[Video]): Int =
  videos
    .filter(video => video.video_type == "cat")
    .map(video => video.length)
    .sum

catVideosSum(videos)

def taxForState(amount: Double, state: Symbol): Double = state match {
  case ('NY) => amount * 0.0645
  case ('PA) => amount * 0.045
}

val nyTax = taxForState(_: Double, 'NY)
val paTax = taxForState(_: Double, 'PA)

nyTax(100)
paTax(20)

val maps = Map(42 -> "foo", 12 -> "bar", 1 -> "baz")

def lookup(key: Int): Option[String] = maps.get(key)

lookup(42)
lookup(3)

def expensiveLookup(id: Int): Option[String] = {
  Thread.sleep(1000)
  print(s"Doing expensive lookup for $id")
  maps.get(id)
}

expensiveLookup(42)
expensiveLookup(3)

def memoizeExpensiveLookup: (Int) => Option[String] = {
  var cache = Map[Int, Option[String]]()
  (id: Int) =>
    cache.get(id) match {
      case Some(result: Option[String]) => result
      case None => {
        val result = expensiveLookup(id)
        cache += id -> result
        result
      }
    }
}

val fastLookup = memoizeExpensiveLookup

fastLookup(42)
fastLookup(3)
