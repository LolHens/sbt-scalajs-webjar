package de.lolhens.sbt.scalajs.webjar

import de.lolhens.sbt.scalajs.webjar.WebjarPlugin.autoImport._
import org.scalajs.linker.interface.Report
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.scalajs.sbtplugin.{ScalaJSPlugin, Stage}
import sbt.Keys._
import sbt._

object ScalaJSWebjarPlugin extends AutoPlugin {
  override def requires: Plugins = ScalaJSPlugin && WebjarPlugin

  private[webjar] def stagedLinkJS[A](f: TaskKey[Attributed[Report]] => Def.Initialize[A]): Def.Initialize[A] =
    Def.settingDyn {
      scalaJSStage.value match {
        case Stage.FastOpt => f(fastLinkJS)
        case Stage.FullOpt => f(fullLinkJS)
      }
    }

  override lazy val projectSettings: Seq[Setting[_]] =
    List(fastLinkJS, fullLinkJS).map { stagedOptJS =>
      Compile / stagedOptJS / crossTarget := (Compile / webjarArtifacts / crossTarget).value
    } ++ Seq(
      Compile / webjarMainResourceNames := {
        /*val attributedReport: Attributed[Report] = (Compile / scalaJSLinkerResult).value
        val moduleInitializers = (Compile / scalaJSModuleInitializers).value
        val report = attributedReport.data
        report.publicModules.iterator
          .filter(module => moduleInitializers.exists(_.moduleID == module.moduleID))
          .map(_.jsFileName)
          .toSeq*/
        Seq(stagedLinkJS(Compile / _ / artifactPath).value.name)
      },

      Compile / webjarArtifacts := {
        val attributedReport: Attributed[Report] = (Compile / scalaJSLinkerResult).value
        val report = attributedReport.data
        val outputDirectory = attributedReport.metadata.get(scalaJSLinkerOutputDirectory.key).get

        val files = report.publicModules.iterator
          .flatMap { module =>
            println(module)
            module.jsFileName +:
              module.sourceMapName.toList
          }.map(new File(outputDirectory, _))
          .toSeq

        files.foreach(println) // TODO: debug

        files
      }
    )
}
