# Akka Untyped vs Akka Typed Continued

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

## Akka Untyped

- Classic untyped actors does not have information
 - what types of messages that can be sent via that ActorRef
 - what type the destination Actor has
- Difficult to read and prone to programming mistakes

---

## Akka Typed

```
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-typed" % "2.5.8"
)
```

- Latest Akka Typed version is `2.5.9` and it is not ready yet
- Akka Typed module is still under-development and it may introduces some
  breaking changes in the future versions
- "If you are using Typed already and would prefer a single migration task,
  you should stay on version 2.5.8 until we have completed these changes"

---

## Akka Untyped vs Akka Typed

```
                               +---------+
                  +---- -Stop> |TERMINATE|
                  |            +---------+
+---+          +-----+              ^
|NEW| -Create> |READY|             Stop
+---+          +-----+              |
                  |             +-------+ jobs+1
                  +---- -Start> |RUNNING| <Work-
                                +-------+
```

- State variable jobs
  - Increment `1` after receive a `Work` message at `RUNNING`
- Checkout code `UThreadDemo`/`TThreadDemo` at `akka-typed-demo`

---

## Akka Untyped vs Akka Typed

- `ActorRef` in Akka Untyped vs `ActorRef[Action]` in Akka Typed
- `Actor.same` keeps the same behavior
- `Actor.deferred` is a factory for a behavior. Creation of the behavior instance is
  deferred until the actor is started, as opposed to `Actor.immutable` that
  creates the behavior instance immediately before the actor is running.
- Checkout `Actor.mutable` and `Actor.MutableBehavior` if you want to work with
  more close syntax to untyped actors
- Lightbend Akka team recommend the immutable style as the `default choice`

---

## Akka Untyped vs Akka Typed: Become/Unbecome

- Actors are state machines
- Akka Typed promote more of this style and enforce us to return
  next behavior to be used for the next messages
- State is updated by returning a new behavior that holds the
  new immutable state

---

## Akka Untyped vs Akka Typed: Supervision and Signal

```
+------+   +--------+   +-------+   +------+
|Office|-->|Director|-->|Manager|-->|Worker|
+------+   +--------+   +-------+   +------+

--> denotes supervision from left to right
```

---

## Akka Untyped vs Akka Typed: Supervision

- Default is set to return a `Restart` message when `/user` receives an exception/failure
- `OneForOneStrategy` only applies to the failed actor and does not affect another
- `AllForOneStrategy` applies to all children under supervision in case of
  failure of any one actor under a supervisor. It is removed in Akka Typed.

---

## Akka Untyped vs Akka Typed: Signal

- `watch` is used to check whether particular actor is running or not
- `onSignal` is used to check for actor liveness in Akka Typed

---

## References

- Akka Typed
  - https://akka.io/blog/2017/05/16/supervision
  - https://akka.io/blog/2017/05/19/signals
  - https://github.com/patriknw/akka-typed-blog/blob/master/src/main/scala/blog/typed/scaladsl/FlakyWorker2.scala

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

scala> val replesent = REPLesent(intp=$intp,source="~/Documents/workspace/scalameetups/scalameetup17/README.md")
replesent: REPLesent = REPLesent(0,0,~/Documents/workspace/scalameetups/scalameetup17/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@3b80bb63)

scala> import replesent._
import replesent._

scala> f
```
