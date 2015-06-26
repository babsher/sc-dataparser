package gmu

import com.mongodb._
import org.deeplearning4j.datasets.fetchers.BaseDataFetcher
import org.deeplearning4j.datasets.iterator.BaseDatasetIterator
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.factory.Nd4j
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.util.Random

object ReplayDataFetcher {
  def playerSize: Int = Race.values.size + Tech.values.size + Upgrade.values.size
  def unitSize: Int = 4

  val numUnits = 10
  val vision = 320 // px
  val visionTiles = vision / 32
}

class ReplayDataFetcher extends BaseDataFetcher with ReplayPickles {
  import ReplayDataFetcher._

  val log = LoggerFactory.getLogger(classOf[ReplayDataFetcher])
  var replayId = 1
  var frame = 1

  inputColumns =  visionTiles * visionTiles * unitSize
  numOutcomes = inputColumns
  val dbName = "sc"

  val mongo = new MongoClient("localhost", MongoClientOptions.builder()
    .connectionsPerHost(16)
    .build())

  val per = new MongoPersistence(mongo, dbName)
  val unitTypes = per.unitTypes

  totalExamples = per.numberOfExamples

  def getExamples(numExamples: Int): Seq[DataSet] = {
    val examples = per.findPlayers(replayId, frame, numExamples + 1)
      .map({
        case (id: DBObject, players: ReplayPlayers) => {
          val units = per.findUnits(id).toSeq
          val center = units(new Random().nextInt(units.size))
          val unitTiles = units.map(u => toTuple(u.position) -> u).toMap
          val map = per.findMap(players.frame.map.mapName)
          val mapTiles = map.cells.map(c => (c.x, c.y) -> c.height).toMap
          val vision = Array.ofDim[Double](visionTiles * 2, visionTiles * 2, unitSize)
          val centerPos = (center.position.x / 32, center.position.y / 32)

          for(x <- Range(-1*visionTiles, visionTiles);
            y  <- Range(-1*visionTiles, visionTiles)) {
            vision(x + visionTiles)(y + visionTiles) = Array.concat(
              Array(mapTiles.get(add(centerPos, (x,y))) match {
                case Some(height) => height
                case None => -1
              }),
              unitTiles.get(add(centerPos, (x,y))) match {
                case Some(u) => serialize(u)
                case None => Array.fill(unitSize)(0.0)
              }
            )
          }

          frame += 1
          cursor += 1

          val valuesArray = Nd4j.create(flatten(vision))
          new DataSet(valuesArray, valuesArray)
        }
      })

    if(examples.size > numExamples) {
      replayId += 1
      frame = 0
      getExamples(numExamples - examples.size) ++ examples
    } else {
      examples.toSeq
    }
  }

  def flatten(a: Array[Array[Array[Double]]]): Array[Double] = {
    val xDim = a.length
    val yDim = a(0).length
    val zDim = a(0)(0).length
    val ret = new Array[Double](xDim * yDim * zDim)
    var count = 0
    for(x <- Range(0, xDim);
      y <- Range(0, yDim);
      z <- Range(0, zDim)) {
      ret(count) = a(x)(y)(z)
      count = count + 1
    }
    ret
  }

  def add(x: (Int, Int), y: (Int, Int)): (Int, Int) = (x._1 + y._1, x._2 + y._2)

  override def fetch(numExamples: Int): Unit = {
    val examples = getExamples(numExamples)

    log.debug("Initializing {} examples", examples.size)
    initializeCurrFromList(examples)
  }

  def getUnits(units: Seq[ReplayUnit]): Seq[Array[Double]] = {
    val filtered = units.filter(u => u.race == Race.Zerg || u.race == Race.Protoss || u.race == Race.Terran)
    if(filtered.size > ReplayDataFetcher.numUnits) {
      Random.shuffle(filtered).slice(0, ReplayDataFetcher.numUnits)
        .map(serialize)
    } else {
      val units = Random.shuffle(filtered).slice(0, ReplayDataFetcher.numUnits)
        .map(serialize)
      val zeros = for(x <- Range(0, ReplayDataFetcher.numUnits - filtered.size))
        yield Array.fill(ReplayDataFetcher.unitSize)(0.0)
      units ++ zeros
    }
  }

  def serialize(unit: ReplayUnit): Array[Double] = {
    val u: BwUnitType = unitTypes.get(unit.unitType).get
    Array[Double](
        binary(u.isFlyer),
        binary(u.groundWeapon.targetsAir || u.airWeapon.targetsAir),
        binary(u.groundWeapon.targetsGround || u.airWeapon.targetsGround),
        unit.hp.toDouble / unit.initalHp.toDouble)
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