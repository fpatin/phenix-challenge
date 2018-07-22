import com.typesafe.sbt.packager.SettingsHelper._

name := "phenix-challenge"

version := "1.0"

scalaVersion := "2.12.6"

libraryDependencies := scalaLogging ++ betterFiles ++ pureConfig ++ scalatest

publishArtifact in(Compile, packageDoc) := false

publishArtifact in(Compile, packageSrc) := true

mainClass in Compile := Some("com.github.fpatin.phenix.Launcher")

javaOptions in Universal ++= Seq(
  "-J-Xmx512m",
  "-J-Xms256m",
)

publishTo := Some(Resolver.file("file", new File("app")))

makeDeploymentSettings(Universal, packageBin in Universal, "zip")

enablePlugins(JavaAppPackaging, UniversalDeployPlugin)