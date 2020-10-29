package de.lolhens.sbt.scalajs.webjar

import sbt._

import scala.language.implicitConversions

class WebjarKeys {
  lazy val webjarArtifacts = taskKey[Seq[File]]("WebJar artifacts.")

  lazy val webjarResourcePath = settingKey[String]("Resource path of the webjar artifacts.")

  lazy val webjarMainResourceNames = settingKey[Seq[String]]("Resource name of the main webjar artifact.")

  lazy val webjarMainResources = settingKey[Seq[String]]("Resource path of the main webjar artifact.")

  implicit def webjarProject(project: Project): WebjarProject = new WebjarProject(project)
}
