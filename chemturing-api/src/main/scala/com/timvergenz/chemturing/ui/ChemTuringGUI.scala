package com.timvergenz.chemturing.ui

import scala.swing._
import scala.swing.event._
import com.timvergenz.chemturing.core._
import com.timvergenz.chemturing.util._

object ChemTuringGUI extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "ChemTuring"

    val prevBtn = new Button("Prev")
    val nextBtn = new Button("Next")
    val controlPanel = new FlowPanel {
      contents += prevBtn
      contents += nextBtn
    }

    val stateDisplay = new Label {
      var state = State(data = seq"1000101")
      text = state.toString

      listenTo(nextBtn)
      reactions += {
        case ButtonClicked(`nextBtn`) =>
          state = state.next
          text = state.toString
      }
    }

    contents = new BorderPanel {
      import BorderPanel.Position._
      layout(stateDisplay) = Center
      layout(controlPanel) = South
    }

  }
}
