package gmu

import gmu.Order.Order
import gmu.Race.Race
import gmu.UnitType.UnitType

case class UnitTypeAttributes(isDetector: Boolean,
                              isFlyer: Boolean,
                              isMechanical: Boolean,
                              isOrganic: Boolean,
                              isRobotic: Boolean,
                              isWorker: Boolean)

case class RPosition(x: Int, y: Int)

case class ReplayMap(mapName: String, size: (Int, Int))

case class ReplayUnit(isNew: Boolean,
                      isDestory: Boolean,
                      id: Int,
                      playerId: Int,
                      position: RPosition,
                      race: Race,
                      initalHp: Int,
                      hp: Int,
                      sheilds: Int,
                      unitType: UnitType,
                      energy: Int,
                      order: Order,
                      orderTargetUnit: Int,
                      orderTargetPosition: RPosition,
                      groundWeaponCooldown: Int,
                      airWeaponCooldown: Int,
                      spellCooldown: Int)

case class ReplayFrame(units: Seq[ReplayUnit], map: ReplayMap, replay: Int)

object UnitState extends Enumeration {
  type UnitState = Value
  val Created, Normal, Destoryed = Value
}