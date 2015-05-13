package gmu

import com.mongodb.{DBObject, BasicDBObject, MongoClient}

import scala.collection.JavaConversions._

class MongoPersistence(val mongo: MongoClient, val dbName: String)
      extends ReplayConversions
      with ReplayPickles {

  val db = mongo.getDB(dbName)
  val units = db.getCollection("units")
  val players = db.getCollection("players")
  val maps = db.getCollection("maps")
  val unitTypesCol = db.getCollection("unitTypes")

  def mapExists(mapName: String): Boolean = {
    val map = maps.findOne(new BasicDBObject("map", mapName))
    map != null
  }

  val unitTypesIsEmpty: Boolean = {
    unitTypesCol.find().count() == 0
  }

  def insert(bwUnitType: Map[Unit.UnitType, BwUnitType]): Unit = {
    unitTypesCol.insert(new BasicDBObject("_id", 1).append("types", pickle(bwUnitType)))
  }

  def unitTypes: Map[Unit.UnitType, BwUnitType] = {
    unpickleUnitTypes(unitTypesCol
      .findOne(new BasicDBObject("_id", 1))
      .get("types")
      .asInstanceOf[Array[Byte]])
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

  def findUnits(id: DBObject): Iterable[ReplayUnit] = {
    units.findOne(
      new BasicDBObject("id.replay", id.get("replay"))
      .append("id.frame", id.get("frame")))
      .asInstanceOf[java.lang.Iterable[DBObject]]
      .map(_.get("units").asInstanceOf[Array[Byte]])
      .flatMap(unpickleUnit)
  }

  def findMap(mapName: String): BwMap = {
    val map = maps.findOne(new BasicDBObject("map", mapName))
      .get("value").asInstanceOf[Array[Byte]]
    unpickleMap(map)
  }

  def findPlayers(replayId: Int, frame: Int, limit: Int): Iterable[(DBObject, ReplayPlayers)] = {
    players.find(new BasicDBObject("id.replay", replayId))
      .sort(new BasicDBObject("id.frame", 1))
      .skip(frame)
      .limit(limit)
      .asInstanceOf[java.lang.Iterable[DBObject]]
      .map(obj =>
        (obj.get("id").asInstanceOf[DBObject],
          unpicklePlayers(obj.get("players").asInstanceOf[Array[Byte]])))
  }

  def numberOfExamples: Int = {
    players.find().count()
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