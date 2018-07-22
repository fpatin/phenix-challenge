package com.github.fpatin.phenix

import com.github.fpatin.phenix.configuration._
import com.github.fpatin.phenix.job.{CleanWorkingDirectoryStep, Job}
import com.typesafe.scalalogging.StrictLogging

import scala.util.{Failure, Success, Try}

object Launcher extends App with StrictLogging {

  val start = System.currentTimeMillis()
  val configuration: JobConfiguration = JobConfiguration.load()
  logger.info(s"$configuration")
  val job = new Job(configuration)
  val cleanWorkingDirectoryStep = new CleanWorkingDirectoryStep(configuration)

  val results: Try[(Seq[Throwable], Unit)] = for {
    jobResult <- job.run()
    cleanResult <- cleanWorkingDirectoryStep.process()
  } yield (jobResult, cleanResult)

  results match {
    case Failure(exception) => logger.error("Job failed", exception)
    case Success((es, _)) if es.nonEmpty => es.foreach(exception => logger.error("Job failed", exception))
    case _ => logger.info("Job success")
  }
  logger.info(s"Duration:${(System.currentTimeMillis() - start) / 1000}")
}
