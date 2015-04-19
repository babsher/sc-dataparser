package gmu

import gmu.Order.OrderType
import gmu.Race.RaceType
import gmu.Tech.TechType
import gmu.Unit.UnitType
import gmu.Upgrade.UpgradeType
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

case class ReplayMap(mapName: String, size: (Int, Int))

case class ReplayPlayer(id: Int,
                        tech: Map[TechType, Boolean],
                        upgrades: Map[UpgradeType, Int])

case class Velocity(x: Double, y: Double)

case class ReplayUnit(isNew: Boolean,
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

case class ReplayFrame(units: Seq[ReplayUnit],
                       map: ReplayMap,
                       replay: Int,
                       frame: Int,
                       frameCount: Int,
                       players: Seq[ReplayPlayer])

object UnitState extends Enumeration {
  type UnitState = Value
  val Created, Normal, Destoryed = Value
}