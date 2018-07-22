package com.github.fpatin.phenix

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import better.files._
import pureconfig.configurable.localDateConfigConvert
import pureconfig.{ConfigConvert, ConfigReader}

package object configuration {

  implicit val localDateConverter: ConfigConvert[LocalDate] = localDateConfigConvert(DateTimeFormatter.BASIC_ISO_DATE)

  implicit val fileReader: ConfigReader[File] = ConfigReader[String].map(s => file"$s")

}
