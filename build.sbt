name                     := "java_cli"
ThisBuild / organization := "com.github.inkspect"
ThisBuild / scalaVersion := "3.6.4"

// parsed by project/Versions.scala, updated by updateDependencies.sh
val cpgVersion       = "1.7.44"
val joernVersion     = "4.0.397"
val flatgraphVersion = "0.1.24"

lazy val schema         = Projects.schema
lazy val domainClasses  = Projects.domainClasses
lazy val schemaExtender = Projects.schemaExtender

dependsOn(domainClasses)

libraryDependencies ++= Seq(
  "com.github.scopt"        %% "scopt"             % Versions.scopt,
  "org.apache.logging.log4j" % "log4j-slf4j2-impl" % Versions.log4j     % Optional,
  "io.joern"                %% "console"           % Versions.joern,
  "io.joern"                %% "x2cpg"             % Versions.joern,
  "io.joern"                %% "javasrc2cpg"       % Versions.joern,
  "io.joern"                %% "joern-cli"         % Versions.joern,
  "io.joern"                %% "semanticcpg"       % Versions.joern,
  "io.joern"                %% "semanticcpg"       % Versions.joern     % Test classifier "tests",
  "org.scalatest"           %% "scalatest"         % Versions.scalatest % Test
)

// mostly so that `sbt assembly` works, but also to ensure that we don't end up
// with unexpected shadowing in jar hell
excludeDependencies ++= Seq(
  ExclusionRule("io.shiftleft", "codepropertygraph-domain-classes_3"),
  ExclusionRule("org.wildfly.common", "wildfly-common")
)

assembly / assemblyMergeStrategy := {
  case "log4j2.xml"                                             => MergeStrategy.first
  case "module-info.class"                                      => MergeStrategy.first
  case "META-INF/versions/9/module-info.class"                  => MergeStrategy.first
  case "io/github/retronym/java9rtexport/Export.class"          => MergeStrategy.first
  case PathList("scala", "collection", "internal", "pprint", _) => MergeStrategy.first
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}

ThisBuild / Compile / scalacOptions ++= Seq("-feature", "-deprecation", "-language:implicitConversions")

enablePlugins(
  JavaAppPackaging,
  GraalVMNativeImagePlugin
)

//graalVMNativeImageGraalVersion := Some("24.0.2")
containerBuildImage := Some("ghcr.io/graalvm/jdk-community:24.0.2")

graalVMNativeImageOptions := Seq(
  "-H:+UnlockExperimentalVMOptions",
  "-R:MaximumHeapSizePercent=90",
  "--gc=epsilon",
  "--initialize-at-build-time=io.appthreat.*",
  "--no-fallback",
  "--exclude-config", ".*wildfly.*", ".*substitutions.*",
  "--allow-incomplete-classpath",
  "--report-unsupported-elements-at-runtime"
)

ThisBuild / licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / resolvers ++= Seq(
  Resolver.mavenLocal,
  "Sonatype OSS" at "https://oss.sonatype.org/content/repositories/public",
  "Atlassian" at "https://packages.atlassian.com/mvn/maven-atlassian-external",
  "Gradle Releases" at "https://repo.gradle.org/gradle/libs-releases/"
)

Compile / mainClass := Some("com.github.inkspect.java_cli.Main")
Compile / doc / sources                := Seq.empty
Compile / packageDoc / publishArtifact := false
