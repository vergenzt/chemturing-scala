package com.timvergenz.chemturing.core

import com.timvergenz.chemturing.util._
import scala.annotation.tailrec

/**
 * Represents a single state in time of the chemical operating system.
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
  val progPtr: Int = 0,
  val dataPtr: Int = 0,
  val mem: Boolean = false,
  val mode: Mode = EXEC,
  val prep: Boolean = false,
  val m: Int = 1) {

  // preconditions
  require(data.size > 0, "data is empty")
  require(0 <= progPtr && progPtr < data.size, "progPtr out of bounds")
  require(0 <= dataPtr && dataPtr < data.size, "dataPtr out of bounds")

  /**
   * Get the next two bits at the program pointer, accounting for wraparound.
   */
  def nextCommand: Seq[Boolean] = (data ++ data).slice(progPtr, progPtr + 2)

  /**
   * Get the bit at the data pointer.
   */
  def dataBit: Boolean = data(dataPtr)

  /**
   * Get the successor to this state by executing the next operation.
   */
  def next: State = successor(this)

  /**
   * Get the descendant of this state found by executing the next operation n times.
   */
  def next(n: Int): State = {
    var state = this
    for (i <- Range(0,n)) state = successor(state)
    state
  }

  override def toString = {
    val dataStr = data.toBinaryString
    val memStr = mem.toBinaryString
    f"State($dataStr, progPtr=$progPtr%02d, dataPtr=$dataPtr%02d, mem=$memStr, mode=$mode, prep=$prep, m=$m)"
  }
}
