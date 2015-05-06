package gmu

import scala.collection.JavaConversions._
import com.mongodb.{WriteConcern, MongoClientOptions, MongoClient}
import org.deeplearning4j.datasets.fetchers.BaseDataFetcher
import org.deeplearning4j.datasets.iterator.{DataSetFetcher, BaseDatasetIterator, DataSetIterator}
import org.deeplearning4j.datasets.iterator.impl.{IrisDataSetIterator, LFWDataSetIterator}
import org.deeplearning4j.eval.Evaluation
import org.deeplearning4j.nn.api.{OptimizationAlgorithm, LayerFactory}
import org.deeplearning4j.nn.conf.{NeuralNetConfiguration, MultiLayerConfiguration}
import org.deeplearning4j.nn.layers.factory.{PretrainLayerFactory, LayerFactories}
import org.deeplearning4j.nn.layers.feedforward.rbm.RBM
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.lossfunctions.LossFunctions
import org.slf4j.LoggerFactory

object Main extends App {
  val log = LoggerFactory.getLogger(classOf[gmu.Main])

  Nd4j.MAX_SLICES_TO_PRINT = -1
  Nd4j.MAX_ELEMENTS_PER_SLICE = -1
  val conf = new NeuralNetConfiguration.Builder()
    .iterations(100)
    .layerFactory(new PretrainLayerFactory(classOf[RBM]))
    .weightInit(WeightInit.DISTRIBUTION).dist(Nd4j.getDistributions().createUniform(0,1))
    .activationFunction("tanh").momentum(0.9)
    .optimizationAlgo(OptimizationAlgorithm.LBFGS)
    .constrainGradientToUnitNorm(true).k(1).regularization(true).l2(2e-4)
    .visibleUnit(RBM.VisibleUnit.GAUSSIAN).hiddenUnit(RBM.HiddenUnit.RECTIFIED)
    .lossFunction(LossFunctions.LossFunction.RMSE_XENT)
    .learningRate(1e-1f)
    .iterationListener(new ScoreIterationListener(2))
    .nIn(4).nOut(3).list(2)
    .hiddenLayerSizes(3)
    .build()

  val d = new MultiLayerNetwork(conf)

  val iter = new IrisDataSetIterator(150, 150)

  val next = iter.next()

  Nd4j.writeTxt(next.getFeatureMatrix,"iris.txt","\t")

  next.normalizeZeroMeanZeroUnitVariance()

  val testAndTrain = next.splitTestAndTrain(110)
  val train = testAndTrain.getTrain

  d.fit(train)

  val test = testAndTrain.getTest


  val eval = new Evaluation()
  val output = d.output(test.getFeatureMatrix)
  eval.eval(test.getLabels,output)
  log.info("Score " + eval.stats())
}

class Main {
  // needed for logging
}

class ReplayIterator(batch: Int, numExamples: Int, fetcher: DataSetFetcher) extends BaseDatasetIterator(batch, numExamples, fetcher) {
}

class ReplayDataFetcher extends BaseDataFetcher {
  val mongo = new MongoClient("192.168.1.250", MongoClientOptions.builder()
    .connectionsPerHost(32)
    .build())

  override def fetch(numExamples: Int): Unit = {
    val db = mongo.getDatabase("sc")
    val units = db.getCollection("units")
    val players = db.getCollection("players")

    val examples = java.util.ArrayList[DataSet]

    val d = new DataSet()

    initializeCurrFromList(examples)
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