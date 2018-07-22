import sbt._
import sbt.librarymanagement.ModuleID

trait Versions {
  val betterFilesVersion = "3.5.0"
  val pureConfigVersion = "0.9.1"
  val scalacticVersion = "3.0.5"
  val scalatestVersion = "3.0.5"
  val scalacheckVersion = "1.14.0"
  val scalaLoggingVersion = "3.9.0"
  val logbackVersion = "1.2.3"
}

object DependencyPlugin extends AutoPlugin with Versions {

  object autoImport {

    val betterFiles: Seq[ModuleID] = Seq(
      "com.github.pathikrit" %% "better-files" % betterFilesVersion
    )

    val pureConfig = Seq(
      "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
    )

    val scalactic = Seq {
      "org.scalactic" %% "scalactic" % scalacticVersion
    }

    val scalatest: Seq[ModuleID] = Seq(
      "org.scalatest" %% "scalatest" % scalatestVersion % Test
    )

    val scalacheck: Seq[ModuleID] = Seq(
      "org.scalacheck" %% "scalacheck" % scalacheckVersion % Test
    )

    val scalaLogging: Seq[ModuleID] = Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
      "ch.qos.logback" % "logback-classic" % logbackVersion
    )
  }

}

