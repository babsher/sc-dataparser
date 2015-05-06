package gmu

import bwapi.Mirror

object Local extends App {
  val mirror = new Mirror()
  val listener = new BwListener(mirror, Integer.parseInt(args(1)))
  mirror.getModule.setEventListener(listener)

  mirror.startGame()
}