package gmu

import main.scala.gmu.GameUnit

import scala.collection.JavaConversions._

import akka.actor.ActorRef
import bwapi.{Unit => BwUnit, _}

import scala.collection.mutable


case class MapState()

class BwListener(val local: ActorRef, val mirror: Mirror) extends DefaultBWListener {

  val current: mutable.Set[Int]
  val destroyed: mutable.Set[Int]
  var map: ReplayMap
  var replayNum: Int

  override def onUnitCreate(unit: BwUnit): Unit = {
  }

  def getState(unit: BwUnit) = {
    if (current.contains(unit.getID)) {
      if(destroyed.contains(unit.getID)) {
        UnitState.Destoryed
      } else {
        UnitState.Normal
      }
    } else {
      UnitState.Created
    }
  }

  override def onFrame(): Unit = {
    for(unit <- mirror.getGame.getAllUnits) {
      val state = getState(unit)
      local ! GameUnit(state, unit)
    }
    local ! ReplayFrame(null, map, replayNum)
  }

  override def onEnd(b: Boolean): Unit = super.onEnd(b)

  override def onUnitDestroy(unit: BwUnit): Unit = super.onUnitDestroy(unit)

  def mapName: String = {
    mirror.getGame.mapName()
  }

  def mapDimensions: (Int, Int) = {
    (mirror.getGame.mapHeight(), mirror.getGame.mapWidth())
  }

  override def onStart(): Unit = {
    // getplayers
    // update replay map
    // update replay num
  }
}
