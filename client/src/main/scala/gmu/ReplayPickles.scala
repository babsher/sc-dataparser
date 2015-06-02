package gmu

import org.xerial.snappy.Snappy

import scala.pickling._
import scala.pickling.json._
import scala.pickling.Defaults._

trait ReplayPickles extends ReplaySerialization {

  implicit val velPickler = Pickler.generate[gmu.Velocity]
  implicit val velUnpickler = Unpickler.generate[gmu.Velocity]

  implicit val posPickler = Pickler.generate[gmu.RPosition]
  implicit val posUnpickler = Unpickler.generate[gmu.RPosition]

  implicit val playerPickler = Pickler.generate[gmu.ReplayPlayer]
  implicit val playerUnpickler = Unpickler.generate[gmu.ReplayPlayer]

  implicit val replayFramePickler = Pickler.generate[ReplayFrame]
  implicit val replayFrameUnpickler = Unpickler.generate[ReplayFrame]

  implicit val mapSizePickler = Pickler.generate[MapSize]
  implicit val mapSizeUnpickler = Unpickler.generate[MapSize]

  implicit val unitPickler = Pickler.generate[gmu.ReplayUnit]
  implicit val unitUnpickler = Unpickler.generate[gmu.ReplayUnit]

  implicit val replayersPickler = Pickler.generate[gmu.ReplayPlayers]
  implicit val replayersUnplicker = Unpickler.generate[gmu.ReplayPlayers]

  implicit val mapCellPickler = Pickler.generate[gmu.MapCell]
  implicit val mapCellUnpickler = Unpickler.generate[gmu.MapCell]

  implicit val bwMapPickler = Pickler.generate[gmu.BwMap]
  implicit val bwMapUnpickler = Unpickler.generate[gmu.BwMap]

  implicit val bwUnitTypePickler = Pickler.generate[gmu.BwUnitType]
  implicit val bwUnitTypeUnpickler = Unpickler.generate[gmu.BwUnitType]

  def compress(s: String): Array[Byte] = {
    Snappy.compress(s.getBytes("UTF-8"))
  }

  def decompress(s: Array[Byte]): String = {
    val raw = Snappy.uncompress(s)
    new String(raw, "UTF-8")
  }

  override def pickle(player: ReplayPlayers): Array[Byte] = {
    compress(player.pickle.value)
  }

  override def pickle(bwUnitType: Map[Unit.UnitType, BwUnitType]): Array[Byte] = {
    compress(bwUnitType.pickle.value)
  }

  override def unpickleUnitTypes(s: Array[Byte]): Map[Unit.UnitType, BwUnitType] = {
    decompress(s).unpickle[Map[Unit.UnitType, BwUnitType]]
  }

  override def pickle(unit: ReplayUnit): Array[Byte] = {
    compress(unit.pickle.value)
  }

  override def pickle(units: Seq[ReplayUnit]): Array[Byte] = {
    compress(units.pickle.value)
  }

  override def pickle(bwMap: BwMap): Array[Byte] = {
    compress(bwMap.pickle.value)
  }

  override def unpickleMap(b: Array[Byte]): BwMap = {
    decompress(b).unpickle[BwMap]
  }

  override def unpickleUnit(b: Array[Byte]): Seq[ReplayUnit] = {
    decompress(b).unpickle[Seq[ReplayUnit]]
  }

  override def unpicklePlayers(b: Array[Byte]): ReplayPlayers = {
    decompress(b).unpickle[ReplayPlayers]
  }
}