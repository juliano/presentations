organization := "br.com.lambda3"

name := "tdc"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
  "org.specs2" % "specs2_2.9.2" % "1.11" % "test",
  "org.scalatest" % "scalatest_2.9.2" % "2.0.M1",
  "junit" % "junit" % "4.10"
)

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked")
