
name := """botkop-geo"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.11.6", "2.10.6")

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json" % "2.4.6",
    "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

scalacOptions ++= Seq("-feature")

