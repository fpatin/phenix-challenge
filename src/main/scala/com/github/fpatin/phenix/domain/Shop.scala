package com.github.fpatin.phenix
package domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import better.files.File
import com.typesafe.scalalogging.StrictLogging

case class Shop(shopId: ShopId, date: LocalDate, productId: ProductId, price: Price)

object Shop extends StrictLogging {

  import ShopKeys._

  def filename(shopId: ShopId, date: LocalDate, dateFormatter: DateTimeFormatter = `yyyyMMdd`): Filename =
    Filename(s"$shopFilenamePrefix${shopId.u}_${date.format(dateFormatter)}$shopFilenameExtension")

  def decode(shopId: ShopId, date: LocalDate): Decoder[Shop] = s => {
    val array = s.split(separatorRegEx, NbFields)
    Shop(shopId, date, ProductId(array(ProductIdKey)), Price(array(PriceKey)))
  }

  def filterShop(shops: Stream[(ShopFileKey, Stream[Shop])], shopId: ShopId, date: LocalDate): Option[Stream[Shop]] = {
    shops.find { case (key, _) => key.shopId.u == shopId.u && key.date.isEqual(date) }.map(_._2)
  }
}

case class ShopFileKey(shopId: ShopId, date: LocalDate)

object ShopFileKey {
  def fromFilename(file: File): ShopFileKey = {
    val key = file.nameWithoutExtension.substring(shopFilenamePrefix.length)
    val array = key.split('_')
    new ShopFileKey(ShopId(array(0)), LocalDate.parse(array(1), `yyyyMMdd`))
  }
}

object ShopKeys {
  val NbFields = 2

  val ProductIdKey = 0
  val PriceKey = 1
}