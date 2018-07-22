package com.gthub.fpatin.phenix
package job

import java.time.{LocalDate, Month}

import com.github.fpatin.phenix.job._
import org.scalatest.{Matchers, WordSpec}

class JobPackageSpec
  extends WordSpec
    with Matchers {
  "job.rangeBefore" should {
    "generate Stream of LocalDate " in {
      val expected = Seq(
        LocalDate.of(2018, Month.JULY, 14),
        LocalDate.of(2018, Month.JULY, 13),
        LocalDate.of(2018, Month.JULY, 12),
        LocalDate.of(2018, Month.JULY, 11),
        LocalDate.of(2018, Month.JULY, 10),
        LocalDate.of(2018, Month.JULY, 9),
        LocalDate.of(2018, Month.JULY, 8)
      )

      val to = LocalDate.of(2018, Month.JULY, 14)

      val result = rangeBefore(to, 7)
      result should contain theSameElementsAs expected
    }
  }

}
