package recognition;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import recognition.Helpers.Helper;
import recognition.Helpers.User;
import recognition.JDBC.JDBC;
import recognition.neuralnets.RecognitionPipelineLoadChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class CameraRecognition extends JFrame {

    // swing components
    private JButton btnCancel;
    private JButton btnStart;
    private JLabel cameraScreen;
    private JPanel mainCameraPane;
    private JTextField tf_jobid;

    // camera variables
    private VideoCapture capture;
    private Mat image;
    // INDArray image variable to store the preprocessed image
    private INDArray imagePreProcessed;
    private INDArray modelOutput;
    private Map categoricalMap;
    private double actualPredValue = 0.0;
    private double minPredValue = 0.85;
    private int choice = 10;
    private int maxTries = 20;
    JDBC db = new JDBC();
    RecognitionPipelineLoadChooser nnModel = new RecognitionPipelineLoadChooser();
    Helper helper = new Helper();
    User user = new User();
    String jobId = "";

    public CameraRecognition() {
        setLayout(null);
        setContentPane(mainCameraPane);
        setTitle("Camera Recognition");
        setSize(640, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e ) {
                super.windowClosing(e);
                capture.release();
                image.release();
                System.exit(0);
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jobId = tf_jobid.getText();
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    user = db.getActiveUser(jobId);
                                    System.out.println(user.getUserFirstName());
                                    if (user.getUserFirstName() != null)
                                        startCamera();
                                    else
                                        JOptionPane.showMessageDialog(null, "User not found!",
                                                "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
            }
        });
    }


    public void startCamera() throws Exception {
        capture = new VideoCapture(0); // initialize a new videoCapture
        image = new Mat(); // Mat image variable to store images from camera
        byte[] imageData; // byte image data for conversion
        ImageIcon icon; // new image icon to output the image in the screen
        int numTries = 0; // initialize number of tries to zero
        while ( true ) {
            // read image matrix
            capture.read(image);
            //convert matrix
            final MatOfByte bufferByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", image, bufferByte);
            imageData = bufferByte.toArray();
            // add to JLabel
            icon = new ImageIcon(imageData);
            cameraScreen.setIcon(icon);
            // pass the image capture from camera to be preprocessed using helper class
            imagePreProcessed = helper.imagePreProcessing(image);
            // pass the captured image to the model
            // to predict the output
            modelOutput = nnModel.testModelImage(imagePreProcessed);
            // get the index of the max value of the INDArray of the output model
            // convert the result to string
            String prediction = modelOutput.argMax().toString();
            // convert back to integer to do math operations
            Integer pred = Integer.parseInt(prediction);
            actualPredValue = modelOutput.getDouble(pred);
            System.out.println("Accuracy predictions values for model: " + modelOutput.toString());
            System.out.println("Predicted accuracy: " + actualPredValue);
            System.out.println("Class index of predicted accuracy: " + pred);
            if (minPredValue >= actualPredValue) {
                numTries++;
                System.out.println("Count: " + numTries);
                if (numTries >= maxTries) {
                    choice = JOptionPane.showConfirmDialog(this,
                            "NOT FOUND! TRY AGAIN?",
                            "CONFIRMATION", JOptionPane.YES_NO_OPTION);
                }
                if (choice == JOptionPane.YES_OPTION){
                    numTries = 0;
                    choice = 10;
                   // continue;
                } else if (choice == JOptionPane.NO_OPTION) {
                    this.dispose();
                    capture.release();
                    image.release();
                    break;
                }
            } else {
                // categorical map to match the prediction value of the model
                // to the categorical value of the training images
                categoricalMap = helper.getCategoricalIndexes();
                System.out.println(categoricalMap);

                if (categoricalMap.containsKey(pred) && modelOutput.getDouble(pred) >= minPredValue) {
                    JOptionPane.showMessageDialog(this,
                            "Welcome " + categoricalMap.get(pred).toString(),
                            "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                    capture.release();
                    image.release();
                    // setVisible(false);
                    this.dispose();
                    MainFrame mainFrame = new MainFrame(jobId);
                    InitialFrame initialFrame = new InitialFrame();
                    // initialFrame.setVisible(false);
                    initialFrame.dispose();
                    mainFrame.setVisible(true);
                    break;
                }
            }
        }
    }
}
