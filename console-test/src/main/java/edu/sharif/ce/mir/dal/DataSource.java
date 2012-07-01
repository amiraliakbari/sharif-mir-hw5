package edu.sharif.ce.mir.dal;

import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/18/12, 18:28)
 */
public interface DataSource {


    /**
     * Should give a list of all columns
     * @return list
     */
    List<ColumnMetaData> getColumns();

    /**
     * The primary key to the table. Must be also present in the {@link #getColumns()}
     * @return the primary key
     */
    ColumnMetaData getPrimaryKey();

    /**
     * Name of the table
     * @return the table
     */
    String getTable();

    String createSql();


}
