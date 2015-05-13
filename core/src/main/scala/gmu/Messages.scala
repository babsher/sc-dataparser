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

case class BwUnitType(
                     untitType: gmu.Unit.UnitType,
                     race: RaceType,
                       requiredTech: TechType,
                       cloakingTech: TechType,
                       abilities: Seq[gmu.Tech.TechType],
                       upgrades: Seq[gmu.Upgrade.UpgradeType],
                       armorUpgrade: UpgradeType,
                       maxHitPoints: Int,
                       maxShields: Int,
                       maxEnergy: Int,
                       armor: Int,
                       mineralPrice: Int,
                       gasPrice: Int,
                       buildTime: Int,
                       supplyRequired: Int,
                       supplyProvided: Int,
                       spaceRequired: Int,
                       spaceProvided: Int,
                       buildScore: Int,
                       destroyScore: Int,
                       tileWidth: Int,
                       tileHeight: Int,
                       tileSize: RPosition,
                       dimensionLeft: Int,
                       dimensionUp: Int,
                       dimensionRight: Int,
                       dimensionDown: Int,
                       width: Int,
                       height: Int,
                       seekRange: Int,
                       sightRange: Int,
                       groundWeapon: WeaponType,
                       maxGroundHits: Int,
                       airWeapon: WeaponType,
                       maxAirHits: Int,
                       topSpeed: Double,
                       acceleration: Int,
                       haltDistance: Int,
                       turnRadius: Int,
                       canProduce: Boolean,
                       canAttack: Boolean,
                       canMove: Boolean,
                       isFlyer: Boolean,
                       regeneratesHP: Boolean,
                       isSpellcaster: Boolean,
                       hasPermanentCloak: Boolean,
                       isInvincible: Boolean,
                       isOrganic: Boolean,
                       isMechanical: Boolean,
                       isRobotic: Boolean,
                       isDetector: Boolean,
                       isResourceContainer: Boolean,
                       isResourceDepot: Boolean,
                       isRefinery: Boolean,
                       isWorker: Boolean,
                       requiresPsi: Boolean,
                       requiresCreep: Boolean,
                       isTwoUnitsInOneEgg: Boolean,
                       isBurrowable: Boolean,
                       isCloakable: Boolean,
                       isBuilding: Boolean,
                       isAddon: Boolean,
                       isFlyingBuilding: Boolean,
                       isNeutral: Boolean,
                       isHero: Boolean,
                       isPowerup: Boolean,
                       isBeacon: Boolean,
                       isFlagBeacon: Boolean,
                       isSpecialBuilding: Boolean,
                       isSpell: Boolean,
                       producesLarva: Boolean,
                       isMineralField: Boolean,
                       isCritter: Boolean,
                       canBuildAddon: Boolean)