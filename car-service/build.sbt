name := "car-service"

lazy val commonSettings = Seq(
  organization := "com.cartracker",
  version := "1.0",
  scalaVersion := "2.11.5"
)

val akka = Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.11" % Test
)

val scalaTest = Seq(
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

lazy val core = (project in file("core"))
  .settings(commonSettings)

lazy val application = (project in file("application"))
  .settings(
    commonSettings,

    libraryDependencies ++= akka ++ scalaTest
  ).dependsOn(core)