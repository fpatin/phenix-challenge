package com.github.fpatin.phenix
package job

import better.files._
import com.github.fpatin.phenix.domain.{CombinedRecord, Decoder, Encoder, Filename}
import com.typesafe.scalalogging.StrictLogging

import scala.util.Try


class GenerateGlobalStep(inputDir: File, outputDir: File) extends StrictLogging {
  def process: (Decoder[CombinedRecord], Encoder[CombinedRecord]) => (Filename, Ordering[CombinedRecord], File => Boolean) => Try[Unit] =
    (decoder, encoder) => (outputFile, ord, fileFilter) => Try {
      logger.info(s"Start GenerateGlobalStep (${outputFile.u})")
      val write: Stream[CombinedRecord] => Unit = Writer.write(outputDir, outputFile, encoder)

      implicit val ordering: Ordering[CombinedRecord] = ord
      write {
        inputDir.children.toStream.filter(fileFilter)
          .flatMap(file => file.lineIterator.toStream.map(decoder(_)))
          .sorted.reverse.take(100)
      }
    }
}

