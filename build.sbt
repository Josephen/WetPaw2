name := """WetPaw2"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  guice,
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
  "com.h2database" % "h2" % "1.4.199",
  "org.postgresql" % "postgresql" % "42.1.1",
  "org.mindrot" % "jbcrypt" % "0.4",
  "com.pauldijou" %% "jwt-play" % "4.2.0",
  "com.pauldijou" %% "jwt-core" % "4.2.0",
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
