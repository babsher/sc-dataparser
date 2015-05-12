package gmu

import com.mongodb.{DBObject, BasicDBObject, MongoClient}

import scala.collection.JavaConversions._

class MongoPersistence(val mongo: MongoClient, val dbName: String) extends ReplayConversions with ReplayPickles {
  var db = mongo.getDB(dbName)
  var units = db.getCollection("units")
  var players = db.getCollection("players")
  var maps = db.getCollection("maps")

  def mapExists(mapName: String): Boolean = {
    val map = maps.findOne(new BasicDBObject("map", mapName))
    map != null
  }

  def findReplays(): Seq[ReplayFrame] = {
    val replayIds: Seq[Int] = players.distinct("id.replay").toList.asInstanceOf[Seq[Int]]
    replayIds.map(p => players.findOne(new BasicDBObject("id.replay", p)))
      .map(_.get("players").asInstanceOf[Array[Byte]])
      .map(unpicklePlayers)
      .map(_.frame)
  }

  def findMaps(): Seq[BwMap] = {
    maps.find().asInstanceOf[java.lang.Iterable[DBObject]]
      .map(_.get("value").asInstanceOf[Array[Byte]])
      .map(unpickleMap)
      .toSeq
  }

  def insert(replayPlayer: ReplayPlayers): Unit = {
    players.insert(new BasicDBObject("id", getKey(replayPlayer)).append("players", pickle(replayPlayer)))
  }

  def insert(replayUnit: Seq[ReplayUnit]): Unit = {
    units.insert(new BasicDBObject("id", getKey(replayUnit)).append("units", pickle(replayUnit)))
  }

  def insert(bwMap: BwMap): Unit = {
    maps.insert(new BasicDBObject("map", bwMap.replayMap.mapName).append("value", pickle(bwMap)))
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