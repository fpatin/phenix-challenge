package com.gthub.fpatin.phenix
package domain


import java.time.{LocalDate, Month}

import better.files.File
import com.github.fpatin.phenix._
import com.github.fpatin.phenix.domain._
import org.scalatest.{Matchers, WordSpec}

class TransactionRecordSpec extends WordSpec with Matchers {
  "TransactionRecord" should {
    """decode a String using "yyyyMmdd" formatter """ in {

      val transactionId = TransactionId("1")
      val date = LocalDate.of(2018, Month.JULY, 22)
      val shopId = ShopId("1")
      val productId = new ProductId(12)
      val quantity = Quantity(1L)

      val line = "1|20180722|1|12|1"

      val expected = TransactionRecord(transactionId, date, shopId, productId, quantity)
      val result = TransactionRecord.decoder(yyyyMMdd)(line)

      result shouldBe expected
    }

    """decode a String using "yyyyMMdd'T'HHmmssZ" formatter """ in {
      val transactionId = TransactionId("1")
      val date = LocalDate.of(2018, Month.JULY, 22)
      val shopId = ShopId("1")
      val productId = new ProductId(12)
      val quantity = Quantity(1L)

      val line = "1|20180722T121212+0100|1|12|1"

      val expected = TransactionRecord(transactionId, date, shopId, productId, quantity)
      val result = TransactionRecord.decoder(`yyyyMMdd'T'HHmmssZ`)(line)

      result shouldBe expected
    }
    "encode to String" in {
      val transactionId = TransactionId("1")
      val date = LocalDate.of(2018, Month.JULY, 22)
      val shopId = ShopId("1")
      val productId = new ProductId(12)
      val quantity = Quantity(1L)

      val expected = "1|20180722|1|12|1"


      val result = TransactionRecord.encoder(TransactionRecord(transactionId, date, shopId, productId, quantity))
      result shouldBe expected
    }

    "generate a filename" in {
      val transactionId = TransactionId("1")
      val date = LocalDate.of(2018, Month.JULY, 22)
      val shopId = ShopId("1")
      val productId = new ProductId(12)
      val quantity = Quantity(1L)

      val expected = Filename("1_20180722_12.raw")

      val result = TransactionRecord.transactionRecordFilename(TransactionRecord(transactionId, date, shopId, productId, quantity))

      result shouldBe expected
    }

    """filter a file endding by ".raw" """ in {
      val result = TransactionRecord.filterFile(File("test.raw"))
      result shouldBe true
    }
  }
}
