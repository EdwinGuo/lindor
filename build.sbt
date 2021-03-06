import sbt.Keys._
import sbt.Project.projectToRef
import de.johoop.cpd4sbt.CopyPasteDetector._
import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "Knoldus-Scala.js"

version := "0.1"


lazy val clients = Seq(client)
lazy val scalaV = "2.11.7"
import sbt.Resolver

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  cpdSettings,
  scalaJSProjects := clients,
  pipelineStages := Seq(scalaJSProd, gzip),
  resolvers ++= Seq[sbt.Resolver]("scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
                     "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"),
  scalacOptions ++= Seq("-deprecation", "-Xlint","-feature"),
  libraryDependencies ++= Seq(
    "com.vmunier" %%% "play-scalajs-scripts" % "0.3.0",
    "org.scalaj" % "scalaj-http_2.11" % "2.3.0",
    "org.json4s" % "json4s-jackson_2.10" % "3.4.0",
    specs2 % Test
  )
).enablePlugins(PlayScala).
  aggregate(clients.map(projectToRef): _*).
  dependsOn(sharedJvm)

bootSnippet := "com.knoldus.weather.Weather().main();"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  cpdSettings,
  persistLauncher := true,
  persistLauncher in Test := false,
  scalacOptions ++= Seq("-deprecation", "-Xlint"),
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.0",
    "be.doeraene" %%% "scalajs-jquery" % "0.9.0",
    "com.lihaoyi" %%% "scalatags" % "0.5.4",
    "com.github.japgolly.scalacss" %%% "ext-scalatags" % "0.4.0",
    "org.datallite" % "scalajsgm" % "0.1",
    "com.github.japgolly.scalacss" %%% "core" % "0.4.0"
  ),

  jsDependencies ++= Seq(
    "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js",
    "org.webjars.npm" % "gmaps" % "0.4.24" / "0.4.24/gmaps.js"),

  jsDependencies += RuntimeDOM,

  // uTest settings
  libraryDependencies += "com.lihaoyi" %%% "utest" % "0.3.0" % "test",
  testFrameworks += new TestFramework("utest.runner.Framework")
).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs)



lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV).
  jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the Play project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
