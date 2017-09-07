# Scala Basics Tour - Classes, Objects and Traits

| Scala Basics Tour - Classes, Objects and Traits

---
This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

Thank you Ignacio Cavero for introducing it to me last week.

---
| Collections Recap

```
List('ironman, 'flash, 'spiderman, 'thor, 'superman, 'wonderwoman)
  .map(e => e.name.capitalize)
```
--

```
"hello world".toList
  .filter(_.isLetter)
  .groupBy(x => x)
  .map { y => y._1 -> y._2.size }
```
---
| Collections Recap

```
val zippedLists = (List(1,3,5), List(2,4,6)).zipped
val (x, y) = zippedLists.find(_._1 > 10).getOrElse(10)
println(s"Found $x")
```

Possibilities
1. Prints:
Found 10
2. Prints:
Found ()
3. Fails to compile.
4. Throws a runtime exception.
---
| Collections Recap

```
def sumSizes(collections: Iterable[Iterable[_]]): Int =
  collections.map(_.size).sum

println("1: " + sumSizes(List(Set(1, 2), List(3, 4))))
println("2: " + sumSizes(Set(List(1, 2), Set(3, 4))))
```

Possibilities
1. Prints:
  1: 4
  2: 4
2. Prints:
  1: 4
  2: 2
3. Prints:
  1: 2
  2: 4
4. Prints:
  1: 2
  2: 2
---
| Collections Recap

```
val xs = Seq(Seq("a", "b", "c"), Seq("d", "e", "f"),
               Seq("g", "h"), Seq("i", "j", "k"))
val ys = for (Seq(x, y, z) <- xs) yield x + y + z
val zs = xs map { case Seq(x, y, z) => x + y + z }
```

Possibilities
1. Evaluating both ys and zs throws a MatchError.
2. Both ys and zs evaluate to: Seq(abc, def, ijk)
3. Evaluating ys throws a MatchError, and zs evaluates to: Seq(abc, def, ijk)
4. ys evaluates to:
  Seq(abc, def, ijk)
  and evaluating zs throws a MatchError.
--

```
val xs = Seq(Seq("a", "b", "c"), Seq("d", "e", "f"),
               Seq("g", "h"), Seq("i", "j", "k"))
```
--

```
val ys = for (Seq(x, y, z) <- xs) yield x + y + z
```
--

```
val zs = xs map { case Seq(x, y, z) => x + y + z }
```
---
| Classes

- A class is blueprint for objects.
- Create objects from the class blueprint with keyword new.
- Do not do multiple inheritance.

```
class ClassPerson(var firstName: String = "Barry",
                  val lastName: String = "Allen",
                  gender: Char = 'M',
                  ability: String = "Speed") {
  def name: String = s"$firstName $lastName"

  def isMale: Boolean = gender match {
    case 'M' => true
    case _ => false
  }
}
```
--

```
val barry = new ClassPerson()
println(barry.name)
println(barry)
```
--

```
val bart = new ClassPerson()
println(barry == bart)
```
---
| Case Classes

- A case class is blueprint for objects.
- We create objects from the case class blueprint without keyword new.

```
case class CaseClassPerson(var firstName: String = "Peter",
                           lastName: String = "Parker",
                           gender: Char = 'M') {
  def name: String = s"$firstName $lastName"

  def isMale: Boolean = gender match {
    case 'M' => true
    case _ => false
  }
}
```
--

```
val ccp1 = CaseClassPerson()
println(ccp1)
val ccp2 = CaseClassPerson()
println(ccp2)
println(ccp1 == ccp2)
```
---
| Case Classes vs Normal Classes

- We can do pattern matching on it.
- You can construct instances of these classes without using the new keyword.
- All constructor arguments are accessible from outside using automatically
  generated accessor functions.
- The toString method is automatically redefined to print the name of the
  case class and all its arguments.
- The equals method is automatically redefined to compare two instances of the
  same case class structurally rather than by identity.
- The hashCode method is automatically redefined to use the hashCodes of
  constructor arguments.
---
| Abstract Classes

- abstract modifier signifies that class may have abstract members.

```
abstract class X {
  def content: Symbol
  def demo() = println("X")
}
```
--

- Abstract classes can not be instantiated.

```
val x = new X
```
--

```
class A extends X {
  override def content: Symbol = 'A
  override def demo() = println("A")
}
```
---
| Final modifier

- final modifier ensures that member cannot be overridden

```
class Class(final var cValue: Symbol = 'cValue) {
  final def value = 'finalAtClass
}
```
--

```
class OverrideClass(var cValue: Symbol = 'cValue) extends Class() {
  override def value = 'ValueAtCverrideClass
}
```
--

- final modifier ensures entire class not be subclassed

```
final abstract class FinalAbstract {
  def value = 'FinalAbstract
}
```
--

```
class FinalAbstractExtension extends FinalAbstract {
  override def value = 'FinalAbstractExtension
}
```
---
| Case Objects vs Objects

```
object Object { var x = 0 }
val o1 = Object
val o2 = Object
o1 match {
  case Object => "Object"
  case _ => "Unknown"
}
o2.x = 1
println(o1.x)
println(o2.x)
println(o1 == o2)
```
--

```
case object CaseObject { var x = 0 }
val co1 = CaseObject
val co2 = CaseObject
co1 match {
  case CaseObject => "Case Object"
  case _ => "Unknown"
}
co2.x = 1
println(co1.x)
println(co2.x)
println(co1 == co2)
```
---
| Traits

- Fundamental unit of code reuse of Scala.
- Encapsulates method and field definitions to be reused by mixing them into classes.
- Traits are not class inheritance, classes extends from traits can only
  inherit from one superclass.
- Linearization is a deterministic process that puts all traits in a
  linear inheritance hierarchy
--

- A minimal trait:

```
trait Color
```
---

| Traits as interfaces

```
trait Alarm {
  def trigger(): String
}
```
--

```
trait Notifier {
  val notificationMessage: String

  def printNotification(): Unit = {
    println(notificationMessage)
  }

  def clear()
}
```
--

```
class NotifierImplementation(val notificationMessage: String) extends Notifier {
  override def clear(): Unit = println("Cleared")
}
```
---
| Traits as classes

```
trait Beeper {
  def beep(times: Int): Unit = {
    (1 to times).foreach(i => println(s"Beep $i"))
  }
}
```

- We can instantiate Beeper and call its methods

```
val times = 10
val beeper = new Beeper {}
beeper.beep(times)
```
---
| Traits extending classes

```
abstract class Connector {
  def connect()
  def close()
}
```
--

```
trait ConnectorWithHelper extends Connector {
  def findDriver(): Unit = println("Find driver called.")
}
```
--

```
class PgSqlConnector extends ConnectorWithHelper {
  override def connect(): Unit = println("Connected")

  override def close(): Unit = println("Closed")
}
```
---
| Traits extending traits

```
trait Ping {
  def ping(): Unit = println("ping")
}

trait Pong {
  def pong(): Unit = println("pong")
}
```
--

```
trait PingPong extends Ping with Pong {
  def pingPong(): Unit = {
    ping()
    pong()
  }
}
```
---
| Clashing traits of same signatures and return types traits

```
trait FormalGreet {
  def hello: String
}

trait InformalGreet {
  def hello: String
}
```
--

```
class Greeter extends FormalGreet with InformalGreet {
  override def hello: String = "Good morning, Scala!"
}
```
---
| Clashing traits of same signatures and different return types traits

```
trait FormalGreet {
  def hello: String
  def getTime: String
}

trait InformalGreet {
  def hello: String
  def getTime: Int
}
```
--

```
class Greeter extends FormalGreet with InformalGreet {
  override def hello: String = "Good morning, Scala!"
}
```
---
| Multiple inheritance

```
trait A {
  def hello = "Hello from A"
}

trait B extends A {
  override def hello: String = "Hello from B"
}

trait C extends A {
  override def hello: String = "Hello from C"
}
```
--

```
trait D1 extends B with C

val diamond1 = new D1 {}
println(diamond1.hello)

trait D2 extends C with B

val diamond2 = new D2 {}
println(diamond2.hello)
```
---
```
abstract class A {
  val message: String
}
class B extends A {
  val message = "I'm an instance of class B"
}
trait C extends A {
  def loudMessage = message.toUpperCase()
}
class D extends B with C
```
--

```
val d = new D
println(d.message)
println(d.loudMessage)
```
---
```
import scala.collection.mutable.ArrayBuffer

abstract class IntQueue {
  def get(): Int

  def put(x: Int): Unit
}

class BasicIntQueue extends IntQueue {
  private val list = ArrayBuffer[Int]()

  def get(): Int = list.remove(0)

  def put(x: Int) = {
    list += x
  }
}
```
---
```
trait Incrementing extends IntQueue {
  abstract override def put(x: Int): Unit = super.put(x + 1)
}

trait Filter extends IntQueue {
  abstract override def put(x: Int): Unit = if (x >= 0) super.put(x)
}

trait Doubling extends IntQueue {
  abstract override def put(x: Int): Unit = super.put(2 - x)
}
```
---
```
val queue = new BasicIntQueue with Doubling with Incrementing

queue.put(1)
queue.put(0)
queue.put(-1)
```
--

```
println(queue.get())
println(queue.get())
println(queue.get())
```
---
| Modelling Data with Traits

- We express any data model in Scala in terms of logical ors and ands,
like in OOP is-a and has-a relationship.
- Logical ors and ands in Functional Programming (FP) are sum and product types
and together form Algebraic Data Types (ADT).
---
Product Type (has-a and)

- If A has a b (with type B) and a c (with type C), we write

```
case class A(b: B, c: C)
```

or

```
trait A {
  def b: B
  def c: C
}
```
---
| Sum Type (is-a or)

- If A is a B or C, we write

```
sealed trait A
final case class B() extends A
final case class C() extends A
```
---
| Algebraic Data Type

- A composite type, i.e., a type formed by combining other types
- Two common classes of algebraic types are
  - product types (i.e., tuples and records)
  - sum types, also called tagged or disjoint unions or variant types
- Code follows immediately from structure of the data
---
| Missing Pattern?

```
|          |      And      |    Or    |
|----------|---------------|----------|
| Is-a     |               | Sum Type |
| Has-a    |  Product Type |          |
```

- The “is-a and” pattern means that A is a B and C.
  - This pattern is in some ways the inverse of the sum type pattern,
    and we can implement it as below.
  - We use it to share implementation across several classes

```
trait B
trait C
trait A extends B with C
```
---
| Missing Pattern?

```
|          |      And      |    Or    |
|----------|:-------------:|:--------:|
| Is-a     |               | Sum Type |
| Has-a    |  Product Type |          |
```

- The “has-a or” patterns means that A has a B or C
  - We can say that A has a d of type D, where D is a B or C

```
trait A {
  def d: D
}
sealed trait D
final case class B() extends D
final case class C() extends D
```

- We can also implement "has-a or" as A is a D or E,
  and D has a B and E has a C.

```
sealed trait A
final case class D(b: B) extends A
final case class E(c: C) extends A
```
---
- A website visitor is:
  - logged in user; or
  - anonymous
- A logged in user has:
  - an ID; and
  - an email address
- An anonymous has:
  - an ID
---
```
sealed trait Visitor {
    id: Id
}
final case class Anonymous(id: Id)  extends Visitor
final case class User(id: Id, email: Email)  extends Visitor
```
---
- A calculation is a success or failure
- A success has an value.
- A failure has an error message.
---
```
sealed trait Calculation
final case class Success(value: Int) extends Calculation
final case class Failure(msg: String) extends Calculation
```
---
| Type Class Pattern

- A type class is a trait with at least one type variable.
- The type variables specify the concrete types
  the type class instances are defined for.
- Methods in the trait usually use the type variables.

```
trait ExampleTypeClass[A] {
  def doSomething(in: A): Foo
}
```
---
| References

- Scala Puzzlers
- Programming in Scala
- Essential Scala
- Scala Design Patterns

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
