package recognition.neuralnets;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import recognition.Helpers.Helper;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class RecognitionPipelineLoadChooser {
    final private static Logger log = LoggerFactory.getLogger(RecognitionPipelineLoadChooser.class);

    static String filechooser = "";
    static INDArray output;
    static List<Integer> labelList;

    static Helper helper = new Helper();

    public static String fileChooser() {
        JFileChooser fc = new JFileChooser();
        int ret = fc.showOpenDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            String filename = file.getAbsolutePath();
            return filename;
        }
        else {
            return null;
        }
    }

    public static void loadModel(String filechooser) throws Exception {
        int height = 64;
        int width = 64;
        int nChannels = 1;
        // recordReader.getLabels();
        labelList = Arrays.asList(2,3,7,1,6,4,0,5,8,9);
        File locationToSave = new File("C:\\Users\\Samuel Oliveira\\dl4j-examples\\src\\main\\java\\faces_recognition_model_12.zip");
        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(locationToSave);
        log.info("**********TEST YOUR IMAGE AGAINST SAVED NETWORK***********");
        // FileChooser is a string we will need a file
        File file = new File(filechooser);
        // Use NativeImageLoader to convert to numerical matrix
        NativeImageLoader loader = new NativeImageLoader(height, width, nChannels);
        // Get the image into an INDarray
        INDArray image = loader.asMatrix(file);
        // 0 - 255
        // 0 - 1
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(image);
        // Pass through to neural Net
        output = model.output(image);
    }

    public static void showResults(String filechooser) {
        System.out.println("## the FILE CHOSEN WAS: " + filechooser);
        System.out.println("## the Neural Nets Prediction ##");
        System.out.println("## List of probabilities per label ##");
        System.out.println("## List of labels in Order ##");
        System.out.println(output.toString());
        System.out.println(labelList.toString());
        System.out.println("Predicted Value: " + output.argMax());
    }

    // inputs a INDArray preprocessed image from camera to the pre-trained model
    // and returns the model output
    public INDArray testModelImage(INDArray cameraPreprocessedImage) throws Exception {
        // recordReader.getLabels();
        labelList = Arrays.asList(2, 3, 7, 1, 6, 4, 0, 5, 8, 9);
        File locationToSave = new File("C:\\Users\\Samuel Oliveira\\dl4j-examples\\src\\main\\java\\faces_recognition_model_014.zip");
        // load the pre-trained model
        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(locationToSave);
        // show some log infos
        log.info("********** TEST CAMERA IMAGE ***********");
        // Pass through to neural Net
        output = model.output(cameraPreprocessedImage);
        // Return the model output to the given image
        return output;
    }
}
