name := "car-service"

lazy val commonSettings = Seq(
  organization := "com.cartracker",
  version := "1.0",
  scalaVersion := "2.11.5"
)

val akkaVersion = "2.5.11"
val akka = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
)

lazy val application = (project in file("application"))
  .settings(
    commonSettings,

    libraryDependencies ++= akka
  )