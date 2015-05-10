package gmu;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.deeplearning4j.datasets.iterator.DataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.LFWDataSetIterator;
import org.deeplearning4j.distributions.Distributions;
import org.deeplearning4j.models.featuredetectors.rbm.RBM;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.layers.factory.LayerFactories;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainMain {

  private static Logger log = LoggerFactory.getLogger(TrainMain.class);

  public static void main(String[] args) {
    System.setProperty("jcuda.home", "/usr/local/cuda-6.5");
    RandomGenerator gen = new MersenneTwister(1234);

    DataSetIterator fetcher = new LFWDataSetIterator(28,28);

    MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .visibleUnit(RBM.VisibleUnit.SOFTMAX).layerFactory(LayerFactories.getFactory(RBM.class))
            .hiddenUnit(RBM.HiddenUnit.BINARY).weightInit(WeightInit.DISTRIBUTION).dist(Distributions.normal(gen, 1e-5))
            .lossFunction(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY).rng(gen)
            .learningRate(1e-2f)
            .nIn(fetcher.inputColumns()).nOut(30)
            .list(5)
            .hiddenLayerSizes(new int[]{2000, 750, 1000, 30})
            .useDropConnect(true)
            .build();

    MultiLayerNetwork d = new MultiLayerNetwork(conf);

    d.pretrain(fetcher);

    System.out.println(d.output(fetcher.next().getFeatures()));
  }
}