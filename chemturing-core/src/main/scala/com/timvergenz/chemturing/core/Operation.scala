package com.timvergenz.chemturing.core

import com.timvergenz.chemturing.util.StringInterpolaters._

/**
 * Represents a single operation that can be computed in the machine.
 *
 * It extends PartialFunction because it represents precisely that: a function
 * from current state to next state that is applicable over some subset of
 * the domain (e.g. the states in which that operation's bit sequence is next
 * in the program pointer).
 *
 * @param bitSeq the bit sequence that corresponds to this operation
 */
abstract sealed class Operation(val bitSeq: Seq[Boolean])
  extends PartialFunction[State, State] {

  /**
   * Get whether this operation can be applied to the given state (i.e. whether
   * `bitSeq` matches the next operation at the program pointer).
   */
  override def isDefinedAt(state: State): Boolean =
    (state.nextCommand == bitSeq)

  /**
   * Apply this operation. This is the core logic of the ChemTuring operating
   * system.
   */
  override def apply(state: State): State = {
    val op = this
    val nextPP = (state.progPtr + 2) % state.data.size
    val dataPtrPlus1 = (state.dataPtr + 1) % state.data.size
    val dataPtrPlusM = (state.dataPtr + state.m) % state.data.size

    import state._

    // determine mode and prep switch behavior
    val (nextPrep, nextMode) =
      (op, prep) match {
        case (mode.switchFromOp, false) => (true, mode)
        case (mode.switchFromOp, true) => (false, mode.next)
        case _ => (false, mode)
      }

    // determine what happens with data, mem, and the data pointer
    val (nextDataPtr, nextMem, nextData) =
      (op, mode) match {

        // KILL mode always skips
        case (_, KILL) => (dataPtrPlus1, mem, data)

        // EXEC mode skips when transitioning to KILL mode
        case (_, EXEC) if nextMode == KILL => (dataPtrPlus1, mem, data)

        // normal EXEC mode operation executions
        case (SKIP, EXEC) => (dataPtrPlus1, mem, data)
        case (MSKIP, EXEC) => (dataPtrPlusM, mem, data)
        case (READ, EXEC) => (dataPtrPlus1, dataBit, data)
        case (WRITE, EXEC) => (dataPtrPlus1, mem, data.updated(dataPtr, mem))
      }

    // return a new state with attributes modified
    state.copy(
      mode = nextMode, prep = nextPrep,
      progPtr = nextPP, dataPtr = nextDataPtr,
      data = nextData, mem = nextMem)
  }
}

/**
 * Increments the data pointer by 1
 */
case object SKIP extends Operation(seq"01")

/**
 * Increments the data pointer by `m`
 */
case object MSKIP extends Operation(seq"00")

/**
 * Copies bit at the data pointer to `mem`; increments the data pointer by 1
 */
case object READ extends Operation(seq"10")

/**
 * Copies `mem` to bit at the data pointer; increments the data pointer by 1
 */
case object WRITE extends Operation(seq"11")

/**
 * ChemTuring successor function, the composition of all of the Operations
 * enumerated above.
 */
object successor extends Function1[State, State] {
  private val allOperations =
    SKIP orElse MSKIP orElse READ orElse WRITE
  
  def apply(state: State) = allOperations.apply(state)
}
