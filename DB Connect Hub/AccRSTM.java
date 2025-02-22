/*
Name: Allen Su
Course: CNT 4714 Fall 2024
Assignment title: Project 3 – A Specialized Accountant Application
Date: October 20, 2024
Class: AccRSTM
*/

import java.sql.*;
import javax.swing.table.AbstractTableModel;

public class AccRSTM extends AbstractTableModel{
    Connection con;
    Statement stmt;
    ResultSet resultSet;
    ResultSetMetaData metaData;
    private int rowNo;


    public AccRSTM(Connection connection) throws SQLException {
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

    public void setQuery(String query) throws SQLException {
        resultSet = stmt.executeQuery(query);
        metaData = resultSet.getMetaData();
        resultSet.last();
        rowNo = resultSet.getRow();
        fireTableStructureChanged();
    }
    public void mySqlCmd(String query) throws SQLException {
        stmt.executeUpdate(query);
    }
}
