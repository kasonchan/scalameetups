# IO and Graphs with Akka Streams

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

## Agenda

- Akka Streams
  - Graphs

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

## Akka Streams TCP IO

```
Incoming         +----------+             +----------+
TCP/UDP Packets->|Incoming  |-ByteString->|Connection|
                 |Connection|             |Flow      |
       Outgoing<-|Flow      |<-ByteString-|          |
TCP/UDP Packets  +----------+             +----------+
```

---

## Scala Collections Operations

---

## Akka Streams Graphs <- Graph DSL

- `Source->Flow(s)->Sink` is a linear computation of data (exactly 1 downstream processing stage)
- How can we deal with non-linear computation? Any guesses?

---

## Akka Streams Simple Graph Demo

---

## References

- Akka Streams
  - https://doc.akka.io/docs/akka/2.5/stream/stream-graphs.html
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

scala> val replesent = REPLesent(intp=$intp,source="~/Documents/workspace/scalameetups/scalameetup20/README.md")
replesent: REPLesent = REPLesent(0,0,~/Documents/workspace/scalameetups/scalameetup20/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@3b80bb63)

scala> import replesent._
import replesent._

scala> f
```
