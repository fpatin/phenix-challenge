package com.gthub.fpatin.phenix
package job


import better.files.File
import com.github.fpatin.phenix.configuration.JobConfiguration
import com.github.fpatin.phenix.job.Job
import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

class JobItSpec
  extends WordSpec
    with Matchers
    with BeforeAndAfter {

  val baseDirName = "baseDir"
  val baseDir = File(getClass.getResource(s"/$baseDirName")).toString().replace('\\', '/')
  val output = "output"
  val config: Config = ConfigFactory.parseString(
    s"""
       |{
       |  base-dir: "$baseDir/"
       |  date-job: "20170514"
       |  input-dir-name: "input"
       |  output-dir-name: "$output"
       |  delete-working-directory-on-exit: true
       |}
      """.stripMargin)
  val jobConfiguration: JobConfiguration = JobConfiguration.load(config)


  before {
    File(getClass.getResource(s"/$baseDirName/$output")).clear()
    jobConfiguration.rawDir.clear()
    jobConfiguration.combinedDir.clear()
  }

  after {
    File(getClass.getResource(s"/$baseDirName/$output")).clear()
  }


  "Job" should {
    "run" in {

      val expectedFiles = File(getClass.getResource("/expected")).children.toStream
      val expectedContent = expectedFiles.map(file => file.name -> file.lineIterator.toStream)
      val expectedFileNames = expectedFiles.map(_.name)

      val jobResult = new Job(jobConfiguration).run()
      jobResult.get shouldBe empty

      val resultFiles = jobConfiguration.output.children.toStream
      val resultFileNames = resultFiles.map(_.name)
      resultFileNames should contain theSameElementsAs expectedFileNames

      val resultContent = resultFiles.map(file => file.name -> file.lineIterator.toStream)
      resultContent.map { case (name, content) =>
        content should contain theSameElementsAs expectedContent.filter(_._1 == name).flatMap(_._2)
      }
    }
  }

}
