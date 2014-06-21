package com.timvergenz.chemturing.core

/**
 * Represents a single state in time of the chemical operating system.
 *
 * Preconditions:
 *  - 0 <= progPtr, dataPtr, m < data.size
 *
 * @param data the data bits for this machine, which the operations act upon
 * @param mem the value of the ambient memory bit
 * @param m the constant parameter m, representing the number of skips to be
 *   executed when encountering an MSKIP operation
 * @param progPtr the current program counter; the bits at and immediately after
 *   this pointer represent the next command to be executed
 * @param dataPtr the current data pointer; the bit at this pointer is what is
 *   read from and written to during READ and WRITE operations
 * @param mode the execution mode of the system, KILL or EXEC
 * @param prep the prep chemical that allows switching between KILL and EXEC;
 *   this is true on the first encounter with a mode-switching operation, and
 *   if that same operation is encountered while prep is true, the mode is
 *   switched
 */
case class ChemTuringState(
  val data: Seq[Boolean],
  val mem: Boolean,
  val m: Int,
  val progPtr: Int,
  val dataPtr: Int,
  val mode: Mode,
  val prep: Boolean = false) {

  // preconditions
  assert(0 <= progPtr && progPtr < data.size)
  assert(0 <= dataPtr && dataPtr < data.size)
  assert(0 <= m && m < data.size)

  def nextProgBits: Seq[Boolean] = (data ++ data).slice(progPtr, progPtr + 2)
  def dataBit: Boolean = data(dataPtr)
}

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
  extends PartialFunction[ChemTuringState, ChemTuringState] {

  /**
   * Get whether this operation can be applied to the given state (i.e. whether
   * `bitSeq` matches the next operation at the program pointer).
   */
  def isDefinedAt(state: ChemTuringState): Boolean =
    (state.nextProgBits == bitSeq)

  /**
   * Apply this operation. This is the core logic of the ChemTuring operating
   * system.
   */
  def apply(state: ChemTuringState): ChemTuringState = {
    val nextPP = (state.progPtr + 2) % state.data.size
    val dataPtrPlus1 = (state.dataPtr + 1) % state.data.size
    val dataPtrPlusM = (state.dataPtr + state.m) % state.data.size
    
    import state._

    // determine mode and prep switch behavior
    val (nextPrep, nextMode) =
      (this, prep) match {
        case (mode.switchFromOp, false) => (true,  mode)
        case (mode.switchFromOp, true)  => (false, mode.complement)
        case  _                         => (false, mode)
      }

    // determine what happens with data, mem, and the data pointer
    val (nextDataPtr, nextMem, nextData) =
      (this, mode) match {

        // EXEC mode does nothing when switching to KILL mode
        case (_, EXEC) if nextMode != mode => (dataPtr, mem, data)

        // normal EXEC mode operation executions
        case (SKIP,  EXEC)  => (dataPtrPlus1, mem,     data)
        case (MSKIP, EXEC)  => (dataPtrPlusM, mem,     data)
        case (READ,  EXEC)  => (dataPtrPlus1, dataBit, data)
        case (WRITE, EXEC)  => (dataPtrPlus1, mem,     data.updated(dataPtr, mem))

        // KILL mode always skips
        case (_, KILL) => (dataPtrPlus1, mem, data)
      }

    // return a new state with attributes modified
    state.copy(
      mode = nextMode, prep = nextPrep,
      progPtr = nextPP, dataPtr = nextDataPtr,
      data = nextData, mem = nextMem)
  }
}

// to be able to create Seq[Boolean]s out of Strings
import com.timvergenz.chemturing.util.Util.string2BitSeq

/**
 * Increments the data pointer by 1
 */
case object SKIP extends Operation("01")

/**
 * Increments the data pointer by `m`
 */
case object MSKIP extends Operation("00")

/**
 * Copies bit at the data pointer to `mem`; increments the data pointer by 1
 */
case object READ extends Operation("10")

/**
 * Copies `mem` to bit at the data pointer; increments the data pointer by 1
 */
case object WRITE extends Operation("11")

/**
 * Represents the execution modes of the operating system.
 *
 * @param switchFromOp the operation that, when encountered twice in a row,
 *   triggers a switch OUT of this mode and into `complement`
 * @param complement the complementary mode to switch into when `switchFromOp`
 *   is encountered twice
 */
abstract sealed class Mode(
  val switchFromOp: Operation,
  val complement: Mode)
case object EXEC extends Mode(READ,  KILL)
case object KILL extends Mode(WRITE, EXEC)
