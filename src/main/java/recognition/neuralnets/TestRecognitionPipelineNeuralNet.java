package recognition.neuralnets;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.InvocationType;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Random;

public class TestRecognitionPipelineNeuralNet {
    private static Logger log = LoggerFactory.getLogger(TestRecognitionPipelineNeuralNet.class);

    public static void main(String[] args) throws Exception {
        // image information
        // 28 * 28 grayscale
        // grayscale implies single channel
        int imageHeight = 64;
        int imageWidth = 64;
        int nChannels = 1;
        int rgnseed = 123;
        Random randNumGen = new Random(rgnseed);
        int batchSize = 64;
        int outputNum = 12;
        int numberEpochs = 1;

        // Define the File Paths
        File trainData = new File("C:\\Users\\Samuel Oliveira\\dl4j-examples\\src\\main\\resources\\faces_jpg\\training");
        File testData = new File("C:\\Users\\Samuel Oliveira\\dl4j-examples\\src\\main\\resources\\faces_jpg\\testing");

        // Define the FileSplit(PATH, ALLOWED FORMATS, random)
        FileSplit train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        FileSplit test = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);

        // Extract the parent path as the image label
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
        ImageRecordReader recordReader = new ImageRecordReader(imageHeight, imageWidth, nChannels, labelMaker);

//        System.out.println(train.length());

        // Initialize the record reader
        // add a listener, to extract the name
        recordReader.initialize(train);
        // recordReader.setListeners(new LogRecordListener());

        // DataSet Iterator
        DataSetIterator dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, outputNum);

        // Scale pixel values to 0 - 1
        DataNormalization scaler = new ImagePreProcessingScaler(0,1);
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);

        // Build Our Neural Network
        log.info("**** BUILD MODEL ****");
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(rgnseed)
                .l2(0.0005)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(1e-3))
                .list()
                .layer(0, new ConvolutionLayer.Builder(5,5)
                        //nIn and nOut specify depth. nIn here is the nChannels and nOut is the number of filters to be applied
                        .nIn(nChannels)
                        .stride(1,1)
                        .nOut(20)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(new SubsamplingLayer.Builder(PoolingType.MAX)
                        .kernelSize(2,2)
                        .stride(2,2)
                        .build())
                .layer(new ConvolutionLayer.Builder(5, 5)
                        //Note that nIn need not be specified in later layers
                        .stride(1,1)
                        .nOut(50)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(new SubsamplingLayer.Builder(PoolingType.MAX)
                        .kernelSize(2,2)
                        .stride(2,2)
                        .build())
                .layer(new DenseLayer.Builder().activation(Activation.RELU)
                        .nOut(500).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(outputNum)
                        .activation(Activation.SOFTMAX)
                        .build())
                .setInputType(InputType.convolutionalFlat(imageHeight,imageWidth,1)) //See note below
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        model.setListeners(new ScoreIterationListener(10), new EvaluativeListener(dataIter, 1, InvocationType.EPOCH_END)); //Print score every 10 iterations and evaluate on test set every epoch
        model.fit(dataIter, numberEpochs);

    }
}
