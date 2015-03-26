package main.scala.gmu

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.FromConfig
import bwapi.Order
import bwapi.Race
import bwapi.UnitType
import bwapi.{Unit => BwUnit, _}
import com.typesafe.config.ConfigFactory
import gmu._
import gmu.UnitState.UnitState

import scala.collection.mutable


object Local extends App {

  implicit val system = ActorSystem("LocalSystem", ConfigFactory.load())
  val localActor = system.actorOf(FromConfig.props(Props[LocalActor]), "localRouter")

  val mirror = new Mirror()
  val listener = new BwListener(localActor, mirror)
  mirror.getModule.setEventListener(listener)

  mirror.startGame()
}

class LocalActor() extends Actor {

  var frame: mutable.ListBuffer[ReplayUnit] = mutable.ListBuffer[ReplayUnit]()

  val remote = context.actorSelection("akka.tcp://Zerg@127.0.0.1:2552/user/zergRouter")

  def receive = {
    case GameUnit(state, unit) =>
        frame += replayUnit(state, unit)
    case ReplayFrame(units, map, replayNum, frameNum, frameCount, players) =>
      remote ! ReplayFrame(frame.clone(), map, replayNum, frameNum, frameCount, players)
      sender() ! Done
      frame.clear()
  }

  def replayUnit(state: UnitState.Value, unit: BwUnit): ReplayUnit = {
    ReplayUnit(UnitState.Created eq state,
      UnitState.Destoryed eq state,
      unit.getID,
      unit.getPlayer.getID,
      unit.getPosition,
      (unit.getVelocityX, unit.getVelocityY),
      unit.getType.getRace,
      unit.getInitialHitPoints,
      unit.getHitPoints,
      unit.getShields,
      unit.getType,
      unit.getEnergy,
      unit.getOrder,
      unit.getOrderTarget.getID,
      unit.getOrderTargetPosition,
      unit.getTarget.getID,
      unit.getTargetPosition,
      unit.getGroundWeaponCooldown,
      unit.getAirWeaponCooldown,
      unit.getSpellCooldown
    )
  }

  implicit def convert[T](sq: collection.mutable.Seq[T]): collection.immutable.Seq[T] =
    collection.immutable.Seq[T](sq:_*)

  implicit def convert(race: Race): gmu.Race.Race =
    gmu.Race.withName(race.toString)

  implicit def convertToEnum(unitType: UnitType): gmu.Unit.UnitType =
    gmu.Unit.withName(unitType.toString)

  implicit def convert(order: Order): gmu.Order.Order =
    gmu.Order.withName(order.toString)

  implicit def convert(pos: Position): gmu.RPosition =
    RPosition(pos.getX, pos.getY)

  implicit def convert(unitType: UnitType): gmu.UnitTypeAttributes =
    UnitTypeAttributes(
      unitType.armor,
      unitType.isDetector,
      unitType.isFlyer,
      unitType.isMechanical,
      unitType.isOrganic,
      unitType.isRobotic,
      unitType.isWorker,
      unitType.isBuilding,
      unitType.groundWeapon,
      unitType.groundWeapon,
      unitType.airWeapon,
      unitType.airWeapon,
      unitType.destroyScore
    )

  implicit def convert(weaponType: WeaponType): gmu.Weapon.WeaponType =
    gmu.Weapon.withName(weaponType.toString)

  implicit def convertToWeaponTypeInfo(weaponType: WeaponType): gmu.WeaponTypeInfo =
    WeaponTypeInfo(weaponType.damageAmount(),
      weaponType.damageCooldown(),
      weaponType.maxRange(),
      weaponType.minRange(),
      weaponType.medianSplashRadius())
}

case class GameUnit(state: UnitState, unit: BwUnit)
case object Done