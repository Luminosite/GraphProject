import uk.gov.hmrc.gitstamp.GitStampPlugin._

// Your sbt build file. Guides on how to write one can be found at
// http://www.scala-sbt.org/0.13/docs/index.html

// Project name
name := "BSLOffline"

// Don't forget to set the version
version := "0.0.1-SNAPSHOT"

// Org
organization := "com.paypal.risk.grs"

// All Spark Packages need a license
licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0"))

// scala version to be used
scalaVersion := "2.11.6"

// spark version to be used
val sparkVersion = "1.6.0"

Seq( gitStampSettings: _* )

// Needed as SBT's classloader doesn't work well with Spark
fork := true
packAutoSettings
// BUG: unfortunately, it's not supported right now
fork in console := true

// Java version
javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

// add a JVM option to use when forking a JVM for 'run'
javaOptions ++= Seq("-Xmx2G")

// append -deprecation to the options passed to the Scala compiler
scalacOptions ++= Seq("-deprecation", "-unchecked")
net.virtualvoid.sbt.graph.Plugin.graphSettings

// Use local repositories by default
resolvers ++= Seq(
  Resolver.defaultLocal,
  Resolver.mavenLocal,
  // make sure default maven local repository is added... Resolver.mavenLocal has bugs.
  "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + "/.m2/repository",
  // For Typesafe goodies, if not available through maven
  // "Typesafe" at "http://repo.typesafe.com/typesafe/releases",
  // For Spark development versions, if you don't want to build spark yourself
  "Apache Staging" at "https://repository.apache.org/content/repositories/staging/",

  "PayPal Nexus releases" at "http://nexus.paypal.com/nexus/content/repositories/releases",
  "PayPal Nexus snapshots" at "http://nexus.paypal.com/nexus/content/repositories/snapshots"
  )


/// Dependencies
// copy all dependencies into lib_managed/
retrieveManaged := true

val sparkDependencyScope = "provided"

// spark modules (should be included by spark-sql, just an example)
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-graphx" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion % sparkDependencyScope,
  "org.apache.spark" %% "spark-hive" % sparkVersion % sparkDependencyScope
)
// logging
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"
libraryDependencies += "com.holdenkarau" %% "spark-testing-base" % "1.5.1_0.2.1" %"provided"
// testing
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.2" % "test"
libraryDependencies += "org.apache.pig" % "pig" % "0.14.0" excludeAll (ExclusionRule(organization = "org.mortbay.jetty"), ExclusionRule(organization="com.google.guava")) classifier "h2"
libraryDependencies += "com.github.scopt" %% "scopt" % "3.5.0"

checksums in update := Nil

/// Compiler plugins
// linter: static analysis for scala
resolvers += "Linter Repository" at "https://hairyfotr.github.io/linteRepo/releases"

coverageHighlighting := false

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.4"
libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "1.8.0" exclude(
  "com.google.guava", "guava")
packExcludeJars := Seq("jackson-.*\\.jar")

val sparkMode = sys.env.getOrElse("SPARK_MODE", "local[2]")

/// scaladoc
scalacOptions in (Compile,doc) ++= Seq("-groups", "-implicits",
  // NOTE: remember to change the JVM path that works on your system.
  // Current setting should work for JDK7 on OSX and Linux (Ubuntu)
  "-doc-external-doc:/Library/Java/JavaVirtualMachines/jdk1.7.0_60.jdk/Contents/Home/jre/lib/rt.jar#http://docs.oracle.com/javase/7/docs/api",
  "-doc-external-doc:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/rt.jar#http://docs.oracle.com/javase/7/docs/api"
  )

autoAPIMappings := true

/// Assembly
// skip test in assembly
test in assembly := {}

// do not include scala libraries
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

// do not include scapegoat jars
assemblyExcludedJars in assembly := {
  val cp = (fullClasspath in assembly).value
  cp filter { cp =>
    cp.data.getName.startsWith("scalac-scapegoat-plugin") || cp.data.getName.startsWith("scaldi")
  }
}
