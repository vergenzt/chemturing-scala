package com.timvergenz.chemturing.core

/**
 * Represents the execution modes of the operating system.
 *
 * @param switchFromOp the operation that, when encountered twice in a row,
 *   triggers a switch OUT of this mode and into `complement`
 * @param complement the complementary mode to switch into when `switchFromOp`
 *   is encountered twice
 */
abstract sealed class Mode(val switchFromOp: Operation, _complement: => Mode) {
  /*
   * Note: This craziness with having a by-name parameter _complement and
   * assigning it to a lazy val is because EXEC and KILL are mutually self-
   * referential, and one reference ends up being null if you try to
   * instantiate it normally.
   */
  lazy val complement = _complement
}
case object EXEC extends Mode(READ, KILL)
case object KILL extends Mode(WRITE, EXEC)
