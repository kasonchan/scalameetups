# Akka Streams Graphs

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

## Agenda

- Akka Configurations
- Akka Logging
  - [Logback](https://logback.qos.ch/)
- Akka Streams
  - Graphs <...> GraphsDSL
- Akka Streams Backpressure

---

## Akka Configurations and Logging Demo

---

## Akka Configurations

- `src/main/resources/application.conf` is where the developer set
  configurations can be placed to by default
- Add `akka.log-config-on-start = on` to show complete configuration at `INFO`
level when the actor system is started
  - it is set to `off` by default

---

## [Logback](https://logback.qos.ch/)

- "Logback is intended as a successor to the popular log4j project,
  picking up where log4j leaves off."
- By default, we can set logback configurations in `src/main/resources/logback.xml`

```
libraryDependencies ++= "ch.qos.logback" % "logback-classic" % "1.2.3"
```

---

## [Logback](https://logback.qos.ch/)

- Set root level
  - Reference `ASYNCSTDOUT` and `ASYNCFILE` appender

```
<root level="DEBUG">
  <appender-ref ref="ASYNCSTDOUT" />
  <appender-ref ref="ASYNCFILE" />
</root>
```

---

## [Logback](https://logback.qos.ch/)

- Create an conversion rule called `coloredLevel` referencing `logger.ColoredLevel`

```
<conversionRule conversionWord="coloredLevel" converterClass="logger.ColoredLevel" />
```

- Create an appender
  - Use console appender and name as `STDOUT`
  - Set `%d` using `ISO8601` format
  - Append `%coloredLevel`
  - `%-10` left-aligned 10-character
  - `%X` conversion specifier for `akkaSource`
  - `%n` represents a new line
  - `%xException` prints the exception if there is an exception

```
<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
  <encoder>
    <pattern>%d{ISO8601} %coloredLevel %-10logger{10} %X{akkaSource} %thread - %message%n%xException</pattern>
  </encoder>
</appender>
```

---

## [Logback](https://logback.qos.ch/)

- Create a file appender
  - Append new log to application home `/logs/application.log`

```
<appender name="FILE" class="ch.qos.logback.core.FileAppender">
    <file>${application.home:-.}/logs/application.log</file>
    <encoder>
      <pattern>%d{ISO8601} %-10level %-10logger{10} %X{akkaSource} %thread - %message%n%xException</pattern>
    </encoder>
  </appender>
```

- Create async standard out and file appender by referencing `STDOUT` and `FILE` appender

```
<appender name="ASYNCSTDOUT" class="ch.qos.logback.classic.AsyncAppender">
    <appender-ref ref="STDOUT" />
  </appender>

<appender name="ASYNCFILE" class="ch.qos.logback.classic.AsyncAppender">
    <appender-ref ref="FILE" />
  </appender>
```

---

## [Logback](https://logback.qos.ch/)

- Create a logger reference the name of the logging class

```
val log: Logger = LoggerFactory.getLogger(this.getClass)
```

- Create a named `foo` logger

```
val foo: Logger = LoggerFactory.getLogger("foo")
```

---

## Akka and [Logback](https://logback.qos.ch/)

- By default, Akka uses `akka.loggers = ["akka.event.Logging$DefaultLogger"]`
  - it logs to `STDOUT` and it is not intended to use in production
- Akka provides a logger for `SLF4J` at `akka-slf4j.jar`
- Akka team recommend [Logback](https://logback.qos.ch/)

---

## Akka and [Logback](https://logback.qos.ch/)

- Use Logback with Akka, specify the following in `src/main/resources/application.conf`

```
akka.loggers = ["akka.event.slf4j.Slf4jLogger"]
akka.loglevel = "DEBUG"
akka.logging-filter = "akka.event.slf4j.Slf4jLoggingFilter"
```

---

## Akka Streams

```
+------+   +-------+  +----+
|Source|-->|Flow(s)|->|Sink|
+------+   +-------+  +----+
```

- `Source`: Entry point of stream and must be at least one in every stream
- `Flow`: Component responsible for manipulating elements of stream and there can
  be none of any number
- `Sink`: Exit point of stream and must be at least one in every stream

---

## Akka Streams IO

```
Incoming         +----------+             +----------+
TCP/UDP Packets->|Incoming  |-ByteString->|Connection|
                 |Connection|             |Flow      |
       Outgoing<-|Flow      |<-ByteString-|          |
TCP/UDP Packets  +----------+             +----------+
```

---

## Akka Streams Graphs <- GraphDSL

- `Source->Flow(s)->Sink` is a linear computation of data (exactly 1 downstream processing stage)
- How can we deal with non-linear computation? Any guesses?

---

## Scala Collections Operations

- What would be the results of the following expression?

```
List((0,"s"), (4, "c"), (1, "a"), (2, "l"), (0, "a")).unzip

List("a", "b", "c") zip (Stream from 1)

"sca".concat("la")
```

---

## Scala Collections Operations

- Given the following `Map`s
- Expecting to get `Map("Sidney" -> 2, "Paul" -> 6, "Nick" -> 2, "Jacob" -> 7)``

```
val names = Map("Sidney" -> 1, "Paul" -> 1, "Jacob" -> 7)
val moreNames = Map("Sidney" -> 1, "Paul" -> 5, "Nick" -> 2)
```

--

```
val mergedMap = names ++ moreNames.map {
  case (name,count) => name -> (count + names.getOrElse(name,0)) }
```

---

## Akka Streams Simple Graph Demo

---

## Akka Streams Graphs

- Graphs are built from simple Flows which serve as the linear connections within the graphs
- Junctions serves as fan-in and fan-out points for Flows
  - Fan-out
    - `Broadcast[T]` – (1 input, N outputs) given an input element emits to each output
    - `Balance[T]` – (1 input, N outputs) given an input element emits to
      one of its output ports
    - `UnzipWith[In,A,B,...]` – (1 input, N outputs) takes a function of 1 input
      that given a value for each input emits N output elements (where N <= 20)
    - `UnZip[A,B]` – (1 input, 2 outputs) splits a stream of (A,B) tuples into
      two streams, one of type A and one of type B

---

## Akka Streams Graphs

- Junctions serves as fan-in and fan-out points for Flows
  - `Merge[In]` – (N inputs , 1 output) picks randomly from inputs
    pushing them one by one to its output
  - `MergePreferred[In]` – like Merge but if elements are available on preferred port,
    it picks from it, otherwise randomly from others
  - `MergePrioritized[In]` – like Merge but if elements are available on
    all input ports, it picks from them randomly based on their priority
  - `ZipWith[A,B,...,Out]` – (N inputs, 1 output) which takes a function of
    N inputs that given a value for each input emits 1 output element
  - `Zip[A,B]` – (2 inputs, 1 output) is a ZipWith specialized to zipping
    input streams of A and B into a (A,B) tuple stream
  - `Concat[A]` – (2 inputs, 1 output) concatenates two streams
    (first consume one, then the second one)
- Checkout more details on on [stages overview](https://doc.akka.io/docs/akka/2.5/stream/stages-overview.html)

---

## Akka Streams Graphs

- GraphDSL is not able to provide compile time type-safety about whether or not
all elements have been properly connected—this validation is performed as a
runtime check during the graph’s instantiation

---

## [Akka Streams Backpressure](https://www.slideshare.net/Lightbend/understanding-akka-streams-back-pressure-and-asynchronous-architectures)

+---------+             +----------+
|Publisher|-message(s)->|Subscriber|
+---------+             +----------+

- Fast Publisher [100 operations per second] and slow Subscriber [1 operation per second]
- Subscriber usually has some kind of buffer
  - What to do if buffer overflow?
    - TCP and Kernel: Bounded buffer: drop messages + require resend messages
    - Increase buffer size? Out of memory? While memory available
    - Reactive streams: Dynamic push/pull pull-based-backpressure

---

## Akka Streams Backpressure Demo

---

## [Akka Streams](https://www.slideshare.net/Lightbend/understanding-akka-streams-back-pressure-and-asynchronous-architectures)

- Checkout more `OverflowStrategy` at https://doc.akka.io/japi/akka/current/akka/stream/OverflowStrategy.html
- Reactive Streams goals: avoid unbounded buffering across async boundaries and inter-op interfaces between various libraries
- Checkout for [Understanding Akka Streams, Back Pressure, and Asynchronous Architectures](https://www.slideshare.net/Lightbend/understanding-akka-streams-back-pressure-and-asynchronous-architectures) for Asynchronous processing toolbox power and constraints Slides 31-37

---

## References

- [Logback](https://logback.qos.ch/)
- Akka Logging
  - https://doc.akka.io/docs/akka/2.5/logging.html
- Akka Streams
  - https://doc.akka.io/docs/akka/2.5/stream/stream-graphs.html
  - Akka Cookbook
  - https://chariotsolutions.com/blog/post/simply-explained-akka-streams-backpressure/
  - https://www.slideshare.net/Lightbend/understanding-akka-streams-back-pressure-and-asynchronous-architectures

---

## Thank you for coming!

## Q&A/Comments/Suggestions?

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

scala> val replesent = REPLesent(intp=$intp,source="~/Documents/workspace/scalameetups/scalameetup20/README.md")
replesent: REPLesent = REPLesent(0,0,~/Documents/workspace/scalameetups/scalameetup20/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@3b80bb63)

scala> import replesent._
import replesent._

scala> f
```
