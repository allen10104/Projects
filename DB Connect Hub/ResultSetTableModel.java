/*
Name: Allen Su
Course: CNT 4714 Fall 2024
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: October 20, 2024
Class: ResultTableModel
*/

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import javax.swing.table.AbstractTableModel;

public class ResultSetTableModel extends AbstractTableModel{
    Connection con;
    Statement stmt;
    ResultSet resultSet;
    ResultSetMetaData metaData;
    private int rowNo;


    public ResultSetTableModel(Connection connection) throws SQLException {
        this.con = connection;
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

    public Class getColumnClass(int column) throws IllegalStateException{
        try{
            String className = metaData.getColumnClassName(column + 1);
            return Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Object.class;
    }

    @Override
    public int getColumnCount() {
        try{
            return metaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getColumnName(int column) throws IllegalStateException{
        try{
            return metaData.getColumnName(column + 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int getRowCount() {
        return rowNo;
    }

    @Override
    public Object getValueAt(int row, int column) throws IllegalStateException{
        try{
            resultSet.next();
            resultSet.absolute(row + 1);
            return resultSet.getObject(column + 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setQuery(String query) throws SQLException, ClassNotFoundException, IOException {
        resultSet = stmt.executeQuery(query);
        metaData = resultSet.getMetaData();
        resultSet.last();
        rowNo = resultSet.getRow();
        fireTableStructureChanged();

        Properties projectDbProp = new Properties();
        Properties projectUserProp = new Properties();

        projectDbProp.load(new FileInputStream("operationslog.properties"));
        projectUserProp.load(new FileInputStream("project3app.properties"));

        String dbDriverClass = projectDbProp.getProperty("MYSQL_DB_DRIVER_CLASS");
        String dbUrl = projectDbProp.getProperty("MYSQL_DB_URL");

        String user = projectUserProp.getProperty("MYSQL_DB_USERNAME");
        String pass = projectUserProp.getProperty("MYSQL_DB_PASSWORD");

        Class.forName(dbDriverClass);
        Connection operationsCon = DriverManager.getConnection(dbUrl, user, pass);

        String getUserQuery = "SELECT CURRENT_USER();";
        PreparedStatement proj3stmt = con.prepareStatement(getUserQuery);
        ResultSet proj3rs = proj3stmt.executeQuery();

        if (proj3rs.next()){
            String currUser = proj3rs.getString(1);

            String userExistsQuery = "SELECT EXISTS (SELECT 1 FROM operationscount where login_username = ?) AS username_exists";
            proj3stmt = operationsCon.prepareStatement(userExistsQuery);
            proj3stmt.setString(1, currUser);
            proj3rs = proj3stmt.executeQuery();

            if(proj3rs.next()){
                boolean userExists = proj3rs.getBoolean(1);
                if(userExists){
                    String updateNumQ = "UPDATE operationscount SET num_queries = num_queries + 1 WHERE login_username = ?";
                    proj3stmt = operationsCon.prepareStatement(updateNumQ);
                    proj3stmt.setString(1, currUser);
                    proj3stmt.executeUpdate();
                }
                else{
                    String insertQuery = "INSERT INTO operationscount VALUES (?, 1, 0)";
                    proj3stmt = operationsCon.prepareStatement(insertQuery);
                    proj3stmt.setString(1, currUser);
                    proj3stmt.executeUpdate();
                }
            }
        }
        operationsCon.close();
    }
    public int setUpdate(String query) throws SQLException, IllegalStateException, IOException, ClassNotFoundException {
        int res;
        res = stmt.executeUpdate(query);

        Properties projectDbProp = new Properties();
        Properties projectUserProp = new Properties();

        projectDbProp.load(new FileInputStream("operationslog.properties"));
        projectUserProp.load(new FileInputStream("project3app.properties"));

        String dbDriverClass = projectDbProp.getProperty("MYSQL_DB_DRIVER_CLASS");
        String dbUrl = projectDbProp.getProperty("MYSQL_DB_URL");

        String user = projectUserProp.getProperty("MYSQL_DB_USERNAME");
        String pass = projectUserProp.getProperty("MYSQL_DB_PASSWORD");

        Class.forName(dbDriverClass);
        Connection operationsCon = DriverManager.getConnection(dbUrl, user, pass);

        String getUserQuery = "SELECT CURRENT_USER();";
        PreparedStatement proj3stmt = con.prepareStatement(getUserQuery);
        ResultSet proj3rs = proj3stmt.executeQuery();

        if (proj3rs.next()){
            String currUser = proj3rs.getString(1);

            String userExistsQuery = "SELECT EXISTS (SELECT 1 FROM operationscount where login_username = ?) AS username_exists";
            proj3stmt = operationsCon.prepareStatement(userExistsQuery);
            proj3stmt.setString(1, currUser);
            proj3rs = proj3stmt.executeQuery();

            if(proj3rs.next()){
                boolean userExists = proj3rs.getBoolean(1);
                if(userExists){
                    String updateNumQ = "UPDATE operationscount SET num_updates = num_updates + 1 WHERE login_username = ?";
                    proj3stmt = operationsCon.prepareStatement(updateNumQ);
                    proj3stmt.setString(1, currUser);
                    proj3stmt.executeUpdate();
                }
                else{
                    String insertQuery = "INSERT INTO operationscount VALUES (?, 0, 1)";
                    proj3stmt = operationsCon.prepareStatement(insertQuery);
                    proj3stmt.setString(1, currUser);
                    proj3stmt.executeUpdate();
                }
            }
        }
        fireTableStructureChanged();
        return res;
    }

    public void mySqlCmd(String query) throws SQLException {
        stmt.executeQuery(query);
    }
}
