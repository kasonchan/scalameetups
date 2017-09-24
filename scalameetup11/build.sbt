lazy val akkaVersion = "10.0.10"
lazy val swaggerVersion = "1.5.16"

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      scalaVersion := "2.12.3",
      version := "0.0.1"
    )),
  name := "scalameetup11",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-xml" % akkaVersion,
    "io.swagger" % "swagger-core" % swaggerVersion,
    "io.swagger" % "swagger-annotations" % swaggerVersion,
    "io.swagger" % "swagger-models" % swaggerVersion,
    "io.swagger" % "swagger-jaxrs" % swaggerVersion,
    "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.11.0",
    "ch.megard" %% "akka-http-cors" % "0.2.1"
  ))
