import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / sbtVersion       := "1.2.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.msb"
ThisBuild / organizationName := "phenix"

lazy val root = (project in file("."))
  .settings(
    name := "phenix-challenge",
    libraryDependencies ++= Seq(
      betterFiles,
      scalaz,
      scallop,

      scalaLogging,
      slf4jBackend % Runtime,

      scalaTest % Test
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
