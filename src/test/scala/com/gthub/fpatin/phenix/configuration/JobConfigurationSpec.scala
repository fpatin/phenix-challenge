package com.gthub.fpatin.phenix
package configuration

import java.time.{LocalDate, Month}

import better.files.File
import com.github.fpatin.phenix.configuration.JobConfiguration
import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, WordSpec}

class JobConfigurationSpec
  extends WordSpec
    with Matchers {

  "JobConfiguration" should {
    "parse a configuration" in {
      val config = ConfigFactory.parseString(
        """
          |{
          |  base-dir: "C:/phenix-challenge-master/"
          |  date-job: "20180722"
          |  input-dir-name: "inputDir"
          |  output-dir-name: "outputDir"
          |  delete-working-directory-on-exit: true
          |}
        """.stripMargin
      )
      val expected = JobConfiguration(File("C:/phenix-challenge-master/"), LocalDate.of(2018, Month.JULY, 22), "inputDir", "outputDir", deleteWorkingDirectoryOnExit = true)
      val result = JobConfiguration.load(config)
      result shouldBe expected
    }
  }

}
