package com.github.fpatin.phenix.job

import better.files.File
import com.github.fpatin.phenix.domain._
import com.typesafe.scalalogging.StrictLogging

import scala.util.Try

class GenerateByShopStep(inputDir: File, outputDir: File) extends StrictLogging {
  def process: (Decoder[CombinedRecord], Encoder[CombinedRecord]) => (ShopId => Filename, Ordering[CombinedRecord], File => Boolean) => Try[Unit] =
    (decoder, encoder) => (outputFile, ord, fileFilter) => Try {
      logger.info("Start GenerateByShopStep")
      implicit val ordering: Ordering[CombinedRecord] = ord
      inputDir.children.toStream.filter(fileFilter).groupBy(f => ShopId(f.name.substring(0, f.name.indexOf('_'))))
        .toStream
        .foreach { case (shopId, files) =>
          val outputFilename = outputFile(shopId)
          logger.info(s"Generate file $outputFilename")
          val write: Stream[CombinedRecord] => Unit = Writer.write(outputDir, outputFilename, encoder)
          write {
            files
              .flatMap(file => file.lineIterator.toStream.map(decoder(_)))
              .sorted.reverse.take(100)
          }
        }
    }
}

