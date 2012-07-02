package edu.sharif.ce.mir.dal;

import java.sql.*;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 5/10/12
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mysql implements DataBase {
    private String host;
    private String database;
    private String username;
    private String password;
    private Connection conn = null;

    public Mysql(String host, String database, String username, String password) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @Override
    public void connect() {
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.username);
        connectionProps.put("password", this.password);
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database + "?useUnicode=true&characterEncoding=utf8&autoReconnect=true", connectionProps);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void disconnect() {
        if(conn!=null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
    }

    @Override
    public void executeQuery(String query){
        try {
            Statement stmt = conn.createStatement();
            System.out.println("insert:" + query);
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Override
    public ResultSet getData(String query) {
        ResultSet rs = null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return rs;
    }
}
