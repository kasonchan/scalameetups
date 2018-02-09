# Hello Akka Streams

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

Agenda

- Futures
- Akka Streams
  - Basic Components and operations
  - Time-based Processing
  - Error Handling

---

## Futures

- Used to perform an operation in other threads for concurrency and parallel
  execution
- Placeholder construct for a value that may not exist yet
- Monad for value executed in time

---

## Futures

- Object holding a value which may become available at some point
- Value is usually the result of some other computation:
  - If the computation
    - has not yet completed, `Future` is not completed.
    - has completed with a value or with an exception, `Future` is completed
  - Completion can take one of two forms:
    - When a Future is completed
      - with a value, `Future` was successfully completed with that value
      - with an exception thrown by the computation, `Future` was failed with
        that exception

---

## Futures

```scala
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

val sumFuture: Future[Int] = Future(1+2).mapTo[Int]
sumFuture.onComplete {
  case Success(value) => println(value)
  case Failure(error) => println(error.getMessage)
} // (executionContext)
```

---

## Futures

- `ExecutionContext` is similar to an Executor
  - free to execute computations in a new thread, in a pooled thread or in the current thread
- `ExecutionContext.global` is an ExecutionContext backed by a `ForkJoinPool`
- `ForkJoinPool` manages a limited amount of threads
- Parallelism level is maximum amount of threads being referred to
  - `scala.concurrent.context.minThreads` - defaults to Runtime.availableProcessors
  - `scala.concurrent.context.numThreads` - can be a number or a multiplier (N) in the form ‘xN’ ; defaults to Runtime.availableProcessors
  - `scala.concurrent.context.maxThreads` - defaults to `Runtime.availableProcessors`
  - The parallelism level will be set to `numThreads` as long as it remains
    within [minThreads; maxThreads]

---

## Akka Streams

- Akka Module built to make ingestion and processing of streams easier
- Motivation for nowadays services consumption includes many instances of streams of data
  downloading or uploading
  - Data transfer across computers i.e. via TCP becomes too large to handle as Big Data
  - Akka Actor is tedious and error-prone to handle streams of data not to overflow the mailboxes and buffers
- Follows ["Reactive Streams manifesto"](http://www.reactive-streams.org/)
  which defines a standard for asynchronous streams processing

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

## Akka Streams Simple Stream Demo

---

- `Materializer`
  - Factory for stream execution engines
  - lets streams `run`
- Connections of `Source`, `Flow`, `Sink` is like architect's blueprint
  - They can be reused and incorporated in larger design

---

## Akka Streams Factorials Demo

---

## Akka Streams Time-based Processing

- `throttle` combinator
  - slows down the stream to 1 element per second
  - sends elements downstream with speed limited to `elements/per`
  - stage set the maximum rate * for emitting messages.
  - works for streams where all elements have the same cost or length

---

## Akka Streams Tweets Demo

---

## Akka Streams Operations

- Basic operations that can be apply to Scala collections can be applied to Akka Streams

---

## Akka Streams Error Handling Demo

---

## Akka Streams Error Handling and Supervision

- `Supervision` strategies `Restart`, `Resume`, `Stop` can be applied to
  Akka Streams as `Supervision.Decider` at two levels
  - materializer: `ActorMaterializerSettings.withSupervisionStrategy(decider)`
  - flow: `flow.withAttributes(ActorAttributes.supervisionStrategy(decider))`
- Supervision strategies
  - `Restart`: element gets dropped and stream keeps running; stateful stages
    will be lost because a new instance of the stage is created
  - `Resume`: element gets dropped and stream keeps running and processing subsequent elements
  - `Stop`: stream is completed with a failure

---

## References

- Futures
  - https://docs.scala-lang.org/overviews/core/futures.html
- Akka Streams
  - https://doc.akka.io/docs/akka/2.5/stream/stream-quickstart.html
  - Akka Cookbook

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

scala> val replesent = REPLesent(intp=$intp,source="~/Documents/workspace/scalameetups/scalameetup18/README.md")
replesent: REPLesent = REPLesent(0,0,~/Documents/workspace/scalameetups/scalameetup18/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@3b80bb63)

scala> import replesent._
import replesent._

scala> f
```
