import Dependencies._

ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "com.ainr"

lazy val root = (project in file("."))
  .settings(
    name := "GdeTramStatsBot",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.bot4s" %% "telegram-akka" % "4.4.0-RC2"
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
