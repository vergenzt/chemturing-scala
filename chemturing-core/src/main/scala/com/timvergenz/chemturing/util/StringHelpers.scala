package com.timvergenz.chemturing.util

object StringInterpolaters {
  implicit class BitSeqHelper(bitSeq: Seq[Boolean]) {
    def toBinaryString: String =
      bitSeq.map(if (_) '1' else '0').mkString
  }

  implicit class BitHelper(bit: Boolean) {
    def toBinaryString: String = if (bit) "1" else "0"
  }

  implicit class BinaryStringHelper(sc: StringContext) {
    def seq() = BitSeq(sc.parts.mkString)
    def bit() = {
      sc.parts.mkString match {
        case "0" => false
        case "1" => true
      }
    }
  }

  implicit class Regex(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }
}

object Bit {
  def apply(b: String) = b match {
    case "0" => false
    case "1" => true
  }
  def unapply(b: Boolean) = b match {
    case false => Some("0")
    case true => Some("1")
  }
  def apply(b: Char): Boolean = apply(b.toString())
}

object BitSeq {
  def apply(bs: String): Seq[Boolean] = bs map Bit.apply
  def unapply(bs: Seq[Boolean]) = {
    val charOptions = bs map Bit.unapply
    if (charOptions forall (_.isDefined))
      Some(charOptions map (_.get) mkString "")
    else
      None
  }
}

