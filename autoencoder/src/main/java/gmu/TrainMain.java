package gmu;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.deeplearning4j.datasets.iterator.DataSetIterator;
import org.deeplearning4j.datasets.iterator.DataSetPreProcessor;
import org.deeplearning4j.datasets.iterator.impl.LFWDataSetIterator;
import org.deeplearning4j.distributions.Distributions;
import org.deeplearning4j.models.featuredetectors.rbm.RBM;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.layers.factory.LayerFactories;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.api.activation.ActivationFunction;
import org.nd4j.linalg.api.activation.Activations;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainMain {

  private static Logger log = LoggerFactory.getLogger(TrainMain.class);

  public static void main(String[] args) {
    System.setProperty("jcuda.home", "/usr/local/cuda-6.5");
    final RandomGenerator gen = new MersenneTwister(1234);

    ReplayDataFetcher fetcher = new ReplayDataFetcher();

    DataSetIterator iter = new ReplayIterator(5, fetcher);
    iter.setPreProcessor(new DataSetPreProcessor() {
      @Override
      public void preProcess(DataSet toPreProcess) {
        toPreProcess.normalizeZeroMeanZeroUnitVariance();
      }
    });

    MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .constrainGradientToUnitNorm(false)
            .visibleUnit(RBM.VisibleUnit.SOFTMAX).layerFactory(LayerFactories.getFactory(RBM.class))
            .hiddenUnit(RBM.HiddenUnit.RECTIFIED).weightInit(WeightInit.DISTRIBUTION).dist(Distributions.normal(gen, 1e-7))
            .activationFunction(Activations.tanh())
            .lossFunction(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY)
            .rng(gen)
            .learningRate(1e-2f)
            .nIn(iter.inputColumns()).nOut(50)
            .list(5)
            .hiddenLayerSizes(new int[]{3000, 750, 2000, 1000})
            .override(new NeuralNetConfiguration.ConfOverride() {
              @Override
              public void override(int i, NeuralNetConfiguration.Builder builder) {
                if(i == 1) {
                  builder.weightInit(WeightInit.DISTRIBUTION).dist(Distributions.normal(gen, 1.0));
                  builder.activationFunction(Activations.rectifiedLinear());
                  builder.lossFunction(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY);
                }
              }
            })
            .build();

    MultiLayerNetwork d = new MultiLayerNetwork(conf);

    d.pretrain(iter);

    System.out.println(d.output(fetcher.next().getFeatures()));
  }
}