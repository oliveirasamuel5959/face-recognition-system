package mnist;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.records.listener.impl.LogRecordListener;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.util.Random;

public class MnistImagePipeline {
    private static Logger log = LoggerFactory.getLogger(MnistImagePipeline.class);

    public static void main(String[] args) throws Exception {
        // image information
        // 28 * 28 grayscale
        // grayscale implies single channel
        int height = 28;
        int width = 28;
        int channels = 1;
        int rgnseed = 123;
        Random randNumGen = new Random(rgnseed);
        int batchSize = 1;
        int outputNum = 10;

        // Define the File Paths
        File trainData = new File("C:\\Users\\Samuel Oliveira\\dl4j-examples\\src\\main\\resources\\mnist_png\\training");
        File testData = new File("C:\\Users\\Samuel Oliveira\\dl4j-examples\\src\\main\\resources\\mnist_png\\testing");

        // Define the FileSplit(PATH, ALLOWED FORMATS, random)
        FileSplit train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        FileSplit test = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);

        // Extract the parent path as the image label
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, labelMaker);

        // Initialize the record reader
        // add a listener, to extract the name
        recordReader.initialize(train);
        recordReader.setListeners(new LogRecordListener());

        // DataSet Iterator
        DataSetIterator dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, outputNum);

        // Scale pixel values to 0 - 1
        DataNormalization scaler = new ImagePreProcessingScaler(0,1);
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);

        for (int i=1; i<3; i++) {
            DataSet ds = dataIter.next();
            System.out.println(ds);
            System.out.println(dataIter.getLabels());
        }
    }
}
