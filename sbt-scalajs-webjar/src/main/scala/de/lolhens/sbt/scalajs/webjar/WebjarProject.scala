package de.lolhens.sbt.scalajs.webjar

import de.lolhens.sbt.scalajs.webjar.WebjarPlugin.autoImport._
import sbt.Keys._
import sbt._

import scala.language.implicitConversions

class WebjarProject(val self: Project) extends AnyVal {
  def webjar: Project = {
    Project(self.id + "-webjar", self.base.toPath.resolve(".webjar").toFile)
      .settings(
        name := (self / name).value + "-webjar",
        normalizedName := (self / normalizedName).value + "-webjar",
        version := (self / version).value,

        scalaVersion := (self / scalaVersion).value,

        watchSources := (self / watchSources).value,

        self / Compile / webjarResourcePath := s"META-INF/resources/webjars/${(self / normalizedName).value}/${(self / version).value}",

        self / Compile / webjarMainResources := {
          val resourcePath = (self / Compile / webjarResourcePath).value
          (self / Compile / webjarMainResourceNames).value.map(resourcePath + "/" + _)
        },

        self / Compile / webjarArtifacts / crossTarget := {
          (Compile / classDirectory).value.toPath.resolve((self / Compile / webjarResourcePath).value).toFile
        },

        Compile / compile := (Compile / compile).dependsOn(self / Compile / webjarArtifacts).value,

        Compile / packageBin / mappings := {
          val artifacts = (self / Compile / webjarArtifacts).value
          (Compile / packageBin / mappings).value.filter(mapping => artifacts.contains(mapping._1))
        }
      )
  }
}
