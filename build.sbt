name := "SlackCont"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.github.gilbertw1" %% "slack-scala-client" % "0.2.3",
  "org.scalaz" %% "scalaz-core" % "7.2.24",
  "com.typesafe.play" %% "play-guice" % "2.6.15" exclude("com.typesafe.play", "play-json"),
  "com.typesafe" % "config" % "1.3.2"
)
