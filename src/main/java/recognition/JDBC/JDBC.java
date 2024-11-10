package recognition.JDBC;

import recognition.Helpers.DataModelLevel1;
import recognition.Helpers.DataModelLevel2;
import recognition.Helpers.DataModelLevel3;
import recognition.Helpers.User;

import java.sql.*;
import java.util.ArrayList;

public class JDBC {
    private Connection connection;

    // data structure ArrayList to store a list with
    // different DataModelLevel# from database
    ArrayList<DataModelLevel1> tableData1 = new ArrayList<>();
    DataModelLevel1 dataModelLevel1;
    ArrayList<DataModelLevel2> tableData2 = new ArrayList<>();
    DataModelLevel2 dataModelLevel2;
    ArrayList<DataModelLevel3> tableData3 = new ArrayList<>();
    DataModelLevel3 dataModelLevel3;
    // User class model
    User user = new User();

    // auxliar method to connect to database
    public void connect() {
        try {
             connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/java_recognition",
                    "reco",
                    "reco1234!"
            );
            System.out.println("DATABASE CONNECTION SUCCESSFULL!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DATABASE CONNECTION FAILED!");
        }
    }

    // method to add user to database
    public void addUser(User user) throws SQLException {
        // start a new connection
        connect();
        // query to insert new user to database
        String query = "INSERT INTO users(firstname, lastname, email, jobid, rolelevel) VALUES (?,?,?,?,?)";
        // prepare statement and add user meta data
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1, user.getUserFirstName());
        pstm.setString(2, user.getUserLastName());
        pstm.setString(3, user.getUserEmail());
        pstm.setString(4, user.getUserJobId());
        pstm.setInt(5, user.getRoleLevel());
        // return the result statement
        int result = pstm.executeUpdate();

        System.out.println(result + "Added to database");
        connection.close();
    }

    // method to return the active user in the system
    public User getActiveUser(String jobid) throws SQLException {
        // start a new connection
        connect();
        String userFirstName = "";
        String userLastName = "";
        Integer userAccessLevel = 0;
        String query = "SELECT * FROM users WHERE jobid = ?";
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1, jobid);
        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            userFirstName = rs.getString("firstname");
            userLastName = rs.getString("lastname");
            userAccessLevel = rs.getInt("rolelevel");
            user.setUserFirstName(userFirstName);
            user.setUserLastName(userLastName);
            user.setRoleLevel(userAccessLevel);
        }

        return user;
    }

    // method to store the table from database in a ArrayList of type DataModelLevel#
    public ArrayList<DataModelLevel1> retrieveDataLevel1() throws SQLException{
        // start a new connection
        connect();
        // query to get all rows from table
        String query = "SELECT * FROM fundo_nacional_mudanca_clima";
        // prepare statement to store retrieve data
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        // while loop to iterate over each row in the database
        while (rs.next()) {
            String process_num = rs.getString("process_num");
            String projeto = rs.getString("nome_projeto");
            String executora = rs.getString("instituicao_executora");
            String uf = rs.getString("UF");
            String valor = rs.getString("valor_fnmc");
            dataModelLevel1 = new DataModelLevel1(process_num, projeto, valor, executora, uf);
            tableData1.add(dataModelLevel1);
        }
        return tableData1; // return all recurrences of data
    }

    public ArrayList<DataModelLevel2> retrieveDataLevel2() throws SQLException{
        // start a new connection
        connect();
        // query to get all rows from table
        String query = "SELECT * FROM lista_especies_ameacas";
        // prepare statement to store retrieve data
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        // while loop to iterate over each row in the database
        while (rs.next()) {
            String fauna_flora = rs.getString("fauna_flora");
            String grupo = rs.getString("grupo");
            String familia = rs.getString("familia");
            String especie = rs.getString("especie");
            String nome_comum = rs.getString("nome_comum");
            String categoria_ameaca = rs.getString("categoria_ameaca");
            String bioma = rs.getString("bioma");
            dataModelLevel2 = new DataModelLevel2(fauna_flora, grupo, familia, especie, nome_comum, categoria_ameaca, bioma);
            tableData2.add(dataModelLevel2);
        }
        return tableData2;
    }

    public ArrayList<DataModelLevel3> retrieveDataLevel3() throws SQLException{
        // start a new connection
        connect();
        // retrieve all data rows in database where ano = 2024
        String query = "SELECT * FROM legislacao_ambiental_brasileira_2024 WHERE ano = 2024";
        // prepare statement to store retrieve data
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        // while loop to iterate over each row in the database
        while (rs.next()) {
            int ano = rs.getInt("ano");
            String documento = rs.getString("documento");
            int numero = rs.getInt("numero");
            String ementa = rs.getString("ementa");
            String status = rs.getString("status");
            dataModelLevel3 = new DataModelLevel3(ano, documento, numero, ementa, status);
            tableData3.add(dataModelLevel3);
        }
        return tableData3;
    }
}
