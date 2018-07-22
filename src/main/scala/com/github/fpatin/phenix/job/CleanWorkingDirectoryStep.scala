package com.github.fpatin.phenix.job

import com.github.fpatin.phenix.configuration.JobConfiguration
import com.typesafe.scalalogging.StrictLogging

import scala.util.Try

class CleanWorkingDirectoryStep(configuration: JobConfiguration) extends StrictLogging {

  def process(): Try[Unit] = Try {
    if (configuration.deleteWorkingDirectoryOnExit) {
      logger.info("Delete working files and directories...")
      configuration.workingDirectory.clear().delete()
    }
  }

}
