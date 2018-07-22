package com.github.fpatin.phenix

import better.files.File

package object domain {

  type Decoder[T] = String => T

  type Encoder[T] = T => String

  class TransactionId(val u: Long) extends AnyVal {
    override def toString: String = u.toString
  }

  object TransactionId {
    def apply(s: String): TransactionId = new TransactionId(s.toLong)

    val zero = new TransactionId(0L)
  }

  class ShopId(val u: String) extends AnyVal {
    override def toString: String = u
  }

  object ShopId {
    def apply(s: String): ShopId = new ShopId(s)

    val zero = new ShopId("")
  }

  class ProductId(val u: Long) extends AnyVal {
    override def toString: String = u.toString
  }

  object ProductId {
    def apply(s: String): ProductId = new ProductId(s.toLong)

    val zero = new ProductId(0L)
  }

  class Quantity(val u: Long) extends AnyVal {
    def +(that: Quantity): Quantity = Quantity(u + that.u)

    def >(that: Quantity): Boolean = u > that.u

    override def toString: String = u.toString
  }

  object Quantity {
    def apply(s: String): Quantity = new Quantity(s.toLong)

    def apply(s: Long): Quantity = new Quantity(s)

    val zero = new Quantity(0L)

    implicit def ordering: Ordering[Quantity] = Ordering.by[Quantity, Long](_.u)
  }

  class Price(val u: BigDecimal) extends AnyVal {
    def *(quantity: Quantity) = new Price(u * BigDecimal(quantity.u))

    override def toString: String = u.toString()
  }

  object Price {
    def apply(s: String): Price = new Price(BigDecimal(s))

    val zero = new Price(BigDecimal(0))

    implicit def ordering: Ordering[Price] = Ordering.by[Price, BigDecimal](_.u)
  }

  class Filename(val u: String) extends AnyVal {
    override def toString: String = u
  }

  object Filename {
    def apply(u: String): Filename = new Filename(u)
  }

  def filterByExtension(extension: String)(file: File): Boolean =
    file.extension.contains(extension.toLowerCase)
}
