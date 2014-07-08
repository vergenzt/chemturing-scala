package com.timvergenz.chemturing

package object util {

  implicit class BitSeqHelper(bitSeq: Seq[Boolean]) {
    def toBinaryString: String =
      bitSeq.map(if (_) '1' else '0').mkString
  }

  implicit class BitHelper(bit: Boolean) {
    def toBinaryString: String = if (bit) "1" else "0"
  }

  implicit class BinaryStringHelper(sc: StringContext) {
    def seq() = {
      val s = sc.parts.mkString
      s.toCharArray() map {
        case '0' => false
        case '1' => true
      }
    }

    def bit() = {
      sc.parts.mkString match {
        case "0" => false
        case "1" => true
      }
    }

    def b() = {
      val s = sc.parts.mkString
      parseBinary(s).getOrElse(sys.error("invalid binary literal: " + s))
    }

    def parseBinary(s: String): Option[Int] =
      try {
        Some(s.toCharArray().foldRight(0) { (accumulator, char) =>
          (2 * accumulator) + char match {
            case ('0' | '1') => (char - '0')
          }
        })
      } catch {
        case e: MatchError => None
      }
  }

}
