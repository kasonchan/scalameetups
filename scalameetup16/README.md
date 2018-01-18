# Hello World Akka Typed and Docker

| Hello World Akka Typed

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

# sbt-native-packager

- "Code once, deploy anywhere"
- Go to https://sbt-native-packager.readthedocs.io/en/stable/formats/index.html

---

# sbt-native-packager

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

# RPM Plugin

- Include the following in `build.sbt`

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

# RPM Plugin

- Execute the packaging RPM command

```
$ sbt rpm:packageBin
```

---

# Docker Plugin

- Execute the packaging Docker command

```
$ sbt docker:publishLocal
```

- Run the immage in a container

```
$ docker run [OPTIONS] IMAGE[:TAG|@DIGEST] [COMMAND] [ARG...]
$ docker run -dt -p 127.0.0.1:9000:9000 sandbox-akka-typed-docker:0.0.1 usr/share/sandbox-akka-typed-docker/bin/sandbox-akka-typed-docker
```

```
$ curl localhost:9000
```

```
$ docker exec -ti elastic_agnesi bash
```

```
$ ps -ef
```

---


---

# References

- Akka
- Akka Typed
  - https://akka.io/blog/2017/05/05/typed-intro
- sbt-native-packager
  - http://www.scala-sbt.org/sbt-native-packager/formats/index.html

---

| Thank you!

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

scala> val replesent = REPLesent(intp=$intp,source="/Users/kason.chan/Documents/workspace/scalameetups/scalameetup13/README.md")
replesent: REPLesent = REPLesent(0,0,/Users/kason.chan/Documents/workspace/scalameetups/scalameetup13/README.md,true,true,scala.tools.nsc.interpreter.ILoop$ILoopInterpreter@3b80bb63)

scala> import replesent._
import replesent._

scala> f
```
