# Data Extraction and Transformation

| Data Extraction and Transformation

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

We are given data source `oak-water-potentials-simple.csv`. Let's write a 
program to answer the following questions.

1. How many different species are recorded in these data?
2. Mid day water potential should always be at least as negative as pre-dawn 
   water potential. Are there any days and plants for which mid-day water
   potential is higher than pre-dawn?
3. What is the lowest (most negative) mid-day water potential in this data set?
4. For which year was the average mid day water potential lowest (most negative)?

---

The answer to the questions are as follow:

1. How many different species are recorded in these data?
   5 species are recorded in these data.

2. Mid day water potential should always be at least as negative as pre-dawn 
   water potential. Are there any days and plants for which mid-day water
   potential is higher than pre-dawn?
   "07/21/11", "05/23/13", "05/24/13", "04/10/11", "05/22/13", "08/25/12" are days
   plants for which mid-day water potential is higher than pre-dawn.

3. What is the lowest (most negative) mid-day water potential in this data set?
   The lowest mid-day water potential in this data set is -6.75, it is recorded on 
   "04/10/11" for "QUGR3 " sp.

4. For which year was the average mid day water potential lowest (most negative)? 
   11 was the average mid day water potential lowest (most negative).

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

scala> val replesent = REPLesent(intp=$intp,source="~/scalameetups/scalameetup14/README.md")
replesent: REPLesent = REPLesent(0,0,~/scalameetups/scalameetup14/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@3b80bb63)

scala> import replesent._
import replesent._

scala> f
```
