package recognition;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import recognition.Helpers.Helper;
import recognition.Helpers.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class CameraForTraining extends JFrame {

    // swing components
    private JLabel cameraScreen;
    private JLabel imagesCounts;
    private JLabel lbCounts;
    private JButton btnCapture;
    private JPanel cameraPane;

    // camera variables
    private VideoCapture capture;
    private Mat image;
    private Mat imageResized;
    private Mat imageGrayScale;

    String firstName = "";
    String lastName = "";

    private boolean clicked = false;

    final private ArrayList<Mat> imagesList = new ArrayList<Mat>();
    Helper helper = new Helper();

    public CameraForTraining() {
        setContentPane(cameraPane);
        setTitle("Camera For Training");
        setLayout(null);

        cameraScreen = new JLabel();
        imagesCounts = new JLabel("Counts: ");
        lbCounts = new JLabel("");

        imagesCounts.setBounds(10, 480, 50,50);
        add(imagesCounts);

        lbCounts.setText(" ");
        lbCounts.setBounds(50, 480, 50,50);
        add(lbCounts);

        cameraScreen.setBounds(0,0,640,470);
        add(cameraScreen);

        btnCapture = new JButton("capture");
        btnCapture.setBounds(250,480,80,40);
        add(btnCapture);

        btnCapture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked = true;
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                capture.release();
                image.release();
                System.exit(0);
            }
        });

        setSize(new Dimension(640,560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // create camera
    public void startCamera() throws Exception {
        capture = new VideoCapture(0);
        image = new Mat();
        byte[] imageData;
        ArrayList<Mat> listOfImages = new ArrayList<Mat>();
        ImageIcon icon;
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
            // capture and save to file
            if ( clicked ) {
                // String name = JOptionPane.showInputDialog(this, "Enter image name");
                if ( firstName == null ) {
                    firstName = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
                }
                listOfImages = getMultipleImages(170);
                int i = 0;
                // Create a new directory
                String pathToImages = "C:\\Users\\Samuel Oliveira\\dl4j-examples\\src\\main\\resources\\faces_jpg\\training\\"
                        + firstName.toLowerCase() + "_" + lastName.toLowerCase();
                Files.createDirectories(Paths.get(pathToImages));

                // save the images to the recent created training directory
                for (Mat imgs: listOfImages) {
                    Imgcodecs.imwrite(pathToImages + "\\" + firstName+lastName + "_" + i + ".jpg", imgs);
                    i++;
                }

                if (helper.matListIsNotEmpty(listOfImages)) {
                    JOptionPane.showMessageDialog(this, "Images Loaded!",
                            "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                    // setVisible(false);
                    this.dispose();
                    capture.release();
                    image.release();
                } else {
                    JOptionPane.showMessageDialog(this, "Fail to load images!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                clicked = false;
            }
        }
    }

    public ArrayList<Mat> getMultipleImages(int numImages) throws Exception {
        // Initialize a new image matrix
        image = new Mat();
        // imageData as a list af byte
        byte[] imageData;
        // New image icon
        ImageIcon icon;
        // loop through the max values of images
        // to be taken and
        for (int i = 0; i < numImages; i++) {
            capture.read(image);
            //convert matrix
            final MatOfByte bufferByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", image, bufferByte);
            imageData = bufferByte.toArray();
            // add to JLabel
            icon = new ImageIcon(imageData);
            cameraScreen.setIcon(icon);
            // Resize the image to 64x64
            imageResized = new Mat();
            Size size = new Size(64,64);
            Imgproc.resize(image, imageResized, size, 0, 0, Imgproc.INTER_AREA);
            // apply gaussian blur filter to resized image
            Mat imageBlurFilter = new Mat();
            Imgproc.GaussianBlur(imageResized, imageBlurFilter, new Size(3 ,3), 0);
            // Convert image to gray scale
            imageGrayScale = new Mat();
            Imgproc.cvtColor(imageBlurFilter, imageGrayScale, Imgproc.COLOR_RGB2GRAY);
            // Add images to the imagesList
            imagesList.add(imageGrayScale);
            // update the value of snapshots counts
            lbCounts.setText(" " + String.valueOf(i + 1));
            // time interval to the next image
            Thread.sleep(40);
        }
        // return list of Mat images
        return imagesList;
    }

    public void transferUserDataFromFormToCamera(User user) {
        firstName = user.getUserFirstName();
        lastName = user.getUserLastName();
    }

}
