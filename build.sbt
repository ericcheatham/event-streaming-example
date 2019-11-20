// The simplest possible sbt build file is just one line:

scalaVersion := "2.13.1"
name := "streaming-example"
version := "1.0"

// Note, it's not required for you to define these three settings. These are
// mostly only necessary if you intend to publish your library's binaries on a
// place like Sonatype or Bintray.


// Want to use a published library in your project?
// You can define other libraries as dependencies in your build like this:
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.0.0",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5",
  "org.json4s" %% "json4s-native" % "3.6.7",
  "com.typesafe" % "config" % "1.4.0",
  "com.lightbend.akka" %% "akka-stream-alpakka-amqp" % "1.1.2",
  "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "1.1.2"
)
// Here, `libraryDependencies` is a set of dependencies, and by using `+=`,

// Here's a quick glimpse of what a multi-project build looks like for this
// build, with only one "subproject" defined, called `root`:

// lazy val root = (project in file(".")).
//   settings(
//     inThisBuild(List(
//       organization := "ch.epfl.scala",
//       scalaVersion := "2.13.1"
//     )),
//     name := "hello-world"
//   )
