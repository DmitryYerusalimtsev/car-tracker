name := "car-service"

lazy val commonSettings = Seq(
  organization := "com.cartracker",
  version := "1.0",
  scalaVersion := "2.11.5"
)

val akka = Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.11",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.11" % Test
)

val ignite = Seq(
  "org.apache.ignite" % "ignite-core" % "2.4.0",
  "org.apache.ignite" % "ignite-spring" % "2.4.0",
  "org.apache.ignite" % "ignite-indexing" % "2.4.0"
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

    libraryDependencies ++= akka ++ scalaTest ++ ignite
  ).dependsOn(core)

lazy val api = (project in file("api"))
  .settings(
    commonSettings,

    libraryDependencies ++= akka ++ ignite ++ Seq(
      "com.typesafe.akka" %% "akka-http" % "10.0.10",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10",
      "com.typesafe" % "config" % "1.3.1",
      "org.apache.commons" % "commons-lang3" % "3.7"
    )
  ).dependsOn(core, application)
