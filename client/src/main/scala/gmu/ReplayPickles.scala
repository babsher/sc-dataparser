package gmu

import com.mongodb.BasicDBObject
import org.xerial.snappy.Snappy

import scala.pickling._
import scala.pickling.json._
import scala.pickling.Defaults._

trait ReplayPickles {

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

  def pickle(player: ReplayPlayers): Array[Byte] = {
    compress(player.pickle.value)
  }

  def compress(s: String): Array[Byte] = {
    Snappy.compress(s.getBytes("UTF-8"))
  }

  def decompress(s: Array[Byte]): String = {
    val raw = Snappy.uncompress(s)
    new String(raw, "UTF-8")
  }

  def pickle(unit: ReplayUnit): Array[Byte] = {
    compress(unit.pickle.value)
  }

  def pickle(units: Seq[ReplayUnit]): Array[Byte] = {
    compress(units.pickle.value)
  }

  def unpickleUnit(b: Array[Byte]): Seq[ReplayUnit] = {
    decompress(b).unpickle[Seq[ReplayUnit]]
  }

  def unpicklePlayers(b: Array[Byte]): ReplayPlayers = {
    decompress(b).unpickle[ReplayPlayers]
  }

  def getKey(frame: ReplayFrame): BasicDBObject =
    new BasicDBObject("replay", frame.replay).append("frame", frame.frame)

  def getKey(players: ReplayPlayers): BasicDBObject =
    getKey(players.frame)

  def getKey(units: Seq[ReplayUnit]): BasicDBObject =
    getKey(units.head.frame)

  def getKey(u: ReplayUnit): BasicDBObject =
    getKey(u.frame).append("id", u.id)
}