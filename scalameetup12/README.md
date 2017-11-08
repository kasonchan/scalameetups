# Testing in Scala Basics Tour

| Testing in Scala Basics Tour

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

Testing in Scala Basics Tour

- Simple Build Tool (SBT)
- ScalaTest
- Specs2
- Mocking
- ScalaCheck

---

Directories in SBT

```
scalameetup12
├── build.sbt
├── project
│   ├── build.properties # This file is mainly used to specify the version of sbt
│   └── plugins.sbt # This file is mainly used to specify the plugins used
├── src # This directory is where we code our main scala code
│   ├── main
│   │   ├── resources
│   │   └── scala
│   └── test # This directory is where we put our tests
│       ├── resources
│       └── scala
└── target # This directory is for the built generated java bytecodes and classes
```

---

SBT Basic Tasks

```
$ sbt tasks

This is a list of tasks defined for the current project.
It does not list the scopes the tasks are defined in; use the 'inspect' command for that.
Tasks produce values.  Use the 'show' command to run the task and print the resulting value.

  bgRun            Start an application's default main class as a background job
  bgRunMain        Start a provided main class as a background job
  clean            Deletes files produced by the build, such as generated sources, compiled classes, and task caches.
  compile          Compiles sources.
  console          Starts the Scala interpreter with the project classes on the classpath.
  consoleProject   Starts the Scala interpreter with the sbt and the build definition on the classpath and useful imports.
  consoleQuick     Starts the Scala interpreter with the project dependencies on the classpath.
  copyResources    Copies resources to the output directory.
  doc              Generates API documentation.
  package          Produces the main artifact, such as a binary jar.  This is typically an alias for the task that actually does the packaging.
  packageBin       Produces a main artifact, such as a binary jar.
  packageDoc       Produces a documentation artifact, such as a jar containing API documentation.
  packageSrc       Produces a source artifact, such as a jar containing sources and resources.
  publish          Publishes artifacts to a repository.
  publishLocal     Publishes artifacts to the local Ivy repository.
  publishM2        Publishes artifacts to the local Maven repository.
  run              Runs a main class, passing along arguments provided on the command line.
  runMain          Runs the main class selected by the first argument, passing the remaining arguments to the main method.
  test             Executes all tests.
  testOnly         Executes the tests provided as arguments or all tests if no arguments are provided.
  testQuick        Executes the tests that either failed before, were not run or whose transitive dependencies changed, among those provided as arguments.
  update           Resolves and optionally retrieves dependencies, producing a report.

More tasks may be viewed by increasing verbosity.  See 'help tasks'
```

---

Show History in SBT

```
$ sbt
sbt:scalameetup12> !
History commands:
   !!    Execute the last command again
   !:    Show all previous commands
   !:n    Show the last n commands
   !n    Execute the command with index n, as shown by the !: command
   !-n    Execute the nth command before this one
   !string    Execute the most recent command starting with 'string'
   !?string    Execute the most recent command containing 'string'
```

---

[ScalaTest](http://www.scalatest.org/)

- Popular testing framework created by Bill Venners
- Extensive Behavior-Driven Development (BDD) suite with numerous built-in specs
- Integrated with classic testing frameworks like JUnit and TestNG
- Provide two assertion dialects - `Matchers - should` and `MustMatchers`

---

[ScalaTest](http://www.scalatest.org/)

```
libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)
```

---

Matchers

- Simple matchers

```
val list = 2 :: 3 :: 4 :: Nil
list.size should be(3)
```

---

Scala Regular Expressions

- http://www.scala-lang.org/api/2.12.3/scala/util/matching/Regex.html
- https://www.tutorialspoint.com/scala/scala_regular_expressions.htm

---

Matchers

- String matchers

```
val string = """This is a scala meetup about Testing in Scala Basics Tour."""
string should startWith("This is")
string should endWith("Basics Tour.")
string should include("about Testing")

string should startWith regex ("This.is+")
string should endWith regex ("T.{2}r.")
string should not include regex("flames?")

string should fullyMatch regex ("""This(.|\n|\S)*Tour.""")
```

---

Matchers

- Relational operator matchers

```
val number = 7
number should be(7)
number should equal(7)
number should not equal (12)
number should be > (3)
number should be < (3)
```

---

Matchers

- Floating point matchers

```
(0.9 - 0.8) should be(0.1 +- .01)
```

---

Matchers

- Reference matchers

```
case class Person(first: String, last: String)

val me = Person("kason", "chan")
val you = me

me should be theSameInstanceAs (you)

val he = Person("Tomas", "Higgens")
me should not be theSameInstanceAs(he)
```

---

Matchers

- Collection matchers

```
val list = 2 :: 3 :: 4 :: Nil
list.size should be(3)
list should have size (3)
list should have length (3)
list should contain(4)
list should not contain (5)
```

--

```
val maps = Map(1 -> "One", 2 -> "Two", 3 -> "Three")
maps should contain key (1)
maps should contain value ("Two")
maps should not contain key(7)
```

---

Matchers

- Compound matchers

```
val list = 2 :: 3 :: 4 :: Nil
list should ((contain(3)) and not(contain(1)))
list should ((have length (3)) or have length (2))
list should (not be (null) and contain(2))
```

---

Matchers

- Property matchers

```
case class Person(first: String, last: String, number: Int)

val me = Person("kason", "chan")

me should have(
  'first ("kason"),
  'last ("chan"),
  'number (12)
)
```

---

Matchers

- Exception Handling

```
And("9 / 0 should throw an java.lang.ArithmeticException")
  intercept[java.lang.ArithmeticException] {
    9 / 0
}
```

---

Informers and GivenWhenThen

```
describe("Mutable Set") {
  it("should allow an element to be added") {
    Given("an empty mutable Set")
    val testingFrameworks = mutable.Set.empty[String]

    When("an element is added")
    testingFrameworks += "ScalaTest"

    Then("the Set should have size 1")
    testingFrameworks.isEmpty shouldBe false
    testingFrameworks.size should equal(1)
    testingFrameworks.size should be(1)
    testingFrameworks should contain("ScalaTest")

    info("That's all for this mutable Set")
  }
}
```

---

Pending test

```
describe("Pending tests") {
  t("This test is pending") { pending }
}
```

---

Ignore test

```
describe("Ignore tests") {
  ignore("This test is ignored") {
    // Ignored tests
  }
}
```

---

Tagging

```
describe("Tagged Tests") {
  it("can be tagged with Tags", Tag("tagged")) {}
}
```

---

Run the tests

- Run all tests
```
sbt:scalameetup12> test
```

- Specify the test class name to run only
```
sbt:scalameetup12> testOnly FunSpecMustTestSuite
sbt:scalameetup12> testOnly FunSpecShouldTestSuite
```

- Specify a tag to include
```
sbt:scalameetup12> testOnly FunSpecShouldTestSuite -- -n tagged
```

- Specify a tag to exclude
```
sbt:scalameetup12> testOnly FunSpecShouldTestSuite -- -l tagged
```

---

References

- Testing in Scala by Daniel Hinojosa
- http://www.scalatest.org/user_guide/using_scalatest_with_sbt
- http://www.scalatest.org/user_guide/using_the_runner#filtering

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

scala> val replesent = REPLesent(intp=$intp,source="~/scalameetups/scalameetup12/README.md")
replesent: REPLesent = REPLesent(0,0,~/scalameetups/scalameetup12/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@32e9c3af)

scala> import replesent._
import replesent._

scala> f
```
