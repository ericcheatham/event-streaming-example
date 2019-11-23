scalaVersion := "2.13.1"
name := "streaming-example"
version := "1.0"

lazy val flyway = (project in file("models/flyway"))
    .enablePlugins(FlywayPlugin)

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5",
  "org.json4s" %% "json4s-native" % "3.6.7",
  "com.typesafe" % "config" % "1.4.0",
  "org.flywaydb" % "flyway-core" % "6.0.8",
  "com.lightbend.akka" %% "akka-stream-alpakka-amqp" % "1.1.2",
  "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "1.1.2",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  "com.twitter" %% "util-core" % "19.11.0"
)

flywayLocations := Seq("classpath:db/migration")