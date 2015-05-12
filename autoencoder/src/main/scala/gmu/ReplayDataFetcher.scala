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
  def playerSize: Int = Race.values.size + Tech.values.size + Upgrade.values.size
  def unitSize: Int = Unit.values.size + 1 + Order.values.size + 2

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

  var per = new MongoPersistence(mongo, dbName)

  totalExamples = per.numberOfExamples

  def getExamples(numExamples: Int): Seq[DataSet] = {
    val examples =

    per.findPlayers(replayId, frame, numExamples)
      .map({
        case (id: DBObject, players: ReplayPlayers) => {
          log.debug("Found id {}", id)

          val units = per.findUnits(id).toSeq
          val center = units(new Random().nextInt(units.size))
          val unitTiles = units.map(u => toTuple(u.position) -> u).toMap
          val map = per.findMap(players.frame.map.mapName)
          val mapTiles = map.cells.map(c => (c.x, c.y) -> c.height).toMap
          val vision = Array.ofDim[Double](visionTiles, visionTiles, unitSize)
          val centerPos = toTuple(center.position)

          for(x <- Range(-1*visionTiles, visionTiles);
            y  <- Range(-1*visionTiles, visionTiles)) {
            vision(x)(y) = Array.concat(
              Array(mapTiles.get((x,y)) match {
                case Some(height) => height
                case None => -1
              }),
              unitTiles.get((x,y)) match {
                case Some(u) => serialize(u)
                case None => Array.fill(unitSize)(0.0)
              }
            )
          }

          // Randomly select numUnits of units
          val rawUnits = getUnits(units)

          frame += 1
          cursor += 1

          val valuesArray = Nd4j.create(ArrayUtil.combineDouble(rawUnits))
          new DataSet(valuesArray, valuesArray)
        }
      })

    if(examples.size > numExamples) {
      replayId += 1
      frame = 0
      examples.addAll(getExamples(numExamples - examples.size))
    }
    examples
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