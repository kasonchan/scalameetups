# Akka Remoting and Clustering

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

## Agenda

- Akka Remoting
- Akka Clustering

---

## Akka Remoting

- Akka remoting enable remoting capabilities
- Akka remoting is designed for communication in a peer-to-peer fashion 
  and it has limitations for client-server setups

---

## Akka Remoting

- Two ways of using remoting:
  - Creation: used to create an actor on a remote node with 
    `actorOf(Props(...), actorName)`
    - The `actorName` need to match set in the configuration file 
      if you are creating remote actors using configuration file
    - Programmatically Creation
      ```
      val address = AddressFromURIString("akka.tcp://sys@host:1234")
      val actor = system.actorOf(Props[SampleActor]
      	.withDeploy(Deploy(scope = RemoteScope(address))))
      ```
  - Lookup: used to look up an actor on a remote node with `actorSelection(path)`

---

## Terminologies

- Node: A logical member of a cluster. There could be multiple nodes on a 
physical machine. Defined by a `hostname:port:uid` tuple.
- Cluster: A set of nodes joined together through the membership service.

---

## Akka Cluster

- Akka Cluster provides a fault-tolerant decentralized peer-to-peer based 
  cluster membership service with no single point of failure or single point of 
  bottleneck.
- Akka Cluster allows us to build distributed multi-nodes applications.

---

## Akka Cluster

- Checkout https://doc.akka.io/docs/akka/2.5/common/cluster.html#membership-lifecycle

---

## Akka Cluster Demo

---

## Akka Cluster Singleton

- Exactly one actor of a certain type running somewhere in the cluster.
- Singleton actor always run on the oldest member.
- To use it, include `"com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion`
  in `build.sbt`.
- Start nodes `sbt -Dconfig.resource=application-cluster-autodown-1.conf run`,
  `sbt -Dconfig.resource=application-cluster-autodown-1.conf run`

---

## References

- https://doc.akka.io/docs/akka/2.5/common/cluster.html
- https://doc.akka.io/docs/akka/2.5.3/scala/cluster-usage.html

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

scala> val replesent = REPLesent(intp=$intp,source="~/Documents/workspace/scalameetups/scalameetup22/README.md")
replesent: REPLesent = REPLesent(0,0,~/Documents/workspace/scalameetups/scalameetup22/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@3b80bb63)

scala> import replesent._
import replesent._

scala> f
```