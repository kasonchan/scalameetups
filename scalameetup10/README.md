# Build a REST API with Akka HTTP

Building a REST API with Akka HTTP

---
This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---
Akka HTTP

- http://akka.io/docs/
- Modern, fast, asynchronous, streaming-first HTTP server and client.
- Implements a full server- and client-side HTTP stack on top of
  akka-actor and \yakka-stream\s.
- General toolkit for providing and consuming HTTP-based services.
---
Akka HTTP

- akka-http
  - Higher-level functionality, like (un)marshalling, (de)compression as well as
    a powerful DSL for defining HTTP-based APIs on the server-side, this is the
    recommended way to write HTTP servers with Akka HTTP. Details can be found
    in the section High-level Server-Side API
- akka-http-core
  - A complete, mostly low-level, server- and client-side implementation of HTTP
    (incl. WebSockets) Details can be found in sections Low-Level Server-Side
    API and Consuming HTTP-based Services (Client-Side) akka-http-testkit
    A test harness and set of utilities for verifying server-side service
    implementations
- akka-http-spray-json
  - Predefined glue-code for (de)serializing custom types from/to JSON with
    spray-json Details can be found here: JSON Support
- akka-http-xml
  - Predefined glue-code for (de)serializing custom types from/to XML with
    scala-xml Details can be found here: XML Support
---
Implicit Parameters
--

```
val value = 10
def addX(x: Int): Int = value + x
addX(3)
```
--

```
implicit val implicitValue = 10
def implicitAddX(implicit x: Int): Int = value + x
implicitAddX
```
---
Implicit Parameters

```
implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
def getEmployee(id: Int)(implicit e: ExecutionContext): Future[Employee] = ???
def getRole(employee :Employee)(implicit e: ExecutionContext): Future[Role] = ???
val bigEmployee: Future[EmployeeWithRole] =
  getEmployee(100).flatMap { e =>
    getRole(e).map { r =>
      EmployeeWithRole(e.id, e.name,r)
    }
  }
```
--

```
val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
val bigEmployee: Future[EmployeeWithRole] =
  getEmployee(100)(ec).flatMap { e =>
    getRole(e)(ec).map { r =>
      EmployeeWithRole(e.id, e.name,r)
    }(ec)
  }(ec)
```
--

- Implicit parameters are useful for removing boiler plate parameter passing and
  can make our code more readable. So if you find yourself passing the same
  value several times in quick succession, they can help hide the duplication.
---
Configuration

- Like other Akka modules, Akka HTTP is can be configured via Typesafe Config.
- We can provide an application.conf file application-specific settings that is
  differ from the default one.
- http://doc.akka.io/docs/akka-http/current/scala/http/configuration.html

---
Akka HTTP

- Today session we will focus on The high-level, routing API of Akka HTTP
  provides a DSL to describe HTTP routes and how they should be handled.
- Each route is composed of one or more level of \yDirective\ss that narrows
  down to handling one specific type of request.
---
Demo
---
References

- http://doc.akka.io/docs/akka-http/current/scala/http/index.html
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
