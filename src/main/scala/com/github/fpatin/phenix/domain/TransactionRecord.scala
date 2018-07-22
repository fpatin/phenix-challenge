package com.github.fpatin.phenix
package domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import better.files.File

/**
  * txId : id de transaction (nombre)
  * datetime : date et heure au format ISO 8601
  * shopId : UUID identifiant le magasin
  * productId : id du produit (nombre)
  * qty : quantité (nombre)
  */
case class TransactionRecord(transactionId: TransactionId, date: LocalDate, shopId: ShopId, productId: ProductId, quantity: Quantity)

object TransactionRecord {

  val rawExtension = ".raw"

  val transactionRecordFilename: TransactionRecord => Filename = transactionRecord =>
    Filename(s"${transactionRecord.shopId.u}_${transactionRecord.date.format(`yyyyMMdd`)}_${transactionRecord.productId.u}$rawExtension")

  val filterFile: File => Boolean = filterByExtension(rawExtension)

  def decoder(dateTimeFormatter: DateTimeFormatter): Decoder[TransactionRecord] = line => {
    import TransactionKey._
    val array = line.split(separatorRegEx, NbField)

    new TransactionRecord(
      TransactionId(array(TransactionIdKey)),
      parseDate(array(DatetimeKey), dateTimeFormatter),
      ShopId(array(ShopIdKey)),
      ProductId(array(ProductIdKey)),
      Quantity(array(QuantityKey))
    )
  }

  val encoder: Encoder[TransactionRecord] = tr =>
    List(
      tr.transactionId.u,
      tr.date.format(yyyyMMdd),
      tr.shopId.u,
      tr.productId.u,
      tr.quantity.u
    ).mkString(separator)
}


object TransactionKey {
  val NbField = 5

  val TransactionIdKey = 0
  val DatetimeKey = 1
  val ShopIdKey = 2
  val ProductIdKey = 3
  val QuantityKey = 4
}