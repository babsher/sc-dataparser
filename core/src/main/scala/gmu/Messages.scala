package gmu

import gmu.Order.OrderType
import gmu.Race.RaceType
import gmu.Unit.UnitType
import gmu.Weapon.WeaponType

case class UnitTypeAttributes(armor: Int,
                              isDetector: Boolean,
                              isFlyer: Boolean,
                              isMechanical: Boolean,
                              isOrganic: Boolean,
                              isRobotic: Boolean,
                              isWorker: Boolean,
                              isBuilding: Boolean,
                              groundWeapon: WeaponType,
                              groundWeaponAttr: WeaponTypeInfo,
                              airWeapon: WeaponType,
                              airWeaponAttr: WeaponTypeInfo,
                              destroyScore: Int)

case class RPosition(x: Int, y: Int)

case class WeaponTypeInfo(
                           damageAmount: Int,
                           damageCooldown: Int,
                           maxRange: Int,
                           minRange: Int,
                           splashRadius: Int)

case class MapSize(x: Int, y: Int)

case class ReplayMap(mapName: String, size: MapSize)

case class ReplayPlayer(id: Int,
                        race: RaceType,
                        supply: Int,
                        totalSupply: Int,
                        tech: Map[Tech.TechType, Boolean],
                        upgrades: Map[Upgrade.UpgradeType, Int])

case class Velocity(x: Double, y: Double)

case class ReplayUnit(frame: ReplayFrame,
                      isNew: Boolean,
                      isDestory: Boolean,
                      id: Int,
                      playerId: Int,
                      position: RPosition,
                      velocity: Velocity,
                      race: RaceType,
                      initalHp: Int,
                      hp: Int,
                      sheilds: Int,
                      unitType: UnitType,
                      energy: Int,
                      order: OrderType,
                      orderTargetUnit: Int,
                      orderTargetPosition: RPosition,
                      targetId: Int,
                      targetPosition: RPosition,
                      groundWeaponCooldown: Int,
                      airWeaponCooldown: Int,
                      spellCooldown: Int)

case class ReplayFrame(map: ReplayMap,
                       replay: Int,
                       frame: Int,
                       frameCount: Int)

case class ReplayPlayers(frame: ReplayFrame, players: Seq[ReplayPlayer])

object UnitState extends Enumeration {
  type UnitState = Value
  val Created, Normal, Destroyed = Value
}

case class BwMap(replayMap: ReplayMap, cells: Seq[MapCell])
case class MapCell(x: Int, y: Int, height: Int)

case object WakeUp
case object ReplayDone