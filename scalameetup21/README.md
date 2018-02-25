# Akka Streams Graphs

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

## Agenda

- Akka Remoting
- Akka Streams

---

## Akka Remoting

- Akka remoting enable remoting capabilities
- Akka remoting is designed for communication in a peer-to-peer fashion 
  and it has limitations for client-server setups
- Add the following to your build file `build.sbt`

```
"com.typesafe.akka" %% "akka-remote" % "akka.version"
```

---

## Akka Remoting Demo: Master and Minion

---

## Akka Remoting

- Add the following to `src/resources/application.conf` to enable Akka Remoting 
  at minimal 

```
akka {
  actor {
    provider = remote
  }
  remote {
    enabled-transports = ["akka.remote.netty.tcp"]
    netty.tcp {
      hostname = "127.0.0.1"
      port = 2552
    }
 }
}
```

- `akka.actor.provider` as `remote`
- `hostname` is the machine that you run the actor system on, it can be 
  reachable IP address or reachable hostname, i.e. 'localhost'
- `port` is specific port that the actor system will listen on, by default is `2552`
  - Set to `0` to have it chosen automatically

---

## Akka Remoting

- Two ways of using remoting:
  - Creation: used to create an actor on a remote node with 
    `actorOf(Props(...), actorName)`
    - The `actorName` need to match set in the configuration file 
      if you are creating remote actors using configuration file
  - Lookup: used to look up an actor on a remote node with `actorSelection(path)`
  
---

## Akka Remoting - Creation

- Specify the remote actor at `src/main/resources/application.conf`
  - By default, the `ActorSystem` read overriden configurations from this file

```
akka {
  actor {
    deployment {
      /sampleActor {
        remote = "akka.tcp://sampleActorSystem@127.0.0.1:2553"
      }
    }
  }
}
```

- Remote actor specified above in the configuration will not be instantiated 
  automatically until we create it an actor by the same deployment name

```
val actor = system.actorOf(Props[SampleActor], "sampleActor")
```

---

## Akka Remoting - Lookup

- Lookup `actorSelection` pattern 
  `akka.<protocol>://<actor system name>@<hostname>:<port>/<actor path>`
- To acquire an `ActorRef` for an `actorSelection`, we can use `resolveOne` to get
  a `Future[ActorRef]`

```
val selection =
  context.actorSelection("akka.tcp://actorSystemName@10.0.0.1:2552/user/actorName")
```

---

## References

- Akka Remoting
  - https://doc.akka.io/docs/akka/2.5/remoting-artery.html#remote-bytebuffer-serialization
- https://www.slideshare.net/ktoso/zen-of-akka
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

scala> val replesent = REPLesent(intp=$intp,source="~/Documents/workspace/scalameetups/scalameetup21/README.md")
replesent: REPLesent = REPLesent(0,0,~/Documents/workspace/scalameetups/scalameetup21/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@3b80bb63)

scala> import replesent._
import replesent._

scala> f
```
