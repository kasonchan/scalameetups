# Functional Patterns (in Scala)

| Functional Patterns (in Scala)

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

Functional Patterns

- Tail Recursion
- Mutual Recursion
- Filter-Map-Reduce
- Chain of Operations
- Function Builder
- Memoization
- Lazy Sequences

---

Scenario: You are given an integer `upTo`, sum up all the number start from 0 to
the integer inclusively. How can we implement it in a a imperative way?

---

```
// Imperative sum
def sum(upTo: Int): Int = {
  var sum = 0
  for (i <- 0 to upTo)
    sum = sum + i
  sum
}
```

---

Scenario: You are given an integer `upTo`, sum up all the number start from 0 to
the integer inclusively. How can we implement it in recursive way?

---

```
// Recursive sum
def recursiveSum(upTo: Int): Int = {
  if (upTo == 0) 0
  else upTo + sum(upTo - 1)
}
```
--

- Each recursive call leads to another frame on program's call stack.

---

```
// Recursive sum
def recursiveSum(upTo: Int): Int = {
  upTo match {
    case 0 => 0
    case _: Int => upTo + sum(upTo - 1)
  }
}
```
--

- Each recursive call leads to another frame on program's call stack.

---

Tail Recursion

```
// Tail recursive sum
import scala.annotation.tailrec

@tailrec
def tailRecursiveSum(upTo: Int, sum: Int): Int = {
  if (upTo == 0) sum
  else sumTail(upTo - 1, sum + upTo)
}
```

---

Tail Recursion

```
// Tail recursive sum
import scala.annotation.tailrec

@tailrec
def tailRecursiveSum(upTo: Int, sum: Int): Int = {
  upTo match {
    case 0 => sum
    case _: Int => sumTail(upTo - 1, sum + upTo)
  }
}
```

---

Tail Recursion

- Tail recursion is a special form of recursion.
- Tail call optimization (TCO) optimizes to use a single frame on the stack.
- @tailrec is a method annotation which verifies that the method
  will be compiled with tail call optimization.
- If @tailrec is present, the compiler will issue an error if the method
  cannot be optimized into a loop.

---

Tail Recursion

Scenario: We are given a list of first names and a list of last names. How can we
form a list of people tail recursively, assuming that the indices matches from
the first names and last names.

--

```
val firstNames = Seq("Harry", "Ron", "Hermione", "Draco")
val lastNames = Seq("Potter", "Weasley", "Granger", "Malfoy")

case class Person(firstNames: String, lastNames: String)

def makePeople(firstNames: Seq[String], lastNames: Seq[String]) = ???

val people = makePeople(firstNames, lastNames)
```

---

```
import scala.annotation.tailrec

def makePeople(firstNames: Seq[String], lastNames: Seq[String]): Seq[Person] = {
  @tailrec
  def helper(firstNames: Seq[String], lastNames: Seq[String],
             people: Vector[Person]): Seq[Person] = {
    firstNames match {
      case Seq() => people
      case _ =>
        val newPerson = Person(firstNames.head, lastNames.head)
        helper(firstNames.tail, lastNames.tail, people :+ newPerson)
    }
  }

  helper(firstNames, lastNames, Vector[Person]())
}

val people = makePeople(firstNames, lastNames)
```

---

Tail Recursion

Intent to repeat a computation without using mutable state and without
overflowing the stack

---

Mutual Recursion

```
def isOdd(n: Long): Boolean = if (n == 0) false else isEven(n - 1)

def isEven(n: Long): Boolean = if (n == 0) true else isOdd(n - 1)

isOdd(100001)
```

--

```
import scala.util.control.TailCalls.TailRec
import scala.util.control.TailCalls.done
import scala.util.control.TailCalls.tailcall

def tailRecursiveIsOdd(n: Long): TailRec[Boolean] =
  if (n == 0) done(false) else tailcall(tailRecursiveIsEven(n - 1))

def tailRecursiveIsEven(n: Long): TailRec[Boolean] =
  if (n == 0) done(true) else tailcall(tailRecursiveIsOdd(n - 1))

tailRecursiveIsOdd(100001).result
```

---

Mutual Recursion

Intent to use mutually recursive functions to express algorithms, such as
walking tree-like data structures, recursive descent parsing and state machine
manipulations

---

Scenario: Calculate a total discount on a sequence of prices, where any price
twenty euros or over is discounted at ten percent, and any under twenty is full
price.

--

```
val prices = Vector(20.0, 4.5, 50.0, 15.75, 30.0, 3.5)

def calculateDiscount(prices: Seq[Double]): Double = ???

val discountPrices = calculatedDiscount(prices)
```

---

```
def calculateDiscount(prices: Seq[Double]): Double = {
  prices filter (price => price >= 20.0) map
    (price => price * 0.10) reduce
      ((total, price) => total + price)
}

val discountPrices = calculateDiscount(prices)
```

---

```
def calculateDiscount(prices: Seq[Double]): Double = {
  (prices filter (price => price >= 20.0) map
    (price => price * 0.10))sum
}

val discountPrices = calculateDiscount(prices)
```

---

Filter-Map-Reduce

Intent to manipulate a sequence (list, vector, ...) declaratively using filter,
map and reduce to produce a new one. It gives us a more declarative way to do
sequence manipulations.

---

Scenario: We are given a list of names and wish to get their initials.

---

```
val names = List("Viggo Mortensen", "Orlando Bloom", "Elijah Wood", "Ian McKellen", "Billy Boyd")

val initials = ???
```

--

```
val initials = names map (_.split(" ") map (_.toUpperCase) map (_.charAt(0)) mkString)
```

---

Scenario: We are given a list of videos. How can we populate the sum of the length
of the cat movies?

---

```
case class Video(title: String, video_type: String, length: Int)

val v1 = Video("Pianocat Plays Carnegie Hall", "cat", 300)
val v2 = Video("Paint Drying", "home-improvement", 600)
val v3 = Video("Lord of the Rings", "Fantasy Fiction", 500)
val v4 = Video("Fuzzy McMittens Live At the Apollo", "cat", 200)

val videos = Seq(v1, v2, v3, v4)

def catVideosSum(videos: Seq[Video]): Int = ???
```

---

```
def catVideosSum(videos: Seq[Video]): Int =
  videos
    .filter(video => video.video_type == "cat")
    .map(video => video.length)
    .sum

catVideosSum(videos)
```

---

```
val o1 = Some(42)
val o2 = Some(8)
for { v1 <- o1; v2 <- o2 } yield (v1 + v2)
```

--

```
val o3: Option[Int] = None
for { v1 <- o1; v3 <- o3 } yield (v1 + v3)
```

---

Chain of Operations

Intent to chain a sequence of computations together that allows us to work
cleanly with immutable data without storing lots of temporary results.

https://docs.scala-lang.org/overviews/collections/introduction.html

---

Scenario: We are given a list of original price. During different sales seasons,
we would like to calculate different discount percentage between 0 to 100.

---

```
def discount(percent: Double): (Double) => Double = {
  if (percent < 0.0 || percent > 100.0) (originalPrice: Double) => originalPrice
  else (originalPrice: Double) => originalPrice - (originalPrice * percent / 100)
}

discount(200)(100)

discount(50)(200)
```

--

```
val twentyFivePercentOff = discount(25)

Vector(100.0, 25.0, 50.0, 25.0) map twentyFivePercentOff sum

val fiftyPercentOff = discount(50)

Vector(100.0, 25.0, 50.0, 25.0) map fiftyPercentOff sum
```

---

Scenario: Different states tax people differently. For example, New York tax
people for 6.45%, Pennsylvania state tax is 4.5%, etc.

---

```
def taxForState(amount: Double, state: Symbol): Double = state match {
  case ('NY) => amount * 0.0645
  case ('PA) => amount * 0.045
}

val nyTax = taxForState(_: Double, 'NY)
val paTax = taxForState(_: Double, 'PA)

nyTax(100)
paTax(20)
```

---

Function Builder (Higher-Order functions)

Intent to create a function that itself create functions, allowing us to
synthesize behaviors on the fly

---

Referential Transparency?

--

An expression always evaluates to the same result in any context.

--

Pure Functions?

--

Pure functions always return the same value for given arguments without side effects.

---

Scenario: We are given a key and we need to find the value of the key from a map.

```
val maps = Map(42 -> "foo", 12 -> "bar", 1 -> "baz")

def lookup(key: Int) = ???
```

--

```
def lookup(key: Int): Option[String] = maps.get(key)

lookup(12)
lookup(3)
```

---

```
def expensiveLookup(id: Int) = {
  Thread.sleep(1000)
  print(s"Doing expensive lookup for $id")
  maps.get(id)
}
```

---

```
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
```

---

Memoization

Intent to cache the results of a pure function call to avoid performing the same
computation more than once

---

What are the difference between val, lazy val and def?

--

```
// Strict immutable value, it is evaluated once when defined
val firstname: String = "Kason"

// Lazy immutable value, it is evaluated when called and is never evaluated more than once
lazy val lastname = "Chan"

// Immutable parameterless function, it is evaluated every time when called
def fullname = s"$firstname $lastname"
```

---

Scenario: We are given a sequence, how can we get the first nth element from it?

---

Built-in support for Lazy Sequence in Stream library

```
val integers = Stream.from(0)
```

--

```
val someIntegers = integers take 5

someIntegers foreach println
```

---

```
import import scala.util.Random

val generate = new Random()

val randoms = Stream.continually(generate.nextInt)

val someRandoms = randoms take 5

someRandoms foreach println
```

---

Scenario: We are given a lazy sequence, how can we get the first element and all
the elements except the first one?

---

```
val infStream = "foo" #:: "bar" #:: Stream[String]()

infStream.head

infStream.tail
```

---

```
def getPage(page: Int) = {
 page match {
   case 1 => Some("Star Wars")
   case 2 => Some("Lord of the Rings")
   case 3 => Some("Harry Potter")
   case _ => None
 }
}

def pagedSequence(pageNumber: Int): Stream[String] = {
 getPage(pageNumber) match {
   case Some(page: String) => page #:: pagedSequence(pageNumber + 1)
   case None => Stream.Empty
 }
}
```

---

```
pagedSequence(1) take 2

pagedSequence(1) take 2 force

pagedSequence(1) force
```

---

Lazy Sequence

Intent to create a sequence whose members are computed only when needed and this allows us to easily stream results
from a computation and to work with infinitely long sequences.

---

References

- Functional Programming Patterns in Scala and Clojure by Michael Bevilacqua-Linn

---

| Thank you for coming!

| Q&A/Comments?/Suggestions?

--- 

This presentation is built with [REPLesent](https://github.com/marconilanna/REPLesent).

To run the slides, first create the follow alias:

```
alias REPLesent='scala -Dscala.color -language:_ -nowarn -i REPLesent.scala'
```

Open the REPL and enter the statements below to start the presentation:

```
$ REPLesent
Welcome to Scala 2.12.3 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_65).
Type in expressions for evaluation. Or try :help.

scala> val replesent = REPLesent(intp=$intp)
replesent: REPLesent = REPLesent(0,0,scalameetup7.txt,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@38d308e7)

scala> import replesent._
import replesent._

scala> f
```
