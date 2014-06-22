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
case class State(
  val data: Seq[Boolean],
  val mem: Boolean,
  val m: Int,
  val progPtr: Int,
  val dataPtr: Int,
  val mode: Mode,
  val prep: Boolean = false) {

  // preconditions
  assert(0 <= progPtr && progPtr < data.size, "progPtr out of bounds")
  assert(0 <= dataPtr && dataPtr < data.size, "dataPtr out of bounds")
  assert(0 <= m && m < data.size, "m out of bounds")

  override def toString =
    f"State($data, pp=$progPtr, mem=$mem, dp=$dataPtr, mode=$mode, prep=$prep, m=$m)"

  /**
   * Get the next two bits at the program pointer, accounting for wraparound.
   */
  def nextProgBits: Seq[Boolean] = (data ++ data).slice(progPtr, progPtr + 2)

  /**
   * Get the bit at the data pointer.
   */
  def dataBit: Boolean = data(dataPtr)

  def succ = successor(this)
  def next = successor(this)
}
