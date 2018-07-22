package com.gthub.fpatin.phenix.job

import java.io.{BufferedWriter, StringWriter}

import com.github.fpatin.phenix.domain.Encoder
import com.github.fpatin.phenix.job.Writer
import org.scalatest.{Matchers, WordSpec}

class WriterSpec
  extends WordSpec
    with Matchers {

  "Writer" should {
    "a single instance" in {
      val data = 1 -> "a line of text"
      val encoder: Encoder[(Int, String)] = t => s"${t._1}|${t._2}"
      val sw = new StringWriter()
      val bw = new BufferedWriter(sw)
      Writer.write(data, encoder, bw)
      bw.close()
      sw.toString shouldBe "1|a line of text" + System.lineSeparator()
    }
  }

}
