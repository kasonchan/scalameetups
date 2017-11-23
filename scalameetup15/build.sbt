name := "scalameetup15"
 
version := "1.0" 
      
lazy val `scalameetup15` = (project in file(".")).enablePlugins(PlayScala)
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( ehcache , ws , specs2 % Test , guice,
  "com.typesafe.play" %% "play-iteratees" % "2.6.1",
  "org.webjars" %% "webjars-play" % "2.6.1",
  "org.webjars" % "jquery" % "3.0.0"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  
