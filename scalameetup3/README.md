# Scala 

Scala is a modern **multi-paradigm** programming language **Object-Oriented** and 
**Functional**. Every value is an object in Scala. Classes and traits describe 
the types and behaviors of objects. Subclassing and a flexible mixin-based 
composition mechanism extends classes as a clean replacement for multiple inheritance. 

Every function is a value. Scala provides a lightweight syntax for defining 
anonymous functions, it supports higher-order functions, it allows functions to 
be nested, and supports currying. Scala’s case classes and its built-in support 
for pattern matching model algebraic types used in many functional programming 
languages. 

Singleton objects provide a convenient way to group functions that aren’t 
members of a class. Scala is statically typed and is extensible.

## Strict vs lazy vs def variables, and values
```scala
// Mutable variable
var number = 1 

number = 2

// Strict immutable value, it is evaluated once when defined
val firstname: String = "Kason" 

// Lazy immutable value, it is evaluated when called and is never evaluated more than once
lazy val lastname = "Chan" 

// Immutable parameterless function, it is evaluated every time when called
def email = "kason.chan@workday.com" 

// Mr. Kason Chan
val fullname = { 
  val fn = firstname + " " + lastname 
  "Mr. " + " " + fn
}
```
- **Expressions** are computable statements.
- **Type inference**, Scala's ability to figure out the types that you leave off. 
- We can explicitly state the type `: String`.
- We can group a number of expressions with a block `{}`.
- The last expression in a block is the result of the overall block.

*****

## Functions, Literals, Anonymous Functions and Methods
```scala
def isGreater(x: Int, y: Int): Boolean = x > y

def max(x: Double, y: Double): Double = {
  if (x > y) x
  else y
}
```
- Method definitions start with the keyword `def`.
- `isGreater` and `max` are the method names.
- A list of parameters in parentheses separated by commoas is the parameters.
Each parameters need to have type specified `: Int` and `: Double`.
- The result types of methods are followed by the parameters `: Boolean` and `: Double`.
- For short methods, we can put them in one line. 
For longer ones, we put the function body within the curly braces.

Literals are notational sugar for representing values of some types the language
considers particularly important. 
Scala has integer literals, character literals, string literals, etc. 
Scala treats functions as first class values representable in source code by 
function literals. These function values inhabit a special function type. 
For example:
- `10` is an integer literal representing a value in Int type.
- `'a'` is a character literal representing a value in Char type.
- `"String"` is a string literal representing a value in String type.
- `'symbol` is a symbol literal representing a value in Symbol type. We can get
the name of the symbol by calling the function `.name`.
- `(x: Int) => x + 2` is a function literal representing a value in `Int => Int`
function type.

Function literals are anonymous (functions): they don't have a name by default, 
but we can give them a name by binding them to a variable.
```scala
val number = () => 1

val plusOne = (x: Int) => x + 1
```
- Functions can take no parameters.

We can also define a method to return a function (algorithm). 
```scala
// echo: echo[](val prefix: String) => String => String
def echo(prefix: String) = (s: String) => {
  prefix + " " + s + "!"
}

// Partially applied function with return type specified
// echoHello: String => String
val echoHello: String => String = echo("Hello")

// Hello Scala!
echoHello("Scala") 
```

Above code snippet is equivalent to the following curried function.
```scala
// Curried function
// echo: echo[](val prefix: String)(val s: String) => String
def echo(prefix: String)(s: String): String = {
  prefix + " " + s + "!"
}

// Partially applied function with return type specified
// echoHello: String => String = $Lambda$1220/1056129247@6d8a3128
val echoHello: String => String = echo("Hello")

// Partially applied function using wildcard
// echoOla: String => String = $Lambda$1221/1460756112@5a225f8d
val echoOla = echo("Ola")_

// Hello Scala!
echoHello("Scala") 

// Ola Scala!
echoOla("Scala")
```

We can use this to encapsulate one or more functions behind a method, 
and is similar in that effect to the Factory and Strategy patterns.
```scala
// greeting: greeting[](val language: String) => String => String
def greeting(language: String) = (name: String) => {
    language match {
        case "english" => s"Hello $name!"
        case "portuguese" => s"Ola $name!"
    }
}

// hello: String => String
val hello = greeting("english")

// ola: String => String
val ola = greeting("portuguese")

// Hello Scala!
hello("Scala")

// Ola Scala!
ola("Scala")
```

### Higher-Order Functions

```scala
def apply(f: Int => String, v: Int): String = f(v)

def addCurly(i: Int): String = "{" + i + "}"

// {12}
apply(addCurly, 12)
```
- `f: Int => String` annotates paramete `f` takes a anonymous function with parameter
`Int` and returns `String`.
- Higher-order function can have one of three forms:
  - One or more of its parameters is a function, and it returns some value.
  - It returns a function, but none of its parameters is a function.
  - Both of the above: One or more of its parameters is a function, and it returns a function.
- Some common higher-order functions are `map`, `flatMap` and `filter`.

### Currying
```scala
// Curried function
def modN(n: Int)(x: Int): Boolean = (x % n) == 0

// Partially applied function
val mod2: (Int) => Boolean = modN(2)

// Vector(2, 4, 6, 8, 10)
(1 to 10).filter(modN(2))

// Vector(2, 4, 6, 8, 10)
(1 to 10).filter(mod2)

// Vector(3, 6, 9)
(1 to 10).filter(modN(3))
```
- When a method is called with a fewer number of parameter lists, then this will 
yield a function taking the missing parameter lists as its argument.

### Special function call forms
We can indicate the last parameter to a function as repeated by placing an
asterisk `*` after the type of the parameter.
```scala
def echo(args: String*): Unit = for (arg <- args) println(arg)
```

We can mix position of the parameters by using named arguments. We can define
default parameter values by adding the values at the function definition.
```scala
// Default parameter values speed = 10.5 and time = 2
def distance(speed: Double = 10.5, time: Double = 2): Double = speed * time

distance(time = 5.0, speed = 10.0) // 50

distance() // 21.0
```

By-name parameters are only evaluated when used. They are in contrast to 
by-value parameters which are evaluated when defined. We can make a parameter 
called by-name by prepending `=>` to its type.
```scala
val number = {
  println("This is the number 5.")
  5
}

// calculate: calculate[](val input: => Int) => Int
def calculate(input: => Int) = input * 37

calculate(number)
```
- Delay evaluation of a parameter until it is used can help performance if the 
parameter is computationally intensive to evaluate or a longer-running block of 
code such as fetching a URL.

***** 

## Basic Types

Basic Type | Range | Literal Examples
---------- | ----- | -------
`Byte`     | 8-bit signed two's complement integer (-2<sup>7</sup> to 2<sup>7</sup> - 1, inclusive) | `val byte: Byte = 38`
`Short`    | 16-bit signed two's complement integer (-2<sup>15</sup> to 2<sup>15</sup> - 1, inclusive) | `val short: Short = 367`
`Int`      | 32-bit signed two's complement integer (-2<sup>31</sup> to 2<sup>31</sup> - 1, inclusive) | `val int: Int = 3`
`Long`     | 64-bit signed two's complement integer (-2<sup>63</sup> to 2<sup>63</sup> - 1, inclusive) | `val long: Long = 35l`, `val long: Long = 35L`
`Char`     | 16-bit unsigned Unicode character (0 to 2<sup>16</sup> - 1, inclusive) | `val char: Char = 'a'`
`String`   | a sequence of `Char`s | `val string: String = "Hello World!"`
`Float`    | 32-bit IEEE 754 double-precision float | `val float: Float = 3.5f`, `val float: Float = 3.5F`
`Double`   | 64-bit IEEE 754 double-precision float | `val double: Double = 3.5`
`Boolean`  | true or false | `val boolean: Boolean = true`

![unifiedtypes](http://docs.scala-lang.org/tutorials/tour/unified-types-diagram.svg)
- `Any` is the supertype of all types, also called the top type. 
- `Any` defines certain universal methods such as `equals`, `hashCode`, and `toString`. 
- `Any` has two direct subclasses `AnyVal` and `AnyRef`.
-  `AnyVal` represents value types. There are nine predefined value types and 
they are non-nullable: `Double`, `Float`, `Long`, `Int`, `Short`, `Byte`, `Char`, 
`Unit`, and `Boolean`. 
- `Unit` is a value type which carries no meaningful information. 
- There is exactly one instance of Unit which can be declared literally like so: `()`. 
- All functions must return something so sometimes Unit is a useful return type.
- `AnyRef` represents reference types. All non-value types are defined as reference types. 
- Every user-defined type in Scala is a subtype of `AnyRef`. 

![typecasting](http://docs.scala-lang.org/tutorials/tour/type-casting-diagram.svg)
```scala
val x: Long = 987654321
val y: Float = x  // 9.8765434E8 (note that some precision is lost in this case)

val face: Char = '☺'
val number: Int = face  // 9786

val x: Long = 987654321
val y: Float = x  // 9.8765434E8
val z: Long = y  // Does not conform
```
- Casting is unidirectional.

*****

## String Interpolations
Scala uses `s` string interpolator to process the literal. The `s` interpolator 
evaluate each embedded expression, invoke `toString` on each result and replace
the embedded expression in the literal with those results. We can put any 
expression after a `$` to be evaluated. 
```scala
val name = "Scala"
s"Hello $name!" // Hello Scala!
```

`raw` string interpolator behaves like `s` except it does not recognize 
character literal escape sequences.
```scala
raw"No\\\\escape!" // No\\\\escape!

"""No\\\\escape!""" // No\\\\escape!

// Welcome to Scala Meetup.
//    Shout "QUESTION" for help.
"""Welcome to Scala Meetup.
   Shout "QUESTION" for help."""

// Welcome to Scala Meetup.
// Shout "QUESTION" for help.
"""|Welcome to Scala Meetup.
   |Shout "QUESTION" for help.""".stripMargin
```
- `|` at the beginning of each line and `.stripMargin` removes all leading and trailing spaces.

`f` string interpolator allow us to attach printf-style formatting instructions
to embedded expressions. 
```
"%07d".format(123) // 0000123
f"${123}%5d" //   123
f"${123}%-5d" // 123  
f"${math.Pi}%.5f" // 3.14167
f"${0.45}%.2f" // 0.45
f"${0.45}%.1f" // 0.5

val version = 2.13
val name = "Scala"
// 2.13 will be the last version of the Scala 2 series!
f"$version%2.2f will be the last version of the $name%s 2 series!" 
```

*****

## Regex Expressions

```scala
val datePattern = """([0-9]{4}-[0-9]{2}-[0-9]{2})""".r

def get(s:String) = s match {
  case datePattern(d) => s"Date: $d"
  case _ => s"Could not extract the date!"
}

// Date: 2017-06-30
get("2017-06-30")

// Could not extract the date!
get("Scala")
```
- We can define regex expression by adding `.r` to the string literal.

*****

## Basic Operations

- Operators are functions, for example `1 + 2` in Scala invokes `1.+(2), 
`-2.0` invokes `(2.0).unary_-`.
- Arithmetic operations, for example `+`, `-`, `*`, `/`, `%`.
- Relational operations, for example greater than `>`,  smaller than `<`, 
smaller or equal to `<=`, greater or equal to `>=`, unary `!` invert a `Boolean` value.
- Logical operations, for example logical-and (`&`, `&&`), logical-or(`|`, `||`).
- Bitwise operations, for example bitwise-and `&`, bitwise-or `|`, bitwise-xor `^`, 
bitwise complement operator `~`, shift right `>>`, unsigned shift right `>>>`, 
shift left `<<`.
- Equality `==` or inverse `!=`

| Operator precedence |
| - |
| (all other special characters) |
| * / % |
| + - |
| : |
| = ! |
| < > |
| & |
| ^ |
| &#124; |
| (all letters) |
| (all assignment operators) |

*****

## Control Structures
Scala provides a handful of built-in control structures. They are `if...else`, 
`while`, `do-while`, `for`, `try`, `match` and function calls.

### If expressions
```scala
val filename = if (args.nonEmpty) args(0) else "default.txt"
```

### While loops
```scala
var line = ""
do {
  line = scala.io.StdIn.readLine
  println("Read: " + line)
} while (line != "")


// Does not work!
// [error]  found   : Unit
// [error]  required: Boolean
// [error]     while (line = scala.io.StdIn.readLine != "") println("Read: " + line)
var line = ""
while (line = scala.io.StdIn.readLine != "") println("Read: " + line)
```
The type of the result is `Unit`. It is called the unit value and is written `()`.
The while loop's condition will never be false because it will never be `""`. 
Scala's `Unit` is different from Java's `void`.

### For expressions
We can iterate through collections, filter, nested iteration, mid-stream variable
bindings, produce a new collection with for expressions.
```scala
for (i <- 1 to 4) println(i) // 1 2 3 4
for (i <- 1 until 4) println(i) // 1 2 3 

val files = new java.io.File(".").listFiles

def fileLines(file: java.io.File) =
  scala.io.Source.fromFile(file).getLines().toList

def grep(pattern: String) =
  for {
    file <- files
    if file.getName.endsWith(".sbt")
    line <- fileLines(file)
    trimmed = line.trim
    if trimmed.matches(pattern)
  } println(trimmed)

grep(".*:=.*")

def sbtFiles =
  for {
    file <- files
    if file.getName.endsWith(".sbt")
  } yield file

scalaFiles.foreach(println)
```

### Exception handling with try expressions
We can use `try-catch-finally` expressions to catch exceptions, yield a value
```scala
import java.io.{FileNotFoundException, FileReader}
import java.net.{MalformedURLException, URL}

import scala.util.{Failure, Success, Try}

try {
  val f = new FileReader("notExisted.txt")
  println("Done")
} catch {
  case ex: FileNotFoundException => println(ex.getMessage)
  case ex: Exception             => println(ex.getMessage)
}

// try {...} catch {...} finally {...}

// yield a new URL value
def urlFor(path: String) = try {
  new URL(path)
} catch {
  case e: MalformedURLException => new URL("https://www.scala-lang.org/")
}

def urlForTry(path: String): Option[URL] = Try {
  new URL(path)
} match {
  case Success(successMsg) => Some(successMsg)
  case Failure(_) => None
}

urlForTry("http://www.google.com") // Some(http://www.google.com)
urlForTry("xyc") // None
```

### Match expressions
We can select from a number of alternatives using Scala's `match` expression.
We can match expression by using arbitrary patterns.
A few important differences from Java's switch statement. Scala can match any
kind of constant but not just integer type, enum and string constants from Java.
There is no `break`s at the end of each alternatives. Scala's `match` results in 
a value. There is no fall through from one alternative to the next.

```scala
case object Nothing 

val s = 5 match {
  case 5 => "Five"
  case Nothing => "Nothing"
  case 2.5 => "Two point five"
  case _ => "Huh?"
}

println(s)
```
Checkout the essential-scala for more information.

*****

## Classes, Objects, Case Classes, Case Objects and Traits

```scala
class Greeter(name: String) {
  def greet(message: String): Unit =
    println(s"$name: message")
}

val greeter = new Greeter("Scala")

// Scala: Hello World!
greeter.greet("Hello World!")
```
- Return type of `greet` is `Unit`.
- All Scala expressions must have some value, Scala has a singleton value of 
type `Unit`, written `()`. It carries no information.

```scala
case class Programmer(firstname: String = "Martin",
                  lastname: String = "Odersky",
                  programmingLanguage: String)

// Person(Martin,Odersky,Scala)
val person = Programmer(programmingLanguage = "Scala")

val martin = Programmer(programmingLanguage = "Scala")

// true
person == martin

// This is not recommended! It is an anti-pattern!
case class Tool(var name: String)

// Tool(Akka)
var tool = Tool("Akka")

tool.name = "Play"

// Tool(Play)
tool

// Tool(Play)
val play = tool.copy()
```
- `case class` is a special type of classes. We do not need `new` keyword to 
instantiate it.
- By default, `case class` is immutable, it has default implementations of 
`equals`, `hashCode` and prettified `toString` and serialization.
- Case classes are compared by structure and not by reference. Even though 
`person` and `martin` refer to different objects, the value of each object is equal.
- We can copy case classes. 
- We can also do named argument `programmingLanguage = "Scala` and default parameter 
`firstname: String = "Martin"`, `lastname: String = "Odersky"` just like in methods.

```scala
object Obj

// obj: Obj.type = Obj$@42b77ca9
val obj = Obj

// A$A61$A$A61$Obj$@6e20af2
println(obj)

case object NoParameterClass

// caseObj: NoParameterClass.type = NoParameterClass
val caseObj = NoParameterClass

// NoParameterClass
println(caseObj)
```
- `object` are singleton.
- `case object` are special type of classes without parameters. The primary use
case here is to pattern match.
- `case object` has default implementations of `equals`, `hashCode` and 
prettified `toString` and serialization but not `object`.
- We do not need multiple instances of an immutable type where the instances 
would all have identical values.

```scala
sealed trait WebsiteVisitor {
  def email: String = {
    this match {
      case User(id: Long, email: String) => email
      case Anonymous(id: Long) => ""
    }
  }
}

final case class User(id: Long, override val email: String) extends WebsiteVisitor

final case class Anonymous(id: Long) extends WebsiteVisitor

// scala@scala-lang.org
val scala = User(1L, "scala@scala-lang.org").getEmail

val anonymous = Anonymous(1L).getEmail
```
- `trait`s are types containing some fields and methods.
- We can combine multiple traits.
- When a trait is "sealed" all of its subclasses are declared within the same 
file and that makes the set of subclasses finite which allows certain compiler 
checks.

*****

## Collections

```scala
val array: Array[Int] = Array(1, 2, 3)

println(array.mkString("[", ",", "]"))

val list: List[Any] = List(
  "a string",
  732,  // an integer
  'c',  // a character
  true, // a boolean value
  () => "an anonymous function returning a string"
)

list.foreach(element => println(element))

val tuple = ("Scala", 2.12)

// Scala
tuple._1

// 2.12
tuple._2

// 2.12
tuple.swap._1

val xyz = Map("x" -> 24, "y" -> 25, "z" -> 26)

// 0
xyz.getOrElse("w", 0)
```
- Note that we use `()` instead of `[]` for Array in Scala.
- By default, all collections are homogeneous. We can type cast the collections to
the supertype to make them heterogeneous.
- `.swap` can only work on tuple2.
- All collections are immutable by default unless your explicitly import the mutable ones except `Array`.

![collections](http://docs.scala-lang.org/resources/images/collections.mutable.png)

*****

## References

- Programming in Scala Third Edition by Martin Odersky
- http://docs.scala-lang.org/tutorials/tour/tour-of-scala.html
- https://www.tutorialspoint.com/scala/scala_regular_expressions.htm
- http://underscore.io/training/courses/essential-scala/
- http://docs.scala-lang.org/overviews/collections/introduction.html
