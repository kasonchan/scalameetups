# Testing in Scala Basics Tour Continued

| Testing in Scala Basics Tour Continued

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
- String matchers i.e. `startWith`, `endWith`, `include`, `regex`, `fullyMatch`
- Relational operator matchers i.e. `=`, `be`, `equal`, `>`, `<`, ...
- Floating point matchers i.e. `+-`
- Reference matchers i.e. `theSameInstanceAs`
- Collection matchers i.e. `be`, `size`, `length`, `contain`, `key`, `value`
- Compound matchers i.e. `(... and ...)`, `(... or ...)`
- Property matchers i.e. `have('parameter (...) ...)`
- Exception Handling i.e. `intercept[Exception] { ... }`

---

Informers 

```
info(...)
```

---

GivenWhenThen

```
describe(...) {
  it(...) {  
    Given(...)
    When(...)
    Then(...)
  }
}
```

---

Pending

```
describe(...) {
  it(...) { pending }
}
```

---

Ignore

```
describe(...) {
  ignore(...)
}
``` 

---

Tagging

```
describe("Tagged Tests") {
  it("can be tagged with Tags", Tag("tagged")) {
    ???
  }
}
```

---

Styles

- [FunSpec](http://www.scalatest.org/getting_started_with_fun_spec)

For teams coming from Ruby's RSpec tool, FunSpec will feel very familiar; 
More generally, for any team that prefers BDD, FunSpec's nesting and gentle 
guide to structuring text (with describe and it) provides an excellent 
general-purpose choice for writing specification-style tests.

---

Styles

- [FlatSpec](http://www.scalatest.org/getting_started_with_flat_spec)

A good first step for teams wishing to move from xUnit to BDD, FlatSpec's 
structure is flat like xUnit, so simple and familiar, but the test names must 
be written in a specification style: "X should Y," "A must B," etc.

---

Styles

- [FeatureSpec](http://www.scalatest.org/getting_started_with_feature_spec)

Trait FeatureSpec is primarily intended for acceptance testing, including 
facilitating the process of programmers working alongside non-programmers to 
define the acceptance requirements.

---

Styles

- [At a glance](http://www.scalatest.org/at_a_glance/FlatSpec)

---

[Using ScalaTest with SBT](http://www.scalatest.org/user_guide/using_scalatest_with_sbt)

To Generate HTML page, add the following to `build.sbt`
```
libraryDependencies ++= Seq(
  "org.pegdown" % "pegdown" % "1.6.0" % "test"
)

testOptions in Test ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
)
```

---

[Using ScalaTest with SBT](http://www.scalatest.org/user_guide/using_scalatest_with_sbt)

- Run all tests
```
sbt:scalameetup13> test
```

- Specify the test class name to run only
```
sbt:scalameetup13> testOnly FunSpecMustTestSuite
sbt:scalameetup13> testOnly FunSpecShouldTestSuite
```

- Specify a tag to include
```
sbt:scalameetup13> testOnly FunSpecShouldTestSuite -- -n tagged
```

- Specify a tag to exclude
```
sbt:scalameetup13> testOnly FunSpecShouldTestSuite -- -l tagged
```

---

| Any Questions? Comments? Suggestions?

---

[Specs2](https://etorreborre.github.io/specs2/)

---

References

- Testing in Scala by Daniel Hinojosa
- http://www.scalatest.org/user_guide/using_scalatest_with_sbt
- http://www.scalatest.org/user_guide/using_the_runner#filtering
- http://www.scalatest.org/user_guide/selecting_a_style
- http://www.scalatest.org/at_a_glance/FlatSpec

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
