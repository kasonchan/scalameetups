# sbt-rpm-docker

The is a web app serving at port `9000` built with Akka HTTP
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
  - `-y` option passes `yes` to `yum` command prompt to install Java OpenJDK. 

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

- sbt-native-packager
  - http://www.scala-sbt.org/sbt-native-packager/formats/index.html
- Docker commands
  - https://docs.docker.com/engine/reference/commandline/docker/
