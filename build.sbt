import uk.gov.hmrc.gitstamp.GitStampPlugin._

// Your sbt build file. Guides on how to write one can be found at
// http://www.scala-sbt.org/0.13/docs/index.html

// Project name
name := "BSLOffline"

// Don't forget to set the version
version := "2.0.12-SNAPSHOT"

// Org
organization := "com.paypal.risk.grs"

// All Spark Packages need a license
licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0"))

// scala version to be used
scalaVersion := "2.11.6"
// force scalaVersion
//ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

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

// scala modules (should be included by spark, just an exmaple)
//libraryDependencies ++= Seq(
//  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
//  "org.scala-lang" % "scala-compiler" % scalaVersion.value
//  )

val sparkDependencyScope = "provided"
// spark modules (should be included by spark-sql, just an example)
libraryDependencies ++= Seq(
/*
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-hive" % sparkVersion
  */
//"org.apache.spark" %% "spark-core" % sparkVersion % sparkDependencyScope,
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-graphx" % sparkVersion,
"org.apache.spark" %% "spark-sql" % sparkVersion % sparkDependencyScope,
"org.apache.spark" %% "spark-hive" % sparkVersion % sparkDependencyScope

  /* ,
  "org.apache.spark" %% "spark-streaming" % sparkVersion ,
  "org.apache.spark" %% "spark-streaming-kafka" % sparkVersion*/
)
/*
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.8.2.0"
*/

// graphx
//libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.1.0"
// logging
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"
libraryDependencies += "com.holdenkarau" %% "spark-testing-base" % "1.5.1_0.2.1" %"provided"
//libraryDependencies += "org.clapper" % "grizzled-slf4j_2.11" % "1.0.2"

// testing
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

//libraryDependencies += "com.google.guava" % "guava" % "14.0.1" force()

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.2" % "test"
libraryDependencies += "org.apache.pig" % "pig" % "0.14.0" excludeAll (ExclusionRule(organization = "org.mortbay.jetty"), ExclusionRule(organization="com.google.guava")) classifier "h2"
libraryDependencies += "com.github.scopt" %% "scopt" % "3.5.0"

checksums in update := Nil
/// Compiler plugins

// linter: static analysis for scala
resolvers += "Linter Repository" at "https://hairyfotr.github.io/linteRepo/releases"

coverageHighlighting := false

//addCompilerPlugin("com.foursquare.lint" %% "linter" % "0.1.8")

/*val commonVoVersion = "2.91"
val paypalInfraVersion = "13.4.3"
libraryDependencies ++= Seq(
  "com.paypal.risk.idi" % "common-vo" % commonVoVersion  exclude(
    "com.paypal.infra","infra"
    //ExclusionRule(organization = "org.apache")
    )/* exclude ("com.paypal.infra", "infra-core")*/,
  "com.paypal.infra" % "infra-core" % paypalInfraVersion
)*/

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"
//libraryDependencies += "com.paypal.risk.grs" %% "spark-pig-loader" % "0.1.3-SNAPSHOT"
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.4"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "1.8.0" exclude(
  "com.google.guava", "guava"
  )

//coverageEnabled := true

/*
libraryDependencies ++= Seq(
  "org.codehaus.jackson" % "jackson-core-asl" % "1.9.13" force(),
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.13" force()
)
*/

packExcludeJars := Seq("jackson-.*\\.jar")

/*libraryDependencies ++= Seq("com.paypal.infra" % "infra-core" % "13.4.3"//,
/*"com.paypal.infra" % "infra" % "13.4.3",*/
/*"org.slf4j"%"slf4j-api"%"1.7.10" *//*,
"org.slf4j"%"slf4j-log4j12"%"1.6.4" force()*/)*/

//libraryDependencies ++= Seq("com.paypal.risk.ars.linking" % "lion.normalizers" % "3.2.0")

/// console

// define the statements initially evaluated when entering 'console', 'consoleQuick', or 'consoleProject'
// but still keep the console settings in the sbt-spark-package plugin

// If you want to use yarn-client for spark cluster mode, override the environment variable
// SPARK_MODE=yarn-client <cmd>
val sparkMode = sys.env.getOrElse("SPARK_MODE", "local[2]")

/*

initialCommands in console :=
  s"""
    |import org.apache.spark.SparkConf
    |import org.apache.spark.SparkContext
    |import org.apache.spark.SparkContext._
    |
    |@transient val sc = new SparkContext(
    |  new SparkConf()
    |    .setMaster("$sparkMode")
    |    .setAppName("Console test"))
    |implicit def sparkContext = sc
    |import sc._
    |
    |@transient val sqlc = new org.apache.spark.sql.SQLContext(sc)
    |implicit def sqlContext = sqlc
    |import sqlc._
    |
    |def time[T](f: => T): T = {
    |  import System.{currentTimeMillis => now}
    |  val start = now
    |  try { f } finally { println("Elapsed: " + (now - start)/1000.0 + " s") }
    |}
    |
    |""".stripMargin

cleanupCommands in console :=
  s"""
     |sc.stop()
   """.stripMargin

*/

/// scaladoc
scalacOptions in (Compile,doc) ++= Seq("-groups", "-implicits",
  // NOTE: remember to change the JVM path that works on your system.
  // Current setting should work for JDK7 on OSX and Linux (Ubuntu)
  "-doc-external-doc:/Library/Java/JavaVirtualMachines/jdk1.7.0_60.jdk/Contents/Home/jre/lib/rt.jar#http://docs.oracle.com/javase/7/docs/api",
  "-doc-external-doc:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/rt.jar#http://docs.oracle.com/javase/7/docs/api"
  )

autoAPIMappings := true


/// publishing
publishMavenStyle := true

publishTo := {
  val nexus = "http://nexus.paypal.com/nexus/content/repositories/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "snapshots")
  else
    Some("releases"  at nexus + "releases")
}

// http://www.scala-sbt.org/0.13.5/docs/Detailed-Topics/Publishing.html#credentials
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

pomExtra := (
  <url>https://github.paypal.com/RiskDataScience/NRTLinking</url>
    <scm>
      <url>git@github.paypal.com:RiskDataScience/NRTLinking.git</url>
      <connection>scm:git@github.paypal.com:RiskDataScience/NRTLinking.git</connection>
    </scm>
    <developers>
      <developer>
        <id>chufang</id>
        <name>Mike Fang</name>
        <url>https://github.paypal.com/chufang</url>
      </developer>
    </developers>)


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

// publishing the fat jar
// artifact in (Compile, assembly) := {
//   val art = (artifact in (Compile, assembly)).value
//   art.copy(`classifier` = Some("assembly"))
// }

// addArtifact(artifact in (Compile, assembly), assembly)
