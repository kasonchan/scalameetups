lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      scalaVersion := "2.12.3",
      version := "0.0.1"
    )),
  name := "scalameetup11",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % "10.0.10",
    "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10",
    "com.typesafe.akka" %% "akka-http-xml" % "10.0.10"
  ))
