import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := "Injector"

version := "0.1-SNAPSHOT"

javacOptions += "-g"

scalaVersion := "2.9.1"

autoScalaLibrary := false

seq(jacoco.settings : _*)

resolvers ++= Seq(
	"Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
	"Sonatype Scala-Tools"   at "https://oss.sonatype.org/content/groups/scala-tools/"
)

libraryDependencies ++= Seq(
    "org.mockito"           %       "mockito-all"                   %   "1.9.0"     %       "test",
    "junit"                 %       "junit"                         %   "4.8.1"     %       "test",
    "org.fluentlenium"      %       "fluentlenium-festassert"       %   "0.5.6"     %       "test",
    "com.novocode"          %       "junit-interface"               %   "0.8"       %       "test->default"
)
