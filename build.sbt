import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / sbtVersion       := "1.2.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.msb"
ThisBuild / organizationName := "carrefour"



lazy val root = (project in file("."))
  .settings(
    name := "phenix-challenge_Habib",
    libraryDependencies ++= Seq(
      betterFiles,
      scalaz,
      scallop,

      scalaLogging,
      slf4jBackend % Runtime,

      scalaTest % Test
    )
  )

