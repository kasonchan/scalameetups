# Building a Chat Application

| Building a Chat Application

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---



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

scala> val replesent = REPLesent(intp=$intp,source="~/scalameetups/scalameetup15/README.md")
replesent: REPLesent = REPLesent(0,0,~/scalameetups/scalameetup15/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@3b80bb63)

scala> import replesent._
import replesent._

scala> f
```
