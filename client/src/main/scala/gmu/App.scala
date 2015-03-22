package main.scala.gmu

import akka.actor.{Actor, ActorSystem, Props}
import bwapi.{Unit => BwUnit, _}
import com.typesafe.config.ConfigFactory
import gmu.UnitState
import gmu.UnitState.UnitState
import gmu.{UnitState, ReplayUnit, ReplayFrame, BwListener}

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
      unit.getType.getRace,
      unit.getInitialHitPoints,
      unit.getHitPoints,
      unit.getShields,
      unit.getType,
      unit.getEnergy,
      unit.getOrder
    )
  }

  implicit def convert[T](sq: collection.mutable.Seq[T]): collection.immutable.Seq[T] =
    collection.immutable.Seq[T](sq:_*)

  implicit def convert(race: Race): gmu.Race.Race =
    gmu.Race.withName(race.c_str())

  implicit def convert(unitType: UnitType): gmu.UnitType.UnitType =
    gmu.UnitType.withName(unitType.c_str())
}

case class GameUnit(state: UnitState, unit: BwUnit)
case object Done