import de.johoop.testngplugin.TestNGPlugin._
import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := "Injector"

version := "0.1.0"

organization := "com.communalizer"

javacOptions += "-g"

scalaVersion := "2.10.2"

autoScalaLibrary := false

jacoco.settings

testNGSettings

testNGSuites := Seq("src/test/resources/testng.xml")

resolvers ++= Seq(
	"Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
	"Sonatype Scala-Tools"   at "https://oss.sonatype.org/content/groups/scala-tools/"
)

libraryDependencies ++= Seq(
    "org.mockito"           %       "mockito-all"                   %   "1.9.0"     %       "test",
    "org.testng"            %       "testng"                        %   "6.8.5"     %       "test",
    "org.fluentlenium"      %       "fluentlenium-festassert"       %   "0.5.6"     %       "test"
)

libraryDependencies ++= Seq(
  "org.core4j" % "core4j" % "0.6",
  "com.thoughtworks.paranamer" % "paranamer" % "2.6"
)

jacoco.reportFormats in jacoco.Config := Seq(XMLReport("utf-8"), HTMLReport("utf-8"))

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
