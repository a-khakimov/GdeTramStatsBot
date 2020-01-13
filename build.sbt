import Dependencies._

ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "com.ainr"

lazy val root = (project in file("."))
  .settings(
    name := "GdeTramStatsBot",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.bot4s" %% "telegram-akka" % "4.4.0-RC2",
      // Start with this one
      libraryDependencies += "org.tpolecat" %% "doobie-core"      % "0.8.8",
      libraryDependencies += "org.tpolecat" %% "doobie-h2"        % "0.8.8",          // H2 driver 1.4.200 + type mappings.
      libraryDependencies += "org.tpolecat" %% "doobie-hikari"    % "0.8.8",          // HikariCP transactor.
      libraryDependencies += "org.tpolecat" %% "doobie-postgres"  % "0.8.8",          // Postgres driver 42.2.9 + type mappings.
      libraryDependencies += "org.tpolecat" %% "doobie-quill"     % "0.8.8",          // Support for Quill 3.4.10
      libraryDependencies += "org.tpolecat" %% "doobie-specs2"    % "0.8.8" % "test", // Specs2 support for typechecking statements.
      libraryDependencies += "org.tpolecat" %% "doobie-scalatest" % "0.8.8" % "test"  // ScalaTest support for typechecking statements.

)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
