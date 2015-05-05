package gmu

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging
import bwapi.Mirror
import com.typesafe.config.ConfigFactory
import gmu.ReplayDone
import gmu.UnitState.UnitState

import scala.collection.mutable

case class GameUnit(state: UnitState, unit: bwapi.Unit, replayFrame: ReplayFrame)

object Local extends App {

  implicit val system = ActorSystem("LocalSystem", ConfigFactory.load())
//  val localActor = system.actorOf(FromConfig.props(Props[LocalActor]), "localRouter")
  val localActor = system.actorOf(Props[LocalActor], "localActor")

  val mirror = new Mirror()
  val listener = new BwListener(localActor, mirror)
  mirror.getModule.setEventListener(listener)

  mirror.startGame()
}

class LocalActor() extends Actor {
  val log = Logging(context.system, this)

  val remote = context.actorSelection("akka.tcp://Zerg@192.168.1.250:2552/user/zergRouter")
  remote ! WakeUp

  def receive = {
    case GameUnit(state, unit, frame) =>
      remote ! replayUnit(state, unit, frame)
    case msg: ReplayPlayers =>
      log.info("Got replay frame, sending {}/{}", msg.frame.frame, msg.frame.frameCount)
      remote ! msg
    case ReplayDone =>
      log.info("Done")
      remote ! ReplayDone
  }

  def replayUnit(state: UnitState.Value, unit: bwapi.Unit, frame: ReplayFrame): ReplayUnit = {
    val orderTarget = unit.getOrderTarget
    val orderTargetId :Int = if(unit.getOrderTarget != null) unit.getOrderTarget.getID else -1

    val targetId :Int = if(unit.getTarget != null) unit.getTarget.getID else -1
    val energy = Option(unit.getEnergy)

    ReplayUnit(
      frame,
      UnitState.Created eq state,
      UnitState.Destroyed eq state,
      unit.getID,
      unit.getPlayer.getID,
      unit.getPosition,
      Velocity(unit.getVelocityX, unit.getVelocityY),
      unit.getType.getRace,
      unit.getInitialHitPoints,
      unit.getHitPoints,
      unit.getShields,
      unit.getType,
      if(energy.isEmpty) -1 else energy.get,
      unit.getOrder,
      orderTargetId,
      unit.getOrderTargetPosition,
      targetId,
      unit.getTargetPosition,
      unit.getGroundWeaponCooldown,
      unit.getAirWeaponCooldown,
      unit.getSpellCooldown
    )
  }

  implicit def convert[T](sq: collection.mutable.Seq[T]): collection.immutable.Seq[T] =
    collection.immutable.Seq[T](sq:_*)

  implicit def convert(race: bwapi.Race): gmu.Race.RaceType =
    gmu.Race.fromName(race.toString)

  implicit def convertToEnum(unitType: bwapi.UnitType): gmu.Unit.UnitType =
    gmu.Unit.fromName(unitType.toString)

  implicit def convert(order: bwapi.Order): gmu.Order.OrderType =
    gmu.Order.fromName(order.toString)

  implicit def convert(pos: bwapi.Position): gmu.RPosition =
    RPosition(pos.getX, pos.getY)

  implicit def convert(unitType: bwapi.UnitType): gmu.UnitTypeAttributes =
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

  implicit def convert(weaponType: bwapi.WeaponType): gmu.Weapon.WeaponType =
    gmu.Weapon.fromName(weaponType.toString)

  implicit def convertToWeaponTypeInfo(weaponType: bwapi.WeaponType): gmu.WeaponTypeInfo =
    WeaponTypeInfo(weaponType.damageAmount(),
      weaponType.damageCooldown(),
      weaponType.maxRange(),
      weaponType.minRange(),
      weaponType.medianSplashRadius())
}

