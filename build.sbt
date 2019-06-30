val Scala212 = "2.12.8"
val Scala211 = "2.11.8"

lazy val baseSettings = Seq(
  homepage := Some(url("http://github.com/kiris/s2bot")),
  licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  organization := "s2bot",
  scalaVersion := Scala212,
  crossScalaVersions := Scala212 :: Scala211 :: Nil,
  version := (version in ThisBuild).value,
  scalacOptions := Seq(
    "-Xfatal-warnings",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:existentials",
    "-language:postfixOps"
  ),
  libraryDependencies := Seq(
    "org.typelevel" %% "cats-core" % "2.0.0-M1",
    "org.scalactic" %% "scalactic" % "3.0.1",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"

  ),
  parallelExecution in ThisBuild := false,
  publishArtifact in packageDoc := false,

  publishTo := Some(Resolver.file("file", file("repo")))
  // scalafmtOnCompile := true,
  // scalafmtVersion := "1.4.0"

)

lazy val modules: Seq[ProjectReference] = Seq(
  core,
  s2botCronExtension,
  s2botBrainExtension,
  s2botRedisBrainExtension,
  s2botChoicePlugin,
  s2botAmeshPlugin
)

lazy val `s2bot-root` = (project in file("."))
    .settings(baseSettings)
    .aggregate(modules: _*)

lazy val core = (project in file("core"))
    .settings(baseSettings)
    .settings(
      resolvers ++= Seq(
        "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/",
        "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
      ),
      libraryDependencies ++= Seq(
        "com.github.slack-scala-client" %% "slack-scala-client" % "0.2.6",

        "com.typesafe.akka" %% "akka-actor" % "2.4.14",
        "com.typesafe.akka" %% "akka-http-core" % "10.0.11"
      )
    )

lazy val s2botCronExtension = (project in file("extensions/cron"))
    .settings(baseSettings)
    .settings(
      resolvers ++= Seq(
        "cronish" at "https://kiris.github.io/cronish/repo/",
        "scalender" at "https://kiris.github.io/scalender/repo/",
      ),
      libraryDependencies ++= Seq(
        "com.github.philcali" %% "cronish" % "0.1.5"
      )
    )
    .dependsOn(
      core % "test->test;compile->compile"
    )

lazy val s2botBrainExtension = (project in file("extensions/brain"))
    .settings(baseSettings)
    .dependsOn(
      core % "test->test;compile->compile"
    )

lazy val s2botRedisBrainExtension = (project in file("extensions/redis-brain"))
    .settings(baseSettings)
    .settings(
      libraryDependencies ++= Seq(
        "com.github.etaty" %% "rediscala" % "1.8.0"
      )
    )
    .dependsOn(
      s2botBrainExtension % "test->test;compile->compile"
    )

lazy val s2botChoicePlugin = (project in file("plugins/choice"))
    .settings(baseSettings)
    .dependsOn(
      core % "test->test;compile->compile"
    )

lazy val s2botAmeshPlugin = (project in file("plugins/amesh"))
    .settings(baseSettings)
    .settings(
      libraryDependencies ++= Seq(
        "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.8",
        "com.sksamuel.scrimage" %% "scrimage-io-extra" % "2.1.8",
        "com.sksamuel.scrimage" %% "scrimage-filters" % "2.1.8",
        "net.databinder.dispatch" %% "dispatch-core" % "0.13.3"
      )
    )
    .dependsOn(
      core % "test->test;compile->compile"
    )
