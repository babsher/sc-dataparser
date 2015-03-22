package gmu

import gmu.Race.Race
import gmu.UnitType.UnitType

case class ReplayMap(mapName: String, size: (Int, Int))
case class ReplayUnit(isNew: Boolean,
                      isDestory: Boolean,
                      id: Int,
                      race: Race,
                      initalHp: Int,
                      hp: Int,
                      sheilds: Int,
                      unitType: UnitType,
                      energy: Int)
case class ReplayFrame(units: Seq[ReplayUnit], map: ReplayMap, replay: Int)


object UnitState extends Enumeration {
  type UnitState = Value
  val Created, Normal, Destoryed = Value
}