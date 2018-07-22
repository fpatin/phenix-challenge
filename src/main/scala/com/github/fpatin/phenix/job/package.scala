package com.github.fpatin.phenix

import java.time.LocalDate

import better.files.File

package object job {

  def filterFiles(directory: File, filter: File => Boolean): Iterator[File] =
    directory.children.filter(filter)

  def rangeBefore(to: LocalDate, nb: Int): Seq[LocalDate] = {
    Stream.iterate(to, nb)((ld: LocalDate) => ld.minusDays(1L))
  }
}
