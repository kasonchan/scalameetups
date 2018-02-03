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

## Akka Typed: Supervision

- Default supervisory strategy

```
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._
import scala.concurrent.duration._

override val supervisorStrategy =
  OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: ArithmeticException      ⇒ Resume
    case _: NullPointerException     ⇒ Restart
    case _: IllegalArgumentException ⇒ Stop
    case _: Exception                ⇒ Escalate
  }
```

---

## Akka Typed: Supervision

- When the supervisor strategy is not defined for an actor the following
  exceptions are handled by default:
  - ActorInitializationException will stop the failing child actor
  - ActorKilledException will stop the failing child actor
  - DeathPactException will stop the failing child actor
  - Exception will restart the failing child actor
  - Other types of Throwable will be escalated to parent actor
- If the exception escalate all the way up to the root guardian it will handle
  it in the same way as the default strategy defined above.

---

## Akka Untyped vs Akka Typed: Supervision

- Default is set to return a `Restart` message when `/user` receives an exception/failure
- `OneForOneStrategy` only applies to the failed actor and does not affect another
- `AllForOneStrategy` applies to all children under supervision in case of
  failure of any one actor under a supervisor. It is removed in Akka Typed.
- Supervisor strategy `Escalate` and `Stop` removed in Akka Typed

---

## Akka Untyped vs Akka Typed: Signal

- `watch` is used to check whether particular actor is running or not
- `onSignal` is used to check for actor liveness in Akka Typed
- `preStart` and `postRestart` are replaced by placing such startup code in the
  constructor of the behavior

---

## Akka Untyped vs Akka Typed: Logging

- `log.info` in Akka Untyped logs the appropriate actor's path as opposite to
  `ctx.system.log` logs `[akka.actor.ActorSystemImpl(toffice)]` which is the
  `ActorSystem`'s name in Akka Typed

---

## References

- Akka
  - https://doc.akka.io/docs/akka/2.5/fault-tolerance.html
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
