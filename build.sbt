
name := """botkop-geo"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.6"

libraryDependencies ++= Seq(
    "com.typesafe.play" % "play-json_2.10" % "2.4.6",
    // "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "com.typesafe.scala-logging" % "scala-logging-slf4j_2.10" % "2.1.2",
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

scalacOptions ++= Seq("-feature")

