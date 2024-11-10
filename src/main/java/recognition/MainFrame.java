package recognition;

import recognition.Helpers.DataModelLevel1;
import recognition.Helpers.DataModelLevel2;
import recognition.Helpers.DataModelLevel3;
import recognition.Helpers.User;
import recognition.JDBC.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    // MainFrame GUI widgets and componentes
    private JPanel rootPane;
    private JPanel mainPane;
    private JTable tableData;
    private JPanel titlePane;
    private JPanel tablePane;
    private JComboBox cbLevels;
    private JButton btnData;
    private JLabel jl_name;
    private JLabel jl_levelAccess;
    // ArrayList variable to store tha tables model from database
    ArrayList<DataModelLevel1> tableList1;
    ArrayList<DataModelLevel2> tableList2;
    ArrayList<DataModelLevel3> tableList3;
    // variables names to database data
    String level1 = "Lista de Especies Ameaçadas";
    String level2 = "legislacao_ambiental_brasileira_2024";
    String level3 = "Fundo Nacional de Mudança de Clima";
    // User model class to handle user information
    User user = new User();
    // database model class to handle database queries
    JDBC db = new JDBC();
    // Constructor of the MainFrame window
    public MainFrame(String jobId) {
        // create combobox options to be accessed
        cbLevels.setModel(new DefaultComboBoxModel(new String[]{
                level1,
                level2,
                level3
            }
        ));
        // main GUI root pane
        setContentPane(rootPane);
        setTitle("Main"); // title main window
        setSize(720, 640); // screen absolute size
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        try {
            user = db.getActiveUser(jobId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Store the user role level and user names in variable
        // to show the corresponding tables the active user has access to
        Integer userRoleAccess = user.getRoleLevel();
        String fname = user.getUserFirstName();
        String lname = user.getUserLastName();
        // change label with user name and access level data
        // from database
        jl_name.setText(fname + " " + lname);
        jl_levelAccess.setText("Nível " + userRoleAccess.toString() + " de" + " Acesso");

        // Data button to retrieve data from the database
        // based on user levels
        btnData.addActionListener(new ActionListener() {
            // Conditions to get the correct data from database
            // giver the selection on combobox
            @Override
            public void actionPerformed(ActionEvent e) {
                if (String.valueOf( cbLevels.getSelectedItem()) == level1 && (userRoleAccess == 3 || userRoleAccess == 1) )  {
                    tableData.setModel(new DefaultTableModel(
                            null,
                            new String[]{"Fauna/Flora", "Grupo", "Familia", "Especia", "Nome Comum", "Categoria de Ameaca", "Bioma"}
                    ));
                    createTable2();
                }
                else if (String.valueOf( cbLevels.getSelectedItem()) == level2 && (userRoleAccess == 3 || userRoleAccess == 2) ) {
                    tableData.setModel(new DefaultTableModel(
                            null,
                            new String[]{"ano", "documento", "numero", "ementa", "status"}
                    ));
                    createTable3();
                } else if(String.valueOf( cbLevels.getSelectedItem()) == level3 && userRoleAccess == 3 ) {
                    tableData.setModel(new DefaultTableModel(
                            null,
                            new String[]{"N° Processo", "Projeto", "Executora", "UF", "Valor FNMC"}
                    ));
                    createTable1();
                } else {
                    JOptionPane.showMessageDialog(null, "ACCESS LEVEL DENIED!",
                            "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    // method to create table model based on
    // database table data
    public void createTable1() {
        DefaultTableModel model = (DefaultTableModel) tableData.getModel();
        try {
            tableList1 = db.retrieveDataLevel1();
            Object rowData[] = new Object[5];
            for(int i = 0;  i < tableList1.size(); i++){
                rowData[0] = tableList1.get(i).getProcess_num();
                rowData[1] = tableList1.get(i).getNome_projeto();
                rowData[2] = tableList1.get(i).getInst_executor();
                rowData[3] = tableList1.get(i).getUf();
                rowData[4] = tableList1.get(i).getValor();
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // method to create table model based on
    // database table data
    public void createTable2() {
        DefaultTableModel model = (DefaultTableModel) tableData.getModel();
        try {
            tableList2 = db.retrieveDataLevel2();
            Object rowData[] = new Object[7];
            for(int i = 0;  i < tableList2.size(); i++){
                rowData[0] = tableList2.get(i).getFauna_flora();
                rowData[1] = tableList2.get(i).getGrupo();
                rowData[2] = tableList2.get(i).getFamilia();
                rowData[3] = tableList2.get(i).getEspecie();
                rowData[4] = tableList2.get(i).getNome_comum();
                rowData[5] = tableList2.get(i).getCategoria_ameaca();
                rowData[6] = tableList2.get(i).getBioma();
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // method to create table model based on
    // database table data
    public void createTable3() {
        DefaultTableModel model = (DefaultTableModel) tableData.getModel();
        try {
            tableList3 = db.retrieveDataLevel3();
            Object rowData[] = new Object[5];
            for(int i = 0;  i < tableList3.size(); i++){
                rowData[0] = tableList3.get(i).getAno();
                rowData[1] = tableList3.get(i).getDocumento();
                rowData[2] = tableList3.get(i).getNumero();
                rowData[3] = tableList3.get(i).getEmenta();
                rowData[4] = tableList3.get(i).getStatus();
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
