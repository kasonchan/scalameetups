## Strict vs lazy vs def variables, and values
```scala
// Mutable variable
var number = 1 
// Strict immutable value, it is evaluated once when defined
val firstname = "Kason" 
// Lazy immutable value, it is evaluated when called and is never evaluated more than once
lazy val lastname = "Chan" 
// Immutable parameterless method, it is evaluated every time when called
def email = "kason.chan@workday.com" 
```
- **Type inference**, Scala's ability to figure out the types that you leave off. 

## Functions, Literals and Anonymous Functions
```scala
def isGreater(x: Int, y: Int): Boolean = x > y

def max(x: Double, y: Double): Double = {
  if (x > y) x
  else y
}
```
- Function definitions start with the keyword `def`.
- `isGreater` and `max` are the function names.
- A list of parameters in parentheses separated by commoas is the parameters.
Each parameters need to have type specified `: Int` and `: Double`.
- The result types of functions are followed by the parameters `: Boolean` and `: Double`.
- For short functions, we can put them in one line. 
For longer functions, we put the function body within the curly braces.

Literals are notational sugar for representing values of some types the language
considers particularly important. 
Scala has integer literals, character literals, string literals, etc. 
Scala treats functions as first class values representable in source code by 
function literals. These function values inhabit a special function type. 
For example:
- `10` is an integer literal representing a value in Int type.
- `'a'` is a character literal representing a value in Char type.
- `"String"` is a string literal representing a value in String type.
- `(x: Int) => x + 2` is a function literal representing a value in `Int => Int`
function type.

Function literals are anonymous (functions): they don't have a name by default, 
but we can give them a name by binding them to a variable.
```scala
val plusOne = (x: Int) => x + 1
```

We can also define a function to return a function (algorithm). 
```scala
// echo: echo[](val prefix: String) => String => String
def echo(prefix: String) = (s: String) => {
  prefix + " " + s + "!"
}

// echoHello: String => String
val echoHello: String => String = echo("Hello")

// Hello Scala!
echoHello("Scala") 
```

We can use this to encapsulate one or more functions behind a function, 
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
```
`f` string interpolator allow us to attach printf-style formatting instructions
to embedded expressions. 
```
"%07d".format(123) // 0000123
f"${123}%17d" //   123
f"${123}%-5d" // 123  
f"${math.Pi}%.5f" // 3.14167
f"${0.45}%.2f" // 0.45
f"${0.45}%.1f" // 0.5

val version = 2.13
val name = "Scala"
// 2.13 will be the last version of the Scala 2 series!
f"$version%2.2f will be the last version of the $name%s 2 series!" 
```
