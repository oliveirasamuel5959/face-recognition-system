package recognition;

import org.opencv.core.Core;
import recognition.JDBC.JDBC;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialFrame extends JFrame {
    private JPanel mainPanel;
    private JButton btnLogin;
    private JButton btnAddUser;

    private static JDBC db = new JDBC();

    public InitialFrame() {
        setContentPane(mainPanel);
        setTitle("Form");
        setSize(720, 640);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        btnAddUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserForm userForm = new UserForm();
                userForm.setVisible(true);
                // setVisible(false);
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CameraRecognition cameraRecognition = new CameraRecognition();
                cameraRecognition.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        InitialFrame initialFrame = new InitialFrame();
        initialFrame.setVisible(true);
        db.connect();
    }
}
