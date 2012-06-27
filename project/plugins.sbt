libraryDependencies ++= Seq(
  "org.jacoco" % "org.jacoco.core" % "0.5.7.201204190339" artifacts(Artifact("org.jacoco.core", "jar", "jar")),
  "org.jacoco" % "org.jacoco.report" % "0.5.7.201204190339" artifacts(Artifact("org.jacoco.report", "jar", "jar")))

addSbtPlugin("de.johoop" % "jacoco4sbt" % "1.2.2")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.0.0")
