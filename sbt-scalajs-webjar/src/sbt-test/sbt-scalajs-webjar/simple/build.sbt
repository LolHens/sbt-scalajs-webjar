lazy val root = project.in(file(".")).settings(
  scalaVersion := "2.13.1"
)
  .aggregate(backend, frontend.webjar)

lazy val frontend = project
  .enablePlugins(
    ScalaJSWebjarPlugin
  )
  .settings(
    name := "frontend",

    scalaVersion := "2.13.1",

    scalaJSUseMainModuleInitializer := true
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
