package gmu

import com.twitter.chill.{KryoPool, ScalaKryoInstantiator}
import gmu.Unit.UnitType

trait ReplayChill extends ReplaySerialization {

  val kryo = KryoPool.withByteArrayOutputStream(10, new ScalaKryoInstantiator())

  override def pickle(player: ReplayPlayers): Array[Byte] = {
    kryo.toBytesWithClass(player)
  }

  override def pickle(bwUnitType: Map[UnitType, BwUnitType]): Array[Byte] = {
    kryo.toBytesWithClass(bwUnitType)
  }

  override def pickle(unit: ReplayUnit): Array[Byte] = {
    kryo.toBytesWithClass(unit)
  }

  override def pickle(units: Seq[ReplayUnit]): Array[Byte] = {
    kryo.toBytesWithClass(units)
  }

  override def pickle(bwMap: BwMap): Array[Byte] = {
    kryo.toBytesWithClass(bwMap)
  }

  override def unpickleMap(b: Array[Byte]): BwMap = {
    kryo.fromBytes(b).asInstanceOf[BwMap]
  }

  override def unpickleUnit(b: Array[Byte]): Seq[ReplayUnit] = {
    kryo.fromBytes(b).asInstanceOf[Seq[ReplayUnit]]
  }

  override def unpicklePlayers(b: Array[Byte]): ReplayPlayers = {
    kryo.fromBytes(b).asInstanceOf[ReplayPlayers]
  }

  override def unpickleUnitTypes(s: Array[Byte]): Map[UnitType, BwUnitType] = {
    kryo.fromBytes(s).asInstanceOf[Map[UnitType, BwUnitType]]
  }
}
