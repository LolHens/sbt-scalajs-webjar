lazy val root = project.in(file(".")).settings(
  scalaVersion := "2.13.1"
)
  .aggregate(backend, frontend.webjar)

lazy val frontend = project
  .enablePlugins(
    ScalaJSBundlerWebjarPlugin
  )
  .settings(
    name := "frontend",

    scalaVersion := "2.13.1",

    scalaJSUseMainModuleInitializer := true,

    Compile / npmDependencies ++= Seq(
      "react" -> "16.13.1",
      "react-dom" -> "16.13.1"
    ),

    Compile / fastOptJS / webpack := {
      val v = (Compile / fastOptJS / webpack).value
      println(v.seq.map(_.metadata.entries.toList).mkString("\n"))
      v
    }
  )

lazy val backend = project
  .settings(
    name := "backend",

    scalaVersion := "2.13.1",

    Compile / compile := {
      println((frontend / Compile / webjarMainResources).value)

      (Compile / compile).value
    }
  )
  .dependsOn(frontend.webjar)
