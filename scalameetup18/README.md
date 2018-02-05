# Hello Akka Streams

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

Agenda

- Futures
- Akka Streams

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
- Follows ["Reactive Streams manifesto"](http://www.reactive-streams.org/)
  which defines a standard for asynchronous streams processing

---

## Akka Streams

```
+------+   +----+  +----+
|Source|-->|Flow|->|Sink|
+------+   +----+  +----+
```

- `Source`: Entry point of stream and must be at least one in every stream
- `Flow`: Component responsible for manipulating elements of stream and there can
  be none of any number
- `Sink`: Exit point of stream and must be at least one in every stream

---

## Akka Streams

- Do not send dropped stream elements to the dead letter office

---

## References

- Futures
  - https://docs.scala-lang.org/overviews/core/futures.html
- Akka Streams
  - Akka Cookbook

---

## Thank you!

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
