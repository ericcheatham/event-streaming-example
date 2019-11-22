scalaVersion := "2.13.1"
name := "streaming-example"
version := "1.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.0.0",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5",
  "org.json4s" %% "json4s-native" % "3.6.7",
  "com.typesafe" % "config" % "1.4.0",
  "com.lightbend.akka" %% "akka-stream-alpakka-amqp" % "1.1.2",
  "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "1.1.2",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42"
)

