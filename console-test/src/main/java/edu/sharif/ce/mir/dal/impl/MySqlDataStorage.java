package edu.sharif.ce.mir.dal.impl;

import edu.sharif.ce.mir.dal.ColumnMetaData;
import edu.sharif.ce.mir.dal.DataSource;
import edu.sharif.ce.mir.dal.DataStorage;
import edu.sharif.ce.mir.dal.data.Entity;
import edu.sharif.ce.mir.dal.data.impl.DefaultDataInjector;
import edu.sharif.ce.mir.dal.sql.MySqlSpecificSqlGenerator;

import java.sql.*;
import java.util.Properties;
/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 6/28/12
 * Time: 7:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class MySqlDataStorage implements DataStorage {
    private String host;
    private String database;
    private String username;
    private String password;
    private Connection conn = null;
    private MySqlSpecificSqlGenerator generator;
    private DefaultDataInjector injector;


    public MySqlDataStorage(String host, String database, String username, String password) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        generator = new MySqlSpecificSqlGenerator();
        injector = new DefaultDataInjector();
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
    public void createTable(DataSource dataSource) {
        String sql = dataSource.createSql();
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void delete(Entity entity) {
        String sql = generator.getDeleteStatement(entity.getDataSource());
        sql = injector.inject(sql, entity.getData());
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void insert(Entity entity) throws SQLException {
        String sql = generator.getInsertStatement(entity);
        sql = injector.inject(sql, entity.getData());
//        System.out.println("insert:" + sql);
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    @Override
    public void update(Entity entity) throws SQLException{
        String sql = generator.getUpdateStatement(entity);
        sql = injector.inject(sql, entity.getData());
//            System.out.println("update:" + sql);
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    @Override
    public Entity select(Entity entity) throws SQLException{
        String sql = generator.getSelectStatement(entity.getDataSource());
        System.out.println(sql);
        sql = injector.inject(sql, entity.getData());
        System.out.println(sql);
        ResultSet rs = null;
        Statement stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);

        Entity newEntity = new Entity(entity.getDataSource());
        if (rs == null)
            return null;
        rs.next();
        for (ColumnMetaData column : entity.getDataSource().getColumns()) {
            if (column.getType().equals(String.class)) {
                newEntity.set(column.getName(), rs.getString(column.getName()));
            } else if (column.getType().equals(Integer.class)) {
                newEntity.set(column.getName(), rs.getInt(column.getName()));
            } else if (column.getType().equals(Short.class)) {
                newEntity.set(column.getName(), rs.getShort(column.getName()));
            } else if (column.getType().equals(Long.class)) {
                newEntity.set(column.getName(), rs.getLong(column.getName()));
            } else if (column.getType().equals(java.util.Date.class)) {
                Long num=rs.getLong(column.getName());
                java.util.Date date=new java.util.Date(num);
//                    System.out.println(date);
                newEntity.set(column.getName(), date);
            } else if (column.getType().equals(Boolean.class)) {
                newEntity.set(column.getName(), rs.getBoolean(column.getName()));
            } else if (column.getType().equals(Character.class)) {
                newEntity.set(column.getName(), rs.getCharacterStream(column.getName()));
            }

        }
        return newEntity;
    }

    @Override
    public <E> E select(Entity entity, Class<E> type) throws SQLException {
        final Entity selected = select(entity);
        return selected.toObject(type);
    }

    public ResultSet execute(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }
    public boolean execute2(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.execute(sql);
    }
}
