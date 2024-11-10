package mnist;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Random;

public class MnistImagePipelineLoad {
    final private static Logger log = LoggerFactory.getLogger(MnistImagePipelineLoad.class);

    public static void main(String[] args) throws Exception {
        // image information
        // 28 * 28 grayscale
        // grayscale implies single channel
        int height = 28;
        int width = 28;
        int nChannels = 1;
        int rgnseed = 123;
        Random randNumGen = new Random(rgnseed);
        int batchSize = 64;
        int outputNum = 10;
        int numberEpochs = 10;

        // Define the File Paths
        File trainData = new File("C:\\Users\\Samuel Oliveira\\dl4j-examples\\src\\main\\resources\\mnist_png\\training");
        File testData = new File("C:\\Users\\Samuel Oliveira\\dl4j-examples\\src\\main\\resources\\mnist_png\\testing");

        // Define the FileSplit(PATH, ALLOWED FORMATS, random)
        FileSplit train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        FileSplit test = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);

        // Extract the parent path as the image label
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
        ImageRecordReader recordReader = new ImageRecordReader(height, width, nChannels, labelMaker);

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

        log.info("**** LOAD TRAINED MODEL ****");

        // Where to save the model
        File locationToSave = new File("C:\\Users\\Samuel Oliveira\\dl4j-examples\\src\\main\\java\\trained_mnist_model.zip");

        // ModelSerializer needs modelname, saveUpdater, Location
        // ModelSerializer.writeModel(model, locationToSave, false);

        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("**** EVALUATE MODEL ****");

        recordReader.reset();

        recordReader.initialize(test);
        DataSetIterator testIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, outputNum);
        scaler.fit(testIter);
        testIter.setPreProcessor(scaler);

        // Create Eval object with 10 possible classes
        Evaluation eval = new Evaluation(outputNum);

        while (testIter.hasNext()) {
            DataSet next = testIter.next();
            INDArray output = model.output(next.getFeatures());
            eval.eval(next.getLabels(), output);
        }

        log.info(eval.stats());

    }
}
