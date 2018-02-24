# Akka Streams Graphs

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

## Agenda

- Akka Remote Actors
- Akka Streams

---



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

scala> val replesent = REPLesent(intp=$intp,source="~/Documents/workspace/scalameetups/scalameetup21/README.md")
replesent: REPLesent = REPLesent(0,0,~/Documents/workspace/scalameetups/scalameetup21/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@3b80bb63)

scala> import replesent._
import replesent._

scala> f
```
