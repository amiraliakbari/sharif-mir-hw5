package edu.sharif.ce.mir.dal.sql;

import edu.sharif.ce.mir.dal.DataSource;
import edu.sharif.ce.mir.dal.data.Entity;

/**
 * This class should generate sql statements
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/18/12, 18:51)
 */
public interface SqlGenerator {

    String getSelectStatement(DataSource dataSource);

    String getDeleteStatement(DataSource dataSource);

    String getUpdateStatement(Entity entity);

    String getInsertStatement(Entity entity);

    String getCreateTableStatement(DataSource dataSource);

    String getAlterPrimaryKeyStatement(DataSource dataSource);
}
