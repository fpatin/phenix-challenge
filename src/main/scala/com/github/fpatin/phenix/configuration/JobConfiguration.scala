package com.github.fpatin.phenix.configuration

import java.time.LocalDate

import better.files.{File, _}
import com.typesafe.config.{Config, ConfigFactory}

case class JobConfiguration(baseDir: File,
                            dateJob: LocalDate = LocalDate.now(),
                            inputDirName: String = "data",
                            outputDirName: String = "output",
                            deleteWorkingDirectoryOnExit: Boolean) {

  val input: File = file"$baseDir/$inputDirName".createIfNotExists(asDirectory = true, createParents = true)
  val workingDirectory: File = {
    file"$baseDir/work".createIfNotExists(asDirectory = true, createParents = true)
  }
  val rawDir: File = File(workingDirectory, "raw").createIfNotExists(asDirectory = true, createParents = true)
  val combinedDir: File = File(workingDirectory, "combined").createIfNotExists(asDirectory = true, createParents = true)
  val output: File = file"$baseDir/$outputDirName".createIfNotExists(asDirectory = true, createParents = true)

}

object JobConfiguration {
  def load(config: Config = ConfigFactory.load()): JobConfiguration = pureconfig.loadConfigOrThrow[JobConfiguration](config)
}
