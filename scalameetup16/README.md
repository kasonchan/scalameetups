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

## sbt-native-packager

- "Code once, deploy anywhere"
- Checkout https://sbt-native-packager.readthedocs.io/en/stable/formats/index.html

---

## sbt-native-packager

- plugins.sbt

```
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.2")
```

- build.sbt
  - Java application

```
enablePlugins(JavaAppPackaging)
```

  - Java Web Application

```
enablePlugins(JavaServerAppPackaging)
```

---

## RPM Plugin

- Include the following in `build.sbt` to enable RPM packaging

```
enablePlugins(SystemdPlugin, RpmPlugin)

lazy val changelog = "changelog.txt"
maintainer in Linux := "Kason Chan <kasonl.chan@gmail.com>"
packageSummary in Linux := "Sandbox Akka Typed Docker"
packageDescription in Rpm := "Sandbox Akka Typed Docker"
rpmBrpJavaRepackJars := true
rpmRelease := "1"
rpmVendor := "kasonchan"
rpmGroup := Some("sandbox-akka-typed-docker")
rpmUrl := Some("https://github.com/kasonchan/sandbox-akka-typed-docker")
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
