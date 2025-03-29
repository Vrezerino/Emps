name := """Emps"""
organization := "ppark"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.16"

libraryDependencies += guice
libraryDependencies += "org.postgresql" % "postgresql" % "42.7.5"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test