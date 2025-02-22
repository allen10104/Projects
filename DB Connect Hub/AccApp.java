/*
Name: Allen Su
Course: CNT 4714 Fall 2024
Assignment title: Project 3 – A Specialized Accountant Application
Date: October 20, 2024
Class: AccApp
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public class AccApp extends JFrame implements ActionListener {
    //Connection
    Connection con;
    //Panels
    JPanel urlPropertiesPanel;
    JPanel userPropertiesPanel;
    JPanel usernamePanel;
    JPanel passwordPanel;


    //Labels and Text-panes
    JLabel connectionDetailsLabel;
    JLabel enterCommandLabel;
    JLabel executionWindowLabel;
    JLabel urlPropertiesLabel;

    JLabel userPropertiesLabel;

    JLabel passwordLabel;

    JLabel usernameLabel;
    JTextPane connectionStatus;

    //ScrollPanes
    JScrollPane enterCommandScroll;
    JScrollPane executionWindowScroll;


    //Text and password fields
    JTextField urlPropertiesText;
    JTextField userPropertiesText;
    JTextField usernameText;
    JPasswordField passwordText;

    JTextArea enterCommandArea;

    JTextArea executionWindowArea;

    //Tables
    JTable resultTable;

    //Buttons
    JButton connectButton;
    JButton disconnectButton;
    JButton clearCommandButton;
    JButton executeCommandButton;
    JButton clearResultButton;
    JButton closeAppButton;

    //Current database URL
    String databaseURL;

    //ResultSetTableModel
    AccRSTM tb;
    public AccApp() throws FileNotFoundException {
        setTitle("SPECIALIZED ACCOUNTANT APPLICATION - (MJL - CNT4714 - FALL 2024 - PROJECT 3)");
        setSize(1300, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);


        //PANELS
        urlPropertiesPanel = new JPanel();
        urlPropertiesPanel.setBackground(new Color(170, 170, 170));
        urlPropertiesPanel.setBounds(10, 60, 190, 40);
        urlPropertiesPanel.setLayout(null);

        userPropertiesPanel = new JPanel();
        userPropertiesPanel.setBackground(new Color(170, 170, 170));
        userPropertiesPanel.setBounds(10, 120, 190, 40);
        userPropertiesPanel.setLayout(null);

        usernamePanel = new JPanel();
        usernamePanel.setBackground(new Color(170, 170, 170));
        usernamePanel.setBounds(10, 180, 190, 40);
        usernamePanel.setLayout(null);

        passwordPanel = new JPanel();
        passwordPanel.setBackground(new Color(170, 170, 170));
        passwordPanel.setBounds(10, 240, 190, 40);
        passwordPanel.setLayout(null);

        //LABELS AND TEXT-PANES
        connectionDetailsLabel = new JLabel("Connection Details");
        connectionDetailsLabel.setFont(new Font("Calibri", Font.BOLD, 25));
        connectionDetailsLabel.setForeground(new Color(3, 15, 252));
        connectionDetailsLabel.setBounds(10,10, 300, 50);


        urlPropertiesLabel = new JLabel("DB URL Properties");
        urlPropertiesLabel.setFont(new Font("Calibri", Font.PLAIN, 23));
        urlPropertiesLabel.setBounds(10, 0, 300, 50);
        urlPropertiesLabel.setForeground(new Color(18, 18, 18));


        userPropertiesLabel = new JLabel("User Properties");
        userPropertiesLabel.setFont(new Font("Calibri", Font.PLAIN, 23));
        userPropertiesLabel.setBounds(10, 0, 300, 50);
        userPropertiesLabel.setForeground(new Color(18, 18, 18));


        usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Calibri", Font.PLAIN, 23));
        usernameLabel.setBounds(10, 0, 300, 50);
        usernameLabel.setForeground(new Color(18, 18, 18));

        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Calibri", Font.PLAIN, 23));
        passwordLabel.setBounds(10, 0, 300, 50);
        passwordLabel.setForeground(new Color(18, 18, 18));

        enterCommandLabel = new JLabel("Enter A SQL Command");
        enterCommandLabel.setFont(new Font("Calibri", Font.BOLD, 25));
        enterCommandLabel.setForeground(new Color(3, 15, 252));
        enterCommandLabel.setBounds(600,10, 300, 50);

        connectionStatus = new JTextPane();
        connectionStatus.setBounds(25, 370, 1195, 40);
        connectionStatus.setText("NO CONNECTION ESTABLISHED");
        connectionStatus.setBackground(new Color(0, 0, 0));
        connectionStatus.setFont(new Font("Calibri", Font.PLAIN, 20));
        connectionStatus.setForeground(new Color(232, 5, 5));
        connectionStatus.setEditable(false);

        executionWindowLabel = new JLabel("SQL Execution Result Window");
        executionWindowLabel.setFont(new Font("Calibri", Font.BOLD, 25));
        executionWindowLabel.setForeground(new Color(3, 15, 252));
        executionWindowLabel.setBounds(25,430, 350, 50);


        urlPropertiesText = new JTextField();
        urlPropertiesText.setBounds(205, 62, 350, 36);
        urlPropertiesText.setFont(new Font("Calibri", Font.PLAIN, 20));
        urlPropertiesText.setText("operationslog.properties");
        urlPropertiesText.setEditable(false);

        userPropertiesText = new JTextField();
        userPropertiesText.setBounds(205, 122, 350, 36);
        userPropertiesText.setFont(new Font("Calibri", Font.PLAIN, 20));
        userPropertiesText.setText("theaccountant.properties");
        userPropertiesText.setEditable(false);

        //Scroll Bars
        enterCommandScroll = new JScrollPane();
        enterCommandScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        enterCommandScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        executionWindowScroll = new JScrollPane();
        executionWindowScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        executionWindowScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        //Text and Password Fields
        usernameText = new JTextField();
        usernameText.setBounds(205, 182, 350, 36);
        usernameText.setFont(new Font("Calibri", Font.PLAIN, 20));

        passwordText = new JPasswordField();
        passwordText.setBounds(205, 242, 350, 36);
        passwordText.setFont(new Font("Calibri", Font.PLAIN, 20));


        enterCommandArea = new JTextArea();
        enterCommandArea.setBounds(600, 50, 650, 210);
        enterCommandArea.setFont(new Font("Calibri", Font.PLAIN, 20));
        enterCommandScroll.setBounds(600, 50, 650, 210);
        enterCommandScroll.getViewport().add(enterCommandArea);

        executionWindowArea = new JTextArea();
        executionWindowArea.setBounds(25,480, 1195, 400);
        executionWindowArea.setFont(new Font("Calibri", Font.BOLD, 18));
        executionWindowArea.setEditable(false);
        executionWindowScroll.setBounds(25,480, 1195, 400);
        executionWindowScroll.getViewport().add(executionWindowArea);

        //Buttons
        connectButton = new JButton("Connect to Database");
        connectButton.setBounds(25, 300, 220, 40);
        connectButton.setBackground(new Color(3, 15, 252));
        connectButton.setFont(new Font("Calibri", Font.PLAIN, 20));
        connectButton.setForeground(new Color(255, 255, 255));
        connectButton.addActionListener(this);
        connectButton.setFocusPainted(false);

        disconnectButton = new JButton("Disconnect from Database");
        disconnectButton.setBounds(265, 300, 260, 40);
        disconnectButton.setBackground(new Color(232, 5, 5));
        disconnectButton.setFont(new Font("Calibri", Font.BOLD, 20));
        disconnectButton.setForeground(new Color(0, 0, 0));
        disconnectButton.addActionListener(this);
        disconnectButton.setFocusPainted(false);
        disconnectButton.setEnabled(false);

        clearCommandButton = new JButton("Clear SQL Command");
        clearCommandButton.setBounds(630, 280, 260, 40);
        clearCommandButton.setBackground(new Color(255, 238, 0));
        clearCommandButton.setFont(new Font("Calibri", Font.BOLD, 20));
        clearCommandButton.setForeground(new Color(0, 0, 0));
        clearCommandButton.addActionListener(this);
        clearCommandButton.setFocusPainted(false);

        executeCommandButton = new JButton("Execute SQL Command");
        executeCommandButton.setBounds(960, 280, 260, 40);
        executeCommandButton.setBackground(new Color(0, 255, 0));
        executeCommandButton.setFont(new Font("Calibri", Font.BOLD, 20));
        executeCommandButton.setForeground(new Color(0, 0, 0));
        executeCommandButton.addActionListener(this);
        executeCommandButton.setFocusPainted(false);
        executeCommandButton.setEnabled(false);

        clearResultButton = new JButton("Clear Result Window");
        clearResultButton.setBounds(30, 890, 220, 40);
        clearResultButton.setBackground(new Color(255, 238, 0));
        clearResultButton.setFont(new Font("Calibri", Font.BOLD, 20));
        clearResultButton.setForeground(new Color(0, 0, 0));
        clearResultButton.addActionListener(this);
        clearResultButton.setFocusPainted(false);

        closeAppButton = new JButton("Close Application");
        closeAppButton.setBounds(985, 890, 220, 40);
        closeAppButton.setBackground(new Color(232, 5, 5));
        closeAppButton.setFont(new Font("Calibri", Font.BOLD, 20));
        closeAppButton.setForeground(new Color(0, 0, 0));
        closeAppButton.addActionListener(this);
        closeAppButton.setFocusPainted(false);

        //Add to Frame
        urlPropertiesPanel.add(urlPropertiesLabel);
        userPropertiesPanel.add(userPropertiesLabel);
        usernamePanel.add(usernameLabel);
        passwordPanel.add(passwordLabel);

        add(urlPropertiesPanel);
        add(userPropertiesPanel);
        add(usernamePanel);
        add(passwordPanel);

        add(usernameText);
        add(passwordText);
        add(connectionStatus);
        add(enterCommandScroll);
        add(executionWindowScroll);

        add(urlPropertiesText);
        add(userPropertiesText);
        add(connectionDetailsLabel);
        add(enterCommandLabel);
        add(executionWindowLabel);


        add(connectButton);
        add(disconnectButton);
        add(clearCommandButton);
        add(executeCommandButton);
        add(clearResultButton);
        add(closeAppButton);

        setVisible(true);
        setResizable(false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clearCommandButton){
            enterCommandArea.setText("");
        }

        if (e.getSource() == clearResultButton){
            executionWindowArea.setText("");
            if (executionWindowScroll.getViewport().getView() instanceof JTable)
                executionWindowScroll.getViewport().remove(resultTable);

            executionWindowScroll.getViewport().add(executionWindowArea);
            executionWindowScroll.revalidate();
            executionWindowScroll.repaint();
        }

        if (e.getSource() == connectButton){
            String username = usernameText.getText();
            String userPropFileName = userPropertiesText.getText();
            String dbPropFileName = urlPropertiesText.getText();
            char [] getPassword = passwordText.getPassword();
            String password = new String(getPassword);

            try {
                boolean connected = createConnection(username, password, userPropFileName, dbPropFileName);
                if(connected){
                    connectionStatus.setText("CONNECTED TO: " + databaseURL);
                    connectionStatus.setForeground(new Color(255, 238, 0));
                    connectButton.setEnabled(false);
                    urlPropertiesText.setEnabled(false);
                    userPropertiesText.setEnabled(false);
                    usernameText.setEditable(false);
                    passwordText.setEditable(false);
                    executeCommandButton.setEnabled(true);
                    disconnectButton.setEnabled(true);

                }
                else{
                    connectionStatus.setText("NOT CONNECTED - User Credentials Do Not Match Properties File!");
                    connectionStatus.setForeground(new Color(232, 5, 5));
                }
            } catch (IOException fnf){
                System.out.println("FILE NOT FOUND");
            } catch (ClassNotFoundException cnf) {
                System.out.println("Class not found");
                throw new RuntimeException(cnf);
            } catch(SQLException sql){
                System.out.println("Invalid username/pass");
            }

        }

        if (e.getSource() == disconnectButton){
            if (con != null){
                try {
                    con.close();
                    connectButton.setEnabled(true);
                    urlPropertiesText.setEnabled(true);
                    userPropertiesText.setEnabled(true);
                    usernameText.setEditable(true);
                    passwordText.setEditable(true);
                    disconnectButton.setEnabled(false);
                    executeCommandButton.setEnabled(false);
                    connectionStatus.setText("NO CONNECTION ESTABLISHED");
                    connectionStatus.setForeground(new Color(232, 5, 5));
                    usernameText.setText("");
                    passwordText.setText("");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        if(e.getSource() == executeCommandButton){
            tb = null;
            String sql = enterCommandArea.getText();
            try {
                tb = new AccRSTM(con);
                if (sql.toLowerCase().contains("select")){
                    tb.setQuery(sql);
                    resultTable = new JTable(tb);
                    resultTable.setGridColor(Color.BLACK);
                    if (executionWindowScroll.getViewport().getView() != null){
                        executionWindowScroll.getViewport().removeAll();
                    }
                    executionWindowScroll.getViewport().add(resultTable);
                    executionWindowScroll.revalidate();
                    executionWindowScroll.repaint();
                }
                else{
                    System.out.println("HIIIIII");
                    try {
                        tb.mySqlCmd(sql);
                    } catch (SQLException exc) {
                        JOptionPane.showMessageDialog(null, exc.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(exc);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(e.getSource() == closeAppButton){
            try {
                if (con != null)
                    con.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.exit(0);
        }
    }

    boolean createConnection(String username, String password, String userPropFileName, String dbPropFileName) throws ClassNotFoundException, SQLException, IOException {
        Properties userProp = new Properties();
        Properties dbProp = new Properties();
        userProp.load(new FileInputStream(userPropFileName));
        if(!Objects.equals(username, userProp.getProperty("MYSQL_DB_USERNAME")) || !Objects.equals(password, userProp.getProperty("MYSQL_DB_PASSWORD")))
            return false;


        dbProp.load(new FileInputStream(dbPropFileName));
        String dbDriverClass = dbProp.getProperty("MYSQL_DB_DRIVER_CLASS");
        String dbUrl = dbProp.getProperty("MYSQL_DB_URL");

        System.out.println(dbDriverClass);
        System.out.println(dbUrl);

        Class.forName(dbDriverClass);
        con = DriverManager.getConnection(dbUrl, username, password);
        databaseURL = dbUrl;
        return true;
    }
}
