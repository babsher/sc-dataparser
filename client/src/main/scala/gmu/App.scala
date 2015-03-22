package main.scala.gmu

import akka.actor.{Actor, ActorSystem, Props}
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
  val localActor = system.actorOf(Props[LocalActor], name = "LocalActor")  // the local actor
  localActor ! "START"                                                     // start the action

  val mirror = new Mirror()
  val listener = new BwListener(localActor, mirror)
  mirror.getModule.setEventListener(listener)

  // game.startGame()
}

class LocalActor() extends Actor {

  var frame: mutable.ListBuffer[ReplayUnit] = mutable.ListBuffer[ReplayUnit]()

  val remote = context.actorSelection("akka.tcp://Zerg@127.0.0.1:2552/user/zergRouter")

  def receive = {
    case GameUnit(state, unit) =>
        frame += replayUnit(state, unit)
    case ReplayFrame(units, map, replayNum) =>
      remote ! ReplayFrame(frame.clone(), map, replayNum)
      sender() ! Done
      frame.clear()
  }

  def replayUnit(state: UnitState.Value, unit: BwUnit): ReplayUnit = {
    ReplayUnit(UnitState.Created eq state,
      UnitState.Destoryed eq state,
      unit.getID,
      unit.getPlayer.getID,
      unit.getPosition,
      unit.getType.getRace,
      unit.getInitialHitPoints,
      unit.getHitPoints,
      unit.getShields,
      unit.getType,
      unit.getEnergy,
      unit.getOrder,
      unit.getOrderTarget.getID,
      unit.getOrderTargetPosition
    )
  }

  implicit def convert[T](sq: collection.mutable.Seq[T]): collection.immutable.Seq[T] =
    collection.immutable.Seq[T](sq:_*)

  implicit def convert(race: Race): gmu.Race.Race =
    gmu.Race.withName(race.c_str())

  implicit def convert(unitType: UnitType): gmu.UnitType.UnitType =
    gmu.UnitType.withName(unitType.c_str())

  implicit def convert(order: Order): gmu.Order.Order =
    gmu.Order.withName(order.c_str())

  implicit def convert(pos: Position): gmu.RPosition =
    RPosition(pos.getX, pos.getY)

  implicit def convert(unitType: UnitType): gmu.UnitTypeAttributes =
    UnitTypeAttributes(unitType.isDetector,
      unitType.isFlyer,
      unitType.isMechanical,
      unitType.isOrganic,
      unitType.isRobotic,
      unitType.isWorker)
}

case class GameUnit(state: UnitState, unit: BwUnit)
case object Done