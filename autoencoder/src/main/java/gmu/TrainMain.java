package gmu;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.deeplearning4j.datasets.iterator.DataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.IrisDataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.LFWDataSetIterator;
import org.deeplearning4j.distributions.Distributions;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.models.featuredetectors.rbm.RBM;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.layers.OutputLayer;
import org.deeplearning4j.nn.layers.factory.DefaultLayerFactory;
import org.deeplearning4j.nn.layers.factory.LayerFactories;
import org.deeplearning4j.nn.layers.factory.PretrainLayerFactory;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.api.activation.Activations;
import org.nd4j.linalg.api.ndarray.DimensionSlice;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ndarray.SliceOp;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainMain {

  private static Logger log = LoggerFactory.getLogger(TrainMain.class);

  public static void main(String[] args) {
    System.setProperty("jcuda.home", "/usr/local/cuda-6.5");
    RandomGenerator gen = new MersenneTwister(123);

    DataSetIterator fetcher = new LFWDataSetIterator(28,28);

    MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .visibleUnit(RBM.VisibleUnit.GAUSSIAN).layerFactory(LayerFactories.getFactory(RBM.class))
            .hiddenUnit(RBM.HiddenUnit.RECTIFIED).weightInit(WeightInit.DISTRIBUTION).dist(Distributions.normal(gen, 1e-5))
            .lossFunction(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY).rng(gen)
            .learningRate(1e-3f)
            .nIn(fetcher.inputColumns()).nOut(100)
            .list(4)
            .hiddenLayerSizes(new int[]{600, 250, 200})
            .build();

    MultiLayerNetwork d = new MultiLayerNetwork(conf);
    d.pretrain(fetcher);

    System.out.println(d.output(fetcher.next().getFeatures()));
  }
}