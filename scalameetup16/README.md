# Hello World Akka Typed and Docker

---

This presentation slides are built with REPLesent!

REPLesent is a neat little tool to build presentations
using the Scala REPL.

---

- Akka
- Akka Typed
- sbt-native-packager
  - RPM
  - Docker

---

## Akka & Actor Model

- Toolkit built by Lightbend
  - Actors (Local, Remote, Typed), Network (Cluster, Cluster Sharding), Streams, HTTP/HTTPs
- A computation model invented by Hewit, Bishop and Steiger in 1973
- Actor
  - A computational entity, in response to a message it receives
    - Send a finite number of messages to other actors
    - Create a finite number of new actors
    - Designate the behavior to be used for the next message it receives

---

- `Main.scala`

```scala
import actors.U
import akka.actor.{ActorSystem => ASU, Props => PropsU}
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout

object Main {
  def main(args: Array[String]): Unit = {
    implicit val timeout = Timeout(5 seconds)

        val systemU = ASU("asu")
        val au = systemU.actorOf(PropsU[U], "au")

        au ! "noReply"
        val future1 = (au ? "helloworld").mapTo[String]
        val reply1 = Await.result(future1, 5.seconds)
        println(reply1)

        val future2 = (au ? 42).mapTo[String]
        val reply2 = Await.result(future2, 5.seconds)
        println(reply2)
  }
}
```

---

- `actors/U.scala`

```scala
class U extends Actor with ActorLogging {
  def receive: PartialFunction[Any, Unit] = {
    case "noReply" =>
      log.info("received noReply from {}", sender)
    case "helloworld" =>
      sender ! s"helloworld from ${self.path.name}"
      log.info("received helloworld from {}", sender)
    case _ =>
      sender ! s"unknown message from ${self.path.name}"
      log.info("received unknown message from {}", sender())
  }
}
```

---

```
helloworld from au
unknown message from au

[akka://asu/user/au] received noReply from Actor[akka://asu/deadLetters]
[asu-akka.actor.default-dispatcher-2] [akka://asu/user/au] received helloworld from Actor[akka://asu/temp/$a]
[asu-akka.actor.default-dispatcher-2] [akka://asu/user/au] received unknown message from Actor[akka://asu/temp/$b]
```

---

## Akka Untyped

- Classic actors
  - No information of
    - what types of messages can be sent to the actors
    - what type of destination actor has

---

## Akka Typed

- `2.5.9` is the latest version. However, Lightbend Akka team recommend to stay on version
`2.5.8` for now if we prefer a single migration task.

```
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-typed" % "2.5.8"
)
```

---

## Akka Typed

```
import akka.actor.ActorSystem
val systemU = ActorSystem("asu")
val au = systemU.actorOf(PropsU[U], "au")

val systemT = ActorSystem("ast")
val at: ActorRef[Greeter.Command] =
  systemT.spawn(Greeter.greeterBehavior, "at")
```

---

## Akka Untyped

```
object Greeter1 {
  case object Greet
  final case class WhoToGreet(who: String)
}

class Greeter1 extends Actor {
  import Greeter1._

  private var greeting = "hello"

  override def receive = {
    case WhoToGreet(who) =>
      greeting = s"hello, $who"
    case Greet =>
      println(greeting)
  }
}
```

---

# Akka Typed

```
object Greeter2 {
  sealed trait Command
  case object Greet extends Command
  final case class WhoToGreet(who: String) extends Command

  val greeterBehavior: Behavior[Command] = greeterBehavior(currentGreeting = "hello")

  private def greeterBehavior(currentGreeting: String): Behavior[Command] =
    Actor.immutable[Command] { (ctx, msg) =>
      msg match {
        case WhoToGreet(who) =>
          greeterBehavior(s"hello, $who")
        case Greet =>
          println(currentGreeting)
          Actor.same
      }
    }
}
```

---

## sbt-rpm-docker

The is a web app serving at port `9000` built with Akka HTTP.
- Note that in `Server.scala`, host is bind to `0.0.0.0` which means any traffics instead of `127.0.0.1` nor `localhost`

---

## sbt-native-packager

- "Code once, deploy anywhere"
- Checkout https://sbt-native-packager.readthedocs.io/en/stable/formats/index.html

---

## sbt-native-packager

- `project/plugins.sbt`

```
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.2")
```

- `build.sbt`
  - Java application
  - Java Web Application

```
enablePlugins(JavaAppPackaging)
enablePlugins(JavaServerAppPackaging)
```

---

## RPM Plugin

- Include the following in `build.sbt` to enable RPM packaging

```
enablePlugins(SystemdPlugin, RpmPlugin)

lazy val changelog = "change.log"
maintainer in Linux := "Kason Chan <kasonl.chan@gmail.com>"
packageSummary in Linux := "SBT RPM Docker"
packageDescription in Rpm := "SBT RPM Docker"
rpmBrpJavaRepackJars := true
rpmRelease := "1"
rpmVendor := "kasonchan"
rpmGroup := Some("sbt-rpm-docker")
rpmUrl := Some("https://github.com/kasonchan/sbt-rpm-docker")
rpmLicense := Some("Apache v2")
rpmChangelogFile := Some(changelog)
```

---

## RPM Plugin

- Execute the packaging RPM command
  - rpm will be built in `target/rpm/RPMS/noarch`

```
$ sbt rpm:packageBin
```

---

## Docker

- Map the built RPM package into the Docker build image, include the following in the `build.sbt`

```
mappings in Docker += file("target/rpm/RPMS/noarch/sbt-rpm-docker-0.0.1-1.noarch.rpm") -> "sbt-rpm-docker-0.0.1-1.noarch.rpm"
```

- Expose the container port by adding the following to the `build.sbt`

```
dockerExposedPorts := Seq(9000)
```

- Specify the `Dockerfile` for building the Docker image
  - `ExecCmd` note the command will be executed during the building of the container
  - `Cmd` note the command will be executed after the container is built
  - `-y` option passes `yes` to `yum` command prompt to install Java OpenJDK

```
dockerCommands := Seq(
  Cmd("FROM", "centos:latest"),
  Cmd("EXPOSE", "9000"),
  ExecCmd("RUN", "cat", "/etc/centos-release"),
  Cmd("WORKDIR", "/workspace"),
  Cmd("ADD", "sbt-rpm-docker-0.0.1-1.noarch.rpm", "/workspace"),
  Cmd("RUN", "yum", "install", "-y", "java-1.8.0-openjdk"),
  Cmd("RUN", "rpm2cpio", "sbt-rpm-docker-0.0.1-1.noarch.rpm", "|", "cpio", "-idmv"),
  ExecCmd("RUN", "ls", "-laRth", ".")
)
```

---

## Docker Plugin

- Execute the packaging Docker command

```
$ sbt docker:publishLocal
```

- Create a new container using the image created earlier
  - `-d` Detached
  - `-t` Allocate a pseudo-tty

```
# docker run [OPTIONS] IMAGE[:TAG|@DIGEST] [COMMAND] [ARG...]
$ docker run -dt -p 127.0.0.1:9000:9000 sandbox-akka-typed-docker:0.0.1 usr/share/sandbox-akka-typed-docker/bin/sandbox-akka-typed-docker
```

- List containers
  - `-a` Show all containers (default shows just running)

```
# docker ps [OPTIONS]
$ docker ps
$ docker ps -a
```

---

## Docker Plugin

- Curl the web application

```
$ curl localhost:9000
```

- Run a command in a running container
  - `-i` Keep STDIN open even if not attached
  - `-t` Allocate a pseudo-TTY

```
# docker exec [OPTIONS] CONTAINER COMMAND [ARG...]
$ docker exec -ti elastic_agnesi bash
```

- Check if the web application process is running
  - `-e` Writes information to standard output about all processes, except kernel processes
  - `-f` Generates a full listing

```
$ ps -ef | grep sandbox-akka-typed-docker
```

---

## References

- Akka
- Akka Typed
  - https://akka.io/blog/news/2018/01/11/akka-2.5.9-released-2.4.x-end-of-life
  - https://akka.io/blog/2017/05/05/typed-intro
- sbt-native-packager
  - http://www.scala-sbt.org/sbt-native-packager/formats/index.html
- Docker commands
  - https://docs.docker.com/engine/reference/commandline/docker/

---

| Thank you!

| Q&A/Comments/Suggestions?

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

scala> val replesent = REPLesent(intp=$intp,source="~/Documents/workspace/scalameetups/scalameetup16/README.md")
replesent: REPLesent = REPLesent(0,0,~/Documents/workspace/scalameetups/scalameetup16/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@3b80bb63)

scala> import replesent._
import replesent._

scala> f
```
