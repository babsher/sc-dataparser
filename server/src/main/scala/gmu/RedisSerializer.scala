package gmu

import akka.util.ByteString
import scala.pickling._
import scala.pickling.binary._
import scala.pickling.static._
import scala.pickling.Defaults._


trait ReplayPickles {
  implicit val racePickler = Pickler.generate[gmu.Race.RaceType]
  implicit val raceUnplicker = Unpickler.generate[gmu.Race.RaceType]

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
}

trait RedisSerializer {

//  implicit val unitFrameSerializer = new ByteStringFormatter[ReplayUnit] with ReplayPickles {
//
//    override def deserialize(bs: ByteString): ReplayUnit = {
//      bs.toArray.unpickle[ReplayUnit]
//    }
//
//    override def serialize(data: ReplayUnit): ByteString = {
//      ByteString(data.pickle.value)
//    }
//  }
//
//  implicit val replayFrameSerializer = new ByteStringFormatter[ReplayPlayers] with ReplayPickles {
//
//    override def deserialize(bs: ByteString): ReplayPlayers = {
//      bs.toArray.unpickle[ReplayPlayers]
//    }
//
//    override def serialize(data: ReplayPlayers): ByteString = {
//      ByteString(data.pickle.value)
//    }
//  }

  def getKey(frame: ReplayFrame): String =
    frame.replay + "" + frame.frame + frame.map

  def getKey(players: ReplayPlayers): String =
    "p" + getKey(players.frame)

  def getKey(u: ReplayUnit): String =
    "u" + getKey(u.frame)
}