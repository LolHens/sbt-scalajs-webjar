package de.lolhens.sbt.scalajs.webjar

import de.lolhens.sbt.scalajs.webjar.ScalaJSWebjarPlugin.stagedLinkJS
import de.lolhens.sbt.scalajs.webjar.WebjarPlugin.autoImport._
import sbt.Keys._
import sbt._
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object ScalaJSBundlerWebjarPlugin extends AutoPlugin {
  override def requires: Plugins = ScalaJSBundlerPlugin && WebjarPlugin

  override lazy val projectSettings: Seq[Setting[_]] =
    Seq(
      Compile / webjarMainResourceNames := Seq(stagedLinkJS(Compile / _ / artifactPath).value.name.stripSuffix(".js") + "-bundle.js"),

      Compile / webjarArtifacts := {
        val attributedFiles = stagedLinkJS(Compile / _ / webpack).value
        val target = (Compile / webjarArtifacts / crossTarget).value

        val artifacts = Seq(
          attributedFiles.find(_.metadata.get(BundlerFileTypeAttr).exists(_ == BundlerFileType.ApplicationBundle)).map(_.data),
          attributedFiles.find(_.metadata.get(BundlerFileTypeAttr).exists(_ == BundlerFileType.Asset)).map(_.data),
        )
          .flatMap(_.toList)
          .map(file => (file, target / file.name))

        IO.copy(artifacts)

        artifacts.map(_._2)
      }
    )
}
