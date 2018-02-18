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
    - `Balance[T]` – (1 input, N outputs) given an input element emits to one of its output ports
    - `UnzipWith[In,A,B,...]` – (1 input, N outputs) takes a function of 1 input that given a value for each input emits N output elements (where N <= 20)
    - `UnZip[A,B]` – (1 input, 2 outputs) splits a stream of (A,B) tuples into two streams, one of type A and one of type B

---

## Akka Streams Graphs

- Junctions serves as fan-in and fan-out points for Flows
  - `Merge[In]` – (N inputs , 1 output) picks randomly from inputs pushing them one by one to its output
  - `MergePreferred[In]` – like Merge but if elements are available on preferred port, it picks from it, otherwise randomly from others
  - `MergePrioritized[In]` – like Merge but if elements are available on all input ports, it picks from them randomly based on their priority
  - `ZipWith[A,B,...,Out]` – (N inputs, 1 output) which takes a function of N inputs that given a value for each input emits 1 output element
  - `Zip[A,B]` – (2 inputs, 1 output) is a ZipWith specialized to zipping input streams of A and B into a (A,B) tuple stream
  - `Concat[A]` – (2 inputs, 1 output) concatenates two streams (first consume one, then the second one)
- Checkout more details on on [stages overview](https://doc.akka.io/docs/akka/2.5/stream/stages-overview.html)

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
