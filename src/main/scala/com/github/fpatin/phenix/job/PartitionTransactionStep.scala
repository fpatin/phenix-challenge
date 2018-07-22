package com.github.fpatin.phenix
package job

import java.io.BufferedWriter
import java.time.LocalDate

import better.files.File.OpenOptions
import better.files._
import com.github.fpatin.phenix.domain.TransactionRecord._
import com.github.fpatin.phenix.domain._
import com.typesafe.scalalogging.StrictLogging

import scala.util.Try

class PartitionTransactionStep(input: File, output: File) extends StrictLogging {

  import PartitionTransactionStep._

  def process(date: LocalDate, period: Seq[LocalDate], trDecoder: Decoder[TransactionRecord], trEncoder: Encoder[TransactionRecord]): Try[Unit] = Try {
    logger.info(s"Start PartitionTransactionStep for period $period")
    period.map(date => File(input, transactionsFileName(date).u))
      .filter(_.exists())
      .foreach { transactionsFile =>
        logger.info(s"Process file $transactionsFile")
        transactionsFile.lineIterator
          .map(trDecoder)
          .filter(tr => period.contains(tr.date))
          .foldLeft(PartitionWriter(output, trEncoder))((pw, t) => pw.handle(t))
          .close()

      }
  }
}

object PartitionTransactionStep {
  def transactionsFileName(date: LocalDate): Filename = Filename(s"$transactionFilenamePrefix${date.format(`yyyyMMdd`)}$transactionFilenameExtension")
}

case class PartitionWriter(directory: File, trEncoder: Encoder[TransactionRecord], cache: Map[Filename, BufferedWriter]) {

  def handle(transaction: TransactionRecord): PartitionWriter = {
    val key = transactionRecordFilename(transaction)
    val (filename, bw) = getOrCreateWriter(key)
    Writer.write(transaction, trEncoder, bw)
    PartitionWriter(directory, trEncoder, updateCacheIfNeed(cache, filename, bw))
  }

  private def getOrCreateWriter(filename: Filename) = {
    filename -> cache.getOrElse(filename, createBufferedWriter(filename))
  }

  private def updateCacheIfNeed(cache: Map[Filename, BufferedWriter], filename: Filename, bw: => BufferedWriter): Map[Filename, BufferedWriter] = {
    cache.get(filename).fold(cache + (filename -> bw))(_ => cache)
  }

  private def createBufferedWriter(filename: Filename) = {
    implicit val openOptions: OpenOptions = OpenOptions.append
    val file = file"$directory/${filename.u}"
    file.newBufferedWriter
  }

  def close(): Unit = cache.foreach { case (_, bw) => bw.close() }

}

object PartitionWriter {
  def apply(directory: File, trEncoder: Encoder[TransactionRecord]): PartitionWriter = new PartitionWriter(directory, trEncoder, Map.empty)
}