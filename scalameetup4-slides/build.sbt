name := "scalameetup4-slides"

version := "0.0.1"

scalaVersion := "2.12.3"

lazy val root = project
  .in(file("."))
  .settings(scalaSettings)
  .settings(publishSettings)
  .settings(dependencySettings)
  .enablePlugins(GhpagesPlugin, TutPlugin)

lazy val scalaSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-unchecked",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture"
  )
)

lazy val publishSettings = Seq(
  ghpagesNoJekyll := true,
  makeSite := (makeSite dependsOn tut).value,
  siteSourceDirectory := tutTargetDirectory.value,
  ghpagesPushSite := (ghpagesPushSite dependsOn makeSite).value
  git.remoteRepo := "git@github.com:kasonchan/scalameetups.git"
)

lazy val refinedVersion = "0.8.1"

lazy val dependencySettings = Seq(
  libraryDependencies ++= Seq(
    "eu.timepit" %% "refined" % refinedVersion,
    "eu.timepit" %% "refined-pureconfig" % refinedVersion)
)
