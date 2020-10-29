lazy val commonSettings: Seq[Setting[_]] = Seq(
  organization := "de.lolhens",
  version := "0.3.3-SNAPSHOT",

  licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0")),

  homepage := Some(url("https://github.com/LolHens/sbt-scalajs-webjar")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/LolHens/sbt-scalajs-webjar"),
      "scm:git@github.com:LolHens/sbt-scalajs-webjar.git"
    )
  ),
  developers := List(
    Developer(id = "LolHens", name = "Pierre Kisters", email = "pierrekisters@gmail.com", url = url("https://github.com/LolHens/"))
  ),


  scriptedLaunchOpts ++= Seq(
    "-Xmx1024M",
    "-Dplugin.version=" + version.value
  ),

  scriptedBufferLog := false,


  Compile / doc / sources := Seq.empty,

  version := {
    val tagPrefix = "refs/tags/"
    sys.env.get("CI_VERSION").filter(_.startsWith(tagPrefix)).map(_.drop(tagPrefix.length)).getOrElse(version.value)
  },

  publishMavenStyle := true,

  publishTo := sonatypePublishToBundle.value,

  credentials ++= (for {
    username <- sys.env.get("SONATYPE_USERNAME")
    password <- sys.env.get("SONATYPE_PASSWORD")
  } yield Credentials(
    "Sonatype Nexus Repository Manager",
    "oss.sonatype.org",
    username,
    password
  )).toList
)

lazy val root = project.in(file("."))
  .settings(commonSettings)
  .settings(
    publish / skip := true
  )
  .aggregate(
    `sbt-scalajs-webjar`,
    `sbt-scalajs-bundler-webjar`
  )

lazy val `sbt-scalajs-webjar` = project
  .enablePlugins(SbtPlugin)
  .settings(commonSettings)
  .settings(
    name := "sbt-scalajs-webjar",
    //logLevel := Level.Debug,

    addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.3.0")
  )

lazy val `sbt-scalajs-bundler-webjar` = project
  .enablePlugins(SbtPlugin)
  .dependsOn(`sbt-scalajs-webjar`)
  .settings(commonSettings)
  .settings(
    name := "sbt-scalajs-bundler-webjar",
    //logLevel := Level.Debug,

    addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.20.0")
  )
