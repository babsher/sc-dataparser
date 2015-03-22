package gmu

import scala.collection.JavaConversions._

import akka.actor.ActorRef
import bwapi.{Unit => BwUnit, _}

import scala.collection.mutable

case class ReplayFrame(units: Seq[ReplayUnit], map: ReplayMap, replay: Int)
case class ReplayMap(mapName: String, size: (Int, Int))
case class ReplayUnit(isNew: Boolean, isDistroyed: Boolean, id: Int, isHighGround: Boolean)
case class MapState()

object UnitState extends Enumeration {
  type UnitState = Value
  val Created, Normal, Destoryed = Value
}

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
    val frame: Seq[ReplayUnit] = mirror.getGame.getAllUnits.par.map((unit :BwUnit) => {
      val state = getState(unit)
      replayUnit(state, unit)
    }).seq
    local ! ReplayFrame(frame, map, replayNum)
  }

  override def onEnd(b: Boolean): Unit = super.onEnd(b)

  override def onUnitDestroy(unit: BwUnit): Unit = super.onUnitDestroy(unit)

  def replayUnit(state: UnitState.Value, unit: BwUnit): ReplayUnit = {
    val game = mirror.getGame
    ReplayUnit(UnitState.Created eq state,
      UnitState.Destoryed eq state,
      unit.getID,
      unit.getType.getRace,
      unit.getRegion.isHigherGround
      )
  }

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
