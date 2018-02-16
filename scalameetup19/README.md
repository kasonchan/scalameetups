# IO and Graphs with Akka Streams

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

## Agenda

- [better-files](https://github.com/pathikrit/better-files)

- [jwt-scala](https://github.com/pauldijou/jwt-scala)
- Akka Streams
  - Basic Components and operations
  - File IO
  - IO and Graphs

---

## [better-files](https://github.com/pathikrit/better-files)

- Simple, safe and intuitive Scala I/O

```
libraryDependencies ++= Seq(
  "com.github.pathikrit" %% "better-files" % "3.4.0"
)
```

---

## Akka Streams File IO Demo

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
Incoming     +----------+             +----------+
TCP Packets->|Incoming  |-ByteString->|Connection|
             |Connection|             |Flow      |
   Outgoing<-|Flow      |<-ByteString-|          |
TCP Packets  +----------+             +----------+
```

---

## Akka Streams IO Demo

---

---

## Akka Streams IO

- Run the following command
```
echo -n 'Hello World from Akka Stream!' | nc 127.0.0.1 19999
```

---

## uPickle

- uPickle: a simple Scala JSON serialization library

---

## jwt-scala

- JWT support for Scala. Bonus extensions for Play, Play JSON, Json4s, Circe and uPickle

---

```
echo -n '{"$type":"messages.Encode","msg":"Hello world from Akka Stream"}' | nc 127.0.0.1 19999
```

---

## Akka Streams IO

- `Flow` process `ByteString` s from and to the TCP Socket
- `Framing.delimiter` helper `Flow` to chunk the inputs up into actual lines of text
  - 1 `ByteString` does not have to correspond to exactly one line of text
- `allowTruncation` boolean argument indicates we require an explicit line ending even for the last message before the connection is closed

---

## References

- [better-files](https://github.com/pathikrit/better-files)
- Akka Streams
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

scala> val replesent = REPLesent(intp=$intp,source="~/Documents/workspace/scalameetups/scalameetup19/README.md")
replesent: REPLesent = REPLesent(0,0,~/Documents/workspace/scalameetups/scalameetup19/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@3b80bb63)

scala> import replesent._
import replesent._

scala> f
```
