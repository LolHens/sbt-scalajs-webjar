package de.lolhens.scalajs.webjar

import sbt._

import scala.language.implicitConversions

object WebjarPlugin extends AutoPlugin {

  object autoImport extends WebjarProjectOps {
    lazy val webjar = taskKey[Unit]("")

    lazy val webjarMappings = taskKey[Seq[(File, String)]]("Defines the mappings from a file to a path, used by packaging the WebJar.")
  }

  import autoImport._

  override def derivedProjects(proj: ProjectDefinition[_]): Seq[Project] = proj match {
    case project: Project =>
      Seq(project.webjar)

    case _ => Seq.empty
  }

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    webjarMappings := Seq.empty
  )
}
