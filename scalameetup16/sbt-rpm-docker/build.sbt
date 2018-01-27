import com.typesafe.sbt.packager.docker._

name := "sbt-rpm-docker"

version := "0.0.1"

scalaVersion := "2.12.4"

enablePlugins(SystemdPlugin, RpmPlugin, DockerPlugin, JavaServerAppPackaging)

// RPM configurations
lazy val changelog = "change"

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

// Docker configurations
mappings in Docker += file("target/rpm/RPMS/noarch/sbt-rpm-docker-0.0.1-1.noarch.rpm") -> "sbt-rpm-docker-0.0.1-1.noarch.rpm"

dockerExposedPorts := Seq(9000)

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

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10"
)
