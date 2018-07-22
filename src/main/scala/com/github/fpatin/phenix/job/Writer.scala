package com.github.fpatin.phenix.job

import java.io.BufferedWriter

import better.files.File
import com.github.fpatin.phenix.domain.{Encoder, Filename}
import com.typesafe.scalalogging.StrictLogging

object Writer extends StrictLogging {

  def write[T](directory: File, filename: Filename, encoder: Encoder[T])(stream: Stream[T]): Unit = {
    File(directory, filename.u).bufferedWriter.foreach { bw =>
      stream.force.foreach { cr =>
        bw.write(encoder(cr))
        bw.newLine()
      }
    }
  }

  def write[T](t: T, encoder: Encoder[T], bw: BufferedWriter): Unit = {
    bw.write(encoder(t))
    bw.newLine()
  }
}


