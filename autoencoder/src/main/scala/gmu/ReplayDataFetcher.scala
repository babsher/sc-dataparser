package gmu

import java.util

import com.mongodb._
import org.deeplearning4j.datasets.fetchers.BaseDataFetcher
import org.deeplearning4j.datasets.iterator.BaseDatasetIterator
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.util.ArrayUtil
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.pickling._
import scala.pickling.json._
import scala.util.Random

object ReplayDataFetcher {
  def playerSize: Int = {
    Race.values.size + Tech.values.size + Upgrade.values.size
  }

  def unitSize: Int = {
    Unit.values.size + 1 + Order.values.size + 2
  }

  val numUnits = 10
}

class ReplayDataFetcher extends BaseDataFetcher with ReplayPickles {
  val log = LoggerFactory.getLogger(classOf[ReplayDataFetcher])
  var replayId = 1
  var frame = 1500

  log.info("Player size {}", ReplayDataFetcher.playerSize)
  log.info("Unit size {}*{}", ReplayDataFetcher.numUnits, ReplayDataFetcher.unitSize + "=" + (ReplayDataFetcher.numUnits * ReplayDataFetcher.unitSize).toString)

  inputColumns = 2 * ReplayDataFetcher.playerSize + ReplayDataFetcher.numUnits * ReplayDataFetcher.unitSize
  numOutcomes = inputColumns
  val dbName = "sc"

  val mongo = new MongoClient("localhost", MongoClientOptions.builder()
    .connectionsPerHost(16)
    .build())

  val db = mongo.getDB(dbName)
  val unitsCol = db.getCollection("units")
  val playersCol = db.getCollection("players")

  totalExamples = mongo.getDB(dbName).getCollection("players").find().count()

  def getExamples(numExamples: Int): java.util.ArrayList[DataSet] = {
    val examples = new java.util.ArrayList[DataSet]

    val cur = playersCol.find(new BasicDBObject("id.replay", replayId))
      .sort(new BasicDBObject("id.frame", 1))
      .skip(frame)
      .limit(numExamples)
    while(cur.hasNext) {
      val replayPlayer = cur.next()
      val id: DBObject = replayPlayer.get("id").asInstanceOf[DBObject]
      log.debug("Found id {}", id)

      val unitsCur = unitsCol.findOne(
        new BasicDBObject("id.replay", id.get("replay"))
          .append("id.frame", id.get("frame")))

      val rawPlayers = makePlayer(replayPlayer).players
        .sortWith(_.id > _.id)
        .subList(0, 2)
        .map(serialize)

      val units = makeUnit(unitsCur)

      // Randomly select numUnits of units
      val rawUnits = getUnits(units)

      val data = new java.util.ArrayList[Array[Double]]()
      data.addAll(rawPlayers)
      data.addAll(rawUnits)

      frame += 1
      cursor += 1

      val valuesArray = Nd4j.create(ArrayUtil.combineDouble(data))
      val ds = new DataSet(valuesArray, valuesArray)
      examples.add(ds)
    }

    if(examples.size() > numExamples) {
      replayId += 1
      frame = 0
    }
    examples
  }

  override def fetch(numExamples: Int): Unit = {
    val examples = getExamples(numExamples)

    log.debug("Initializing {} examples", examples.size())
    initializeCurrFromList(examples)
  }

  def getUnits(units: Seq[ReplayUnit]): Seq[Array[Double]] = {
    val filtered = units.filter(u => u.race == Race.Zerg || u.race == Race.Protoss || u.race == Race.Terran)
    if(filtered.size > ReplayDataFetcher.numUnits) {
      Random.shuffle(filtered).subList(0, ReplayDataFetcher.numUnits)
        .map(serialize)
    } else {
      val units = Random.shuffle(filtered).subList(0, ReplayDataFetcher.numUnits)
        .map(serialize)
      val zeros = for(x <- Range(0, ReplayDataFetcher.numUnits - filtered.size))
        yield Array.fill(ReplayDataFetcher.unitSize)(0.0)
      units ++ zeros
    }
  }

  def makeUnit(u: DBObject): Seq[ReplayUnit] = {
    val raw = u.get("units").asInstanceOf[Array[Byte]]
    unpickleUnit(raw)
  }

  def makePlayer(p: DBObject): ReplayPlayers = {
    val raw = p.get("players").asInstanceOf[Array[Byte]]
    unpicklePlayers(raw)
  }

  def serialize(u: ReplayUnit): Array[Double] = {
    category(Unit.values, u.unitType) ++
//    Array[Double](u.position.x, u.position.y) ++
    Array[Double](u.hp.toDouble / u.initalHp.toDouble) ++
    category(Order.values, u.order)
  }

  def serialize(p: ReplayPlayer): Array[Double] = {
    val tech = p.tech.toList
      .map(t => binary(t._2))
    val upgrades = p.upgrades.toList
      .map(t => t._2.toDouble)

    val playerRaw = category(Race.values, p.race) ++ Array.concat(tech.toArray, upgrades.toArray)
    playerRaw
  }

  def category[E](values: Seq[E], value: E): Array[Double] = {
    values.map(v => binary(value.equals(v))).toArray
  }

  def binary(bool: Boolean): Double = {
    if(bool) 1.toDouble else -1.toDouble
  }
}

class ReplayIterator(batch: Int, featch: ReplayDataFetcher) extends BaseDatasetIterator(batch, featch.totalExamples(), featch) {
}