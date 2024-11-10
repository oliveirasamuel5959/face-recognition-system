package recognition.Helpers;

import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.cpu.nativecpu.bindings.Nd4jCpu;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Helper {
    private String pathToTrainImages = "C:\\Users\\Samuel Oliveira\\dl4j-examples\\src\\main\\resources\\faces_jpg\\training";
    private int height = 64;
    private int width = 64;
    private int nChannels = 1;
    public boolean matListIsNotEmpty(ArrayList<Mat> matList) {
        // check if the list of images is not empty
        // return false otherwise
        if(matList.isEmpty())
            return false;
        else
            return true;
    }

    public INDArray imagePreProcessing(Mat image) throws IOException {
        // initialize Mat variables
        Mat imageResized = new Mat();
        Mat imageGrayScale = new Mat();
        Mat imageBlurFilter = new Mat();
        // Resize the image to 64 x 64
        Size size = new Size(64,64);
        Imgproc.resize(image, imageResized, size, 0, 0, Imgproc.INTER_AREA);
        // Convert image to gray scale
        Imgproc.cvtColor(imageResized, imageGrayScale, Imgproc.COLOR_RGB2GRAY);
        // apply gaussian blur filter with a (3, 3) kernel size to resized image
        Imgproc.GaussianBlur(imageResized, imageBlurFilter, new Size(3 ,3), 0);
        // Use NativeImageLoader to convert to numerical matrix
        NativeImageLoader loader = new NativeImageLoader(height, width, nChannels);
        // Get the image into an INDarray
        INDArray image_array = loader.asMatrix(imageBlurFilter);
        // 0 - 255
        // 0 - 1
        // Normalize the image to values between 0 and 1
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(image_array);
        // return the preprocessed INDArray image
        return image_array;
    }

    public Map getCategoricalIndexes() {
        Map<Integer, String> map = new HashMap<>();
        File[] files = new File(pathToTrainImages).listFiles();
        Integer count_indexes = 0;
        for (File file: files) {
            if (file.isDirectory()){
                map.put(count_indexes, file.getName());
                count_indexes++;
            }
            else {
                System.out.println("File: " + file.getName());
            }
        }
        return map;
    }
}
