package gmu

import com.mongodb.BasicDBObject

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

  def pickle(player: ReplayPlayers): String = {
    player.pickle.value
  }

  def pickle(unit: ReplayUnit): String = {
    unit.pickle.value
  }

  def unpickleUnit(b: String): ReplayUnit = {
    b.unpickle[ReplayUnit]
  }

  def unpicklePlayers(b: String): ReplayPlayers = {
    b.unpickle[ReplayPlayers]
  }

  def getKey(frame: ReplayFrame): BasicDBObject =
    new BasicDBObject("replay", frame.replay).append("frame", frame.frame)

  def getKey(players: ReplayPlayers): BasicDBObject =
    getKey(players.frame)

  def getKey(u: ReplayUnit): BasicDBObject =
    getKey(u.frame).append("id", u.id)
}