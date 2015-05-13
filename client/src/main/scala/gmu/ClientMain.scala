package gmu

import bwapi.Mirror

object ClientMain extends App {
  val mirror = new Mirror()
  val listener = new BwListener(mirror, Integer.parseInt(args(2)), args(1), false)
  mirror.getModule.setEventListener(listener)

  mirror.startGame()
}