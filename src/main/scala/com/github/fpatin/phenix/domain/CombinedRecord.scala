package com.github.fpatin.phenix
package domain

import java.time.LocalDate

import better.files.File


case class CombinedRecord(date: LocalDate, shopId: ShopId, productId: ProductId, quantity: Quantity, price: Price)

object CombinedRecord {
  def apply(tr: TransactionRecord, q: Quantity, p: Price): CombinedRecord = new CombinedRecord(tr.date, tr.shopId, tr.productId, q, p)

  val zero: CombinedRecord = CombinedRecord(LocalDate.MIN, ShopId.zero, ProductId.zero, Quantity.zero, Price.zero)

  val extension = ".combined"

  val salesByShopAndDateAndProductFilename: CombinedRecord => Filename = s =>
    Filename(s"${s.shopId.u}_${s.date.format(yyyyMMdd)}_${s.productId.u}$extension")

  val filterFile: File => Boolean = filterByExtension(extension)

  def orderingByQuantity: Ordering[CombinedRecord] = {
    import Quantity.ordering
    Ordering.by[CombinedRecord, Quantity](_.quantity)
  }

  def orderingByPrice: Ordering[CombinedRecord] = {
    import Price.ordering
    Ordering.by[CombinedRecord, Price](_.price)
  }

  val decoder: Decoder[CombinedRecord] = line => {
    import CombinedRecordKeys._
    val array = line.split(separatorRegEx, NbField)
    CombinedRecord(
      parseDate(array(DatetimeKey), yyyyMMdd),
      ShopId(array(ShopIdKey)),
      ProductId(array(ProductIdKey)),
      Quantity(array(QuantityKey)),
      Price(array(PriceKey))
    )
  }
  val encoder: Encoder[CombinedRecord] = cr =>
    Seq(
      cr.date.format(yyyyMMdd),
      cr.shopId.u,
      cr.productId.u,
      cr.quantity.u,
      cr.price.u.toString(),
    ).mkString(separator)

}

object CombinedRecordKeys {
  val NbField = 5

  val DatetimeKey = 0
  val ShopIdKey = 1
  val ProductIdKey = 2
  val QuantityKey = 3
  val PriceKey = 4
}
