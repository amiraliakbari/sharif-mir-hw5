package edu.sharif.ce.mir.dal;

import edu.sharif.ce.mir.dal.data.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/18/12, 19:22)
 */
public interface DataStorage {

    /**
     * Establishes a connection to the data source
     */
    void connect();

    /**
     * Disconnects from the storage
     */
    void disconnect();

    /**
     * Creates a table (if not already present)
     * @param dataSource    the data source
     */
    void createTable(DataSource dataSource);

    /**
     * Deletes an entity
     * @param entity    the entity
     */
    void delete(Entity entity);

    /**
     * Inserts an entity into the storage
     * @param entity    the entity
     */
    void insert(Entity entity) throws SQLException;

    /**
     * Updates all the fields in a given entity
     * @param entity    the entity to be updated
     */
    void update(Entity entity) throws SQLException;

    /**
     * Retrieves a single entity from the storage based on the primary key
     * @param entity the entity
     * @return selected entity
     */
    Entity select(Entity entity) throws SQLException;

    /**
     * Same as . Converts the selected entity
     * to the specified type
     * @param entity target entity
     * @param type target type
     * @param <E> type parameter
     * @return converted entity
     */
    <E> E select(Entity entity, Class<E> type) throws SQLException;

    public ResultSet execute(String sql) throws SQLException;
}
