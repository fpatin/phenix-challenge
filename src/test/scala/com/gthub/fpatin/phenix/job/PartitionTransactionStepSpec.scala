package com.gthub.fpatin.phenix.job

import java.time.LocalDate

import com.github.fpatin.phenix.domain.Filename
import com.github.fpatin.phenix.job.PartitionTransactionStep
import org.scalatest.{Matchers, WordSpec}

class PartitionTransactionStepSpec extends WordSpec with Matchers {

  "PartitionTransactionStep" should {
    "return a " in {
      val date = LocalDate.of(2018, 7, 1)
      val expected = Filename("transactions_20180701.data")
      val result = PartitionTransactionStep.transactionsFileName(date)
      result shouldEqual expected
    }
  }
}
