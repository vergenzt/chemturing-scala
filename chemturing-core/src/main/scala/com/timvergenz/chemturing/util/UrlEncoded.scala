package com.timvergenz.chemturing.util

import com.timvergenz.chemturing.core.EXEC
import com.timvergenz.chemturing.core.KILL
import com.timvergenz.chemturing.core.State

import StringInterpolaters.BitHelper
import StringInterpolaters.BitSeqHelper
import StringInterpolaters.Regex

object UrlEncoded {

  def apply(state: State) = {
    import state._
    f"${data.toBinaryString}/$progPtr/$dataPtr/${mem.toBinaryString}/$mode/${prep.toBinaryString}/$m"
  }

  def unapply(stateStr: String) = stateStr match {
    case r"([01]+)$data/(\d+)$progPtr/(\d+)$dataPtr/([01])$mem/(EXEC|KILL)$mode/([01])$prep/(\d+)$m" =>
      val modeObject = mode match {
        case "EXEC" => EXEC
        case "KILL" => KILL
      }
      Some(State(BitSeq(data), progPtr.toInt, dataPtr.toInt, Bit(mem), modeObject, Bit(prep), m.toInt))
    case _ =>
      None
  }
}
