package recognition;

import recognition.Helpers.User;
import recognition.JDBC.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class UserForm extends JFrame {
    private JPanel userFormPanel;
    private JTextField tf_firstName;
    private JTextField tf_lastName;
    private JTextField tf_email;
    private JButton btnSave;
    private JButton btnCancel;
    private JTextField tf_jobId;
    private JRadioButton rb_level1;
    private JRadioButton rb_level2;
    private JRadioButton rb_level3;

    // database class
    private JDBC db = new JDBC();
    // User class
    private User user = new User();

    // class temporary variables
    private int roleLevel;
    boolean checkUserAdded;

    public UserForm() {
        setLayout(null);
        setContentPane(userFormPanel);
        setTitle("User Form");
        setSize(640, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check radioButtons selected and access value attributes
                if (rb_level1.isSelected())
                    roleLevel = 1; // level 1 access selected
                else if (rb_level2.isSelected())
                    roleLevel = 2; // level 2 access selected
                else if (rb_level3.isSelected())
                    roleLevel = 3; // level 3 access selected
                else
                    roleLevel = 0; // Nothing is selected
                // get the user input
                String userFirstName = tf_firstName.getText();
                String userLastName = tf_lastName.getText();
                String jobId = tf_jobId.getText();
                String userEmail = tf_email.getText();
                // Add user to Database
                try {
                    // Set the new user to the User class model
                    user = new User(userFirstName, userLastName, jobId, userEmail, roleLevel);
                    // check user valid input
                    checkUserAdded = user.checkEmptyFields();
                    // add user to database
                    if (checkUserAdded) {
                        db.addUser(user);
                    }

                } catch (SQLException sqlException){
                    sqlException.printStackTrace();
                }

                if (checkUserAdded) {
                    JOptionPane.showMessageDialog(null,
                            "USER ADDED SUCCESSFULL!",
                            "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "MISSING INPUT VALUES!",
                            "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                }

                if (checkUserAdded) {
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            CameraForTraining cam = new CameraForTraining();
                            cam.transferUserDataFromFormToCamera(user);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        cam.startCamera();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    });
                }
            }
        });
    }
    public void clearFields() {
        tf_firstName.setText("");
        tf_lastName.setText("");
        tf_email.setText("");
        tf_jobId.setText("");
    }
}
