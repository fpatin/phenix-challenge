package com.github.fpatin.phenix
package job

import better.files.File
import com.github.fpatin.phenix.configuration.JobConfiguration
import com.github.fpatin.phenix.domain._
import com.typesafe.scalalogging.StrictLogging

import scala.util.{Failure, Try}

class Job(configuration: JobConfiguration) extends StrictLogging {

  private val partitionStep = new PartitionTransactionStep(configuration.input, configuration.rawDir)
  private val combineStep = new CombineStep(configuration.rawDir, configuration.combinedDir, configuration.input)
  private val globalStep = new GenerateGlobalStep(configuration.combinedDir, configuration.output).process(CombinedRecord.decoder, CombinedRecord.encoder)
  private val byShopStep = new GenerateByShopStep(configuration.combinedDir, configuration.output).process(CombinedRecord.decoder, CombinedRecord.encoder)

  private val period = rangeBefore(configuration.dateJob, 7)

  private val jobDateAsString = formatDate(configuration.dateJob, yyyyMMdd)

  private val jobDayFilter = (file: File) => file.name.contains(s"_${jobDateAsString}_")
  private val jobDayFilterJ7 = (file: File) => {
    rangeBefore(configuration.dateJob, 7).exists(d => file.name.contains(s"_${formatDate(d, yyyyMMdd)}_"))
  }

  private val globalStepData = Seq(
    (Filename(s"top_100_ventes_GLOBAL_$jobDateAsString.data"), CombinedRecord.orderingByQuantity, jobDayFilter),
    (Filename(s"top_100_ca_GLOBAL_$jobDateAsString.data"), CombinedRecord.orderingByPrice, jobDayFilter),
    (Filename(s"top_100_ventes_GLOBAL_$jobDateAsString-J7.data"), CombinedRecord.orderingByQuantity, jobDayFilterJ7),
    (Filename(s"top_100_ca_GLOBAL_$jobDateAsString-J7.data"), CombinedRecord.orderingByPrice, jobDayFilterJ7)
  )

  private val byShopStepData = Seq(
    ((shopId: ShopId) => Filename(s"top_100_ventes_${shopId.u}_$jobDateAsString.data"), CombinedRecord.orderingByQuantity, jobDayFilter),
    ((shopId: ShopId) => Filename(s"top_100_ca_${shopId.u}_$jobDateAsString.data"), CombinedRecord.orderingByPrice, jobDayFilter),
    ((shopId: ShopId) => Filename(s"top_100_ventes_${shopId.u}_$jobDateAsString-J7.data"), CombinedRecord.orderingByQuantity, jobDayFilterJ7),
    ((shopId: ShopId) => Filename(s"top_100_ca_${shopId.u}_$jobDateAsString-J7.data"), CombinedRecord.orderingByPrice, jobDayFilterJ7)
  )

  def run(): Try[Seq[Throwable]] = {
    logger.info("Start job")
    val partitionAndCombinedResultT = for {
      step1 <- partitionStep.process(configuration.dateJob, period, TransactionRecord.decoder(`yyyyMMdd'T'HHmmssZ`), TransactionRecord.encoder)
      step2 <- combineStep.process(TransactionRecord.decoder(yyyyMMdd), CombinedRecord.encoder)
    } yield (step1, step2)

    partitionAndCombinedResultT match {
      case Failure(e) => Failure(e)
      case _ =>
        val globalStepResult = globalStepData.map(globalStep.tupled.apply).collect { case f@Failure(_) => f.exception }
        val byShopStepResult = byShopStepData.map(byShopStep.tupled.apply).collect { case f@Failure(_) => f.exception }
        Try(globalStepResult ++ byShopStepResult)
    }
  }
}
