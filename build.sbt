name := "SlackCont"

version := "0.1"

organization := "com.github.yyu"

scalaVersion := "2.12.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-language:postfixOps")

scalapropsWithScalazlaws

scalapropsVersion := "0.5.5"

coverageExcludedPackages := ".*di;.*provider;.*main"

libraryDependencies ++= Seq(
  "com.github.gilbertw1" %% "slack-scala-client" % "0.2.3",
  "org.scalaz" %% "scalaz-core" % "7.2.24",
  "com.google.inject" % "guice" % "4.2.0",
  "com.typesafe" % "config" % "1.3.2",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.mockito" % "mockito-core" % "2.18.3" % "test"
)
