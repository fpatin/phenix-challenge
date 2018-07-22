package com.gthub.fpatin.phenix

import java.time.Month

import com.github.fpatin.phenix._
import org.scalatest._

class PhenixSpec extends WordSpec with Matchers {
  "Transaction" should {
    "parse a string to date" in {
      val dateAsString = "20170514T223544+0100"
      val result = parseDate(dateAsString, `yyyyMMdd'T'HHmmssZ`)

      result.getYear shouldBe 2017
      result.getMonth shouldBe Month.MAY
      result.getDayOfMonth shouldBe 14
    }
  }
}
