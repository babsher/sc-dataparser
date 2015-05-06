package gmu

import com.google.common.collect.Lists
import org.deeplearning4j.distributions.Distributions
import org.deeplearning4j.models.featuredetectors.rbm.RBM
import org.nd4j.linalg.api.activation.Activations
import org.apache.commons.math3.random.MersenneTwister
import org.apache.commons.math3.random.RandomGenerator
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.util.ArrayUtil

import scala.collection.JavaConversions._
import com.mongodb._
import org.deeplearning4j.datasets.fetchers.BaseDataFetcher
import org.deeplearning4j.datasets.iterator.{DataSetFetcher, BaseDatasetIterator, DataSetIterator}
import org.deeplearning4j.datasets.iterator.impl.{IrisDataSetIterator, LFWDataSetIterator}
import org.deeplearning4j.eval.Evaluation
import org.deeplearning4j.nn.api.{OptimizationAlgorithm, LayerFactory}
import org.deeplearning4j.nn.conf.{NeuralNetConfiguration, MultiLayerConfiguration}
import org.deeplearning4j.nn.layers.factory.{PretrainLayerFactory, LayerFactories}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.lossfunctions.LossFunctions
import org.slf4j.LoggerFactory

import scala.pickling._
import scala.pickling.static._
import scala.pickling.binary._
import scala.pickling.Defaults._
import scala.pickling.shareNothing._

class ReplayIterator(batch: Int, numExamples: Int) extends BaseDatasetIterator(batch, numExamples, new ReplayDataFetcher) {
}

class ReplayDataFetcher extends BaseDataFetcher with ReplayPickles {
  val log = LoggerFactory.getLogger(classOf[ReplayDataFetcher])
  var replayId = 2
  var frame = 1

  val mongo = new MongoClient("localhost", MongoClientOptions.builder()
    .connectionsPerHost(32)
    .build())

  override def fetch(numExamples: Int): Unit = {
    val db = mongo.getDB("sc")
    val unitsCol = db.getCollection("units")
    val playersCol = db.getCollection("players")

    val examples = new java.util.ArrayList[DataSet]
    val d = new DataSet()

    val cur = playersCol.find(new BasicDBObject("id.replay", replayId))
      .sort(new BasicDBObject("id.frame", 1))
      .skip(frame)
      .limit(numExamples)
    while(cur.hasNext) {
      val replayPlayer = cur.next()
      val id: DBObject = replayPlayer.get("id").asInstanceOf[DBObject]
      log.debug("Found id {}", id)
      val unitsCur = unitsCol.find(
        new BasicDBObject("id.replay", id.get("replay"))
          .append("id.frame", id.get("frame")))
      log.debug("Found {} units", unitsCur.count())

      val rawPlayers = players(replayPlayer).players
        .sortWith(_.id > _.id)
        .map(serialize)

      val u: Seq[ReplayUnit] = Iterator
        .continually(units(unitsCur.next()))
        .dropWhile(_ => unitsCur.hasNext)
        .toList

      val rawUnits = u.filter(u => u.race == Race.Zerg || u.race == Race.Protoss || u.race == Race.Terran)
        .sortWith((e1, e2) => e1.position.y > e2.position.y && e1.position.x > e1.position.x)
        .map(serialize)

      rawPlayers.addAll(rawUnits)

      frame += 1
      d.addFeatureVector(Nd4j.create(ArrayUtil.combineDouble(rawPlayers)))
    }

    initializeCurrFromList(examples)
  }

  def units(u: DBObject): ReplayUnit = {
    val raw: Array[Byte] = u.get("units").asInstanceOf[Array[Byte]]
    raw.unpickle[ReplayUnit]
  }

  def players(p: DBObject): ReplayPlayers = {
    val raw: Array[Byte] = p.get("players").asInstanceOf[Array[Byte]]
    raw.unpickle[ReplayPlayers]
  }

  def serialize(u: ReplayUnit): Array[Double] = {
    Array(
      Unit.values.indexOf(u.unitType),
      u.hp / u.initalHp,
      Order.values.indexOf(u.order)
    )
  }

  def serialize(p: ReplayPlayer): Array[Double] = {
    val tech = p.tech.toList
      .sortWith((e1, e2) => Tech.values.indexOf(e1._1) > Tech.values.indexOf(e2._1))
      .map(t => if(t._2) 1.toDouble else -1.toDouble)

    val upgrades = p.upgrades.toList
      .sortWith((e1, e2) => Upgrade.values.indexOf(e1._1) > Upgrade.values.indexOf(e2._1))
      .map(t => t._2.toDouble)

    Array.concat(tech.toArray, upgrades.toArray)
  }
}