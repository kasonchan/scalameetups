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
- Provide two assertion dialects - `MustMatchers` and `ShouldMatchers`

---

References

- Testing in Scala by Daniel Hinojosa

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
No definition found.
Search the web for "| functional patterns (in scala)" »
