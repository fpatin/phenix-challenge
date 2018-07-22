package com.github.fpatin.phenix
package job

import better.files._
import com.github.fpatin.phenix.domain._
import com.typesafe.scalalogging.StrictLogging

import scala.util.Try

class CombineStep(inputDir: File, outputDir: File, dataDir: File) extends StrictLogging {

  import CombineStep._

  def process(transactionRecordDecoder: Decoder[TransactionRecord], combinedRecordEncoder: Encoder[CombinedRecord]): Try[Unit] = Try {
    logger.info("Start CombineStep")
    val shops: Stream[(ShopFileKey, Stream[Shop])] = filterFiles(dataDir, file => file.name.startsWith(shopFilenamePrefix))
      .toStream
      .map { file =>
        val key = ShopFileKey.fromFilename(file)
        key -> file.lineIterator.toStream.map(str => Shop.decode(key.shopId, key.date)(str))
      }


    filterFiles(inputDir, TransactionRecord.filterFile).foreach { transactionRecordFile =>
      val salesByShopAndDateAndProduct = transactionRecordFile.lineIterator
        .map(transactionRecordDecoder)
        .foldLeft[CombinedRecord](CombinedRecord.zero)(combineRecords(shops))
      File(outputDir, CombinedRecord.salesByShopAndDateAndProductFilename(salesByShopAndDateAndProduct).u)
        .appendLine(combinedRecordEncoder(salesByShopAndDateAndProduct))
    }
  }

}

object CombineStep extends StrictLogging {

  import Shop._

  def combineRecords(shops: Stream[(ShopFileKey, Stream[Shop])])(cr: CombinedRecord, tr: TransactionRecord): CombinedRecord = {
    val priceO = for {
      filteredShops <- filterShop(shops, tr.shopId, tr.date)
      shop <- filteredShops.find(shop => shop.productId.u == tr.productId.u)
    } yield shop.price
    val price = priceO.getOrElse(Price.zero)

    cr match {
      case CombinedRecord.zero =>
        CombinedRecord(tr, tr.quantity, price * tr.quantity)
      case _ =>
        val q = tr.quantity + cr.quantity
        CombinedRecord(tr, q, price * q)
    }
  }
}
