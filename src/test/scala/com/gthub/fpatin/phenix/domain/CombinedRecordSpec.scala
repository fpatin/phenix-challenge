package com.gthub.fpatin.phenix
package domain

import java.time.{LocalDate, Month}

import better.files.File
import com.github.fpatin.phenix.domain.{CombinedRecord, _}
import org.scalatest.{Matchers, WordSpec}

class CombinedRecordSpec
  extends WordSpec
    with Matchers {
  "CombinedRecord" should {
    "decode a String" in {

      val date = LocalDate.of(2018, Month.JULY, 22)
      val shopId = ShopId("1")
      val productId = new ProductId(12)
      val quantity = Quantity(1L)
      val price = new Price(BigDecimal(42))

      val line = "20180722|1|12|1|42"

      val expected = CombinedRecord(date, shopId, productId, quantity, price)
      val result = CombinedRecord.decoder(line)

      result shouldBe expected
    }
    "encode to String" in {
      val date = LocalDate.of(2018, Month.JULY, 22)
      val shopId = ShopId("1")
      val productId = new ProductId(12)
      val quantity = Quantity(1L)
      val price = new Price(BigDecimal(42))

      val expected = "20180722|1|12|1|42"


      val result = CombinedRecord.encoder(CombinedRecord(date, shopId, productId, quantity, price))
      result shouldBe expected
    }

    "create a CombinedRecord from a Transaction, a Price and a Quantity record" in {
      val date = LocalDate.of(2018, Month.JULY, 22)
      val shopId = ShopId("shop#1")
      val productId = ProductId("3")
      val transactionRecord = TransactionRecord(TransactionId("1"), date, shopId, productId, Quantity(1L))
      val quantity = Quantity(12L)
      val price = Price("42")

      val expected = CombinedRecord(date, shopId, productId, quantity, price)
      val result = CombinedRecord(transactionRecord, quantity, price)
      result shouldBe expected
    }

    "generate a filename" in {
      val date = LocalDate.of(2018, Month.JULY, 22)
      val shopId = ShopId("1")
      val productId = new ProductId(12)
      val quantity = Quantity(1L)
      val price = new Price(BigDecimal(42))

      val expected = Filename("1_20180722_12.combined")

      val result = CombinedRecord.salesByShopAndDateAndProductFilename(CombinedRecord(date, shopId, productId, quantity, price))

      result shouldBe expected
    }

    """filter a file endding by ".combined" """ in {
      val result = CombinedRecord.filterFile(File("test.combined"))
      result shouldBe true
    }

    "order by Price" in {
      val date = LocalDate.of(2018, Month.JULY, 22)
      val shopId = ShopId("1")
      val productId = new ProductId(12)
      val quantity = Quantity(1L)
      val price42 = new Price(BigDecimal(42))
      val price1 = new Price(BigDecimal(1))
      val cr42 = CombinedRecord(date, shopId, productId, quantity, price42)
      val cr1 = CombinedRecord(date, shopId, productId, quantity, price1)
      val result = CombinedRecord.orderingByPrice.gt(cr42, cr1)
      result shouldBe true
    }
    "order by Quantity" in {
      val date = LocalDate.of(2018, Month.JULY, 22)
      val shopId = ShopId("1")
      val productId = new ProductId(12)
      val quantity42 = Quantity(41L)
      val quantity1 = Quantity(1L)

      val price = new Price(BigDecimal(1))
      val cr42 = CombinedRecord(date, shopId, productId, quantity42, price)
      val cr1 = CombinedRecord(date, shopId, productId, quantity1, price)
      val result = CombinedRecord.orderingByQuantity.gt(cr42, cr1)
      result shouldBe true
    }
  }
}
