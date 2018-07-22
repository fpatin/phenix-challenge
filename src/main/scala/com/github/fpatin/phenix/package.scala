package com.github.fpatin

import java.time.LocalDate
import java.time.format.DateTimeFormatter

package object phenix {

  type Seq[+T] = scala.collection.immutable.Seq[T]
  val Seq = scala.collection.immutable.Seq

  val `yyyyMMdd'T'HHmmssZ`: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssZ")
  val yyyyMMdd: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

  def parseDate(s: String, dateFormatter: DateTimeFormatter): LocalDate =
    LocalDate.parse(s, dateFormatter)

  def formatDate(date: LocalDate, dateFormatter: DateTimeFormatter): String =
    date.format(dateFormatter)

  val separatorRegEx = "\\|"
  val separator = "|"
  val shopFilenamePrefix = "reference_prod-"
  val shopFilenameExtension = ".data"

  val transactionFilenamePrefix = "transactions_"
  val transactionFilenameExtension = ".data"
}
