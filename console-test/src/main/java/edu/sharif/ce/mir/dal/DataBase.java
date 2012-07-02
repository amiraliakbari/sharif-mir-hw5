package edu.sharif.ce.mir.dal;

import java.sql.ResultSet;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 5/10/12
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataBase {


    public void connect();

    public void disconnect();

    public void executeQuery(String query);
    
    public ResultSet getData(String query);
}
