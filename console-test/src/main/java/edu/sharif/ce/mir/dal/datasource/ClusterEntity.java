package edu.sharif.ce.mir.dal.datasource;

import edu.sharif.ce.mir.dal.Collections;
import edu.sharif.ce.mir.dal.ColumnMetaData;
import edu.sharif.ce.mir.dal.DataSource;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/4/12
 * Time: 9:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterEntity implements DataSource {
    public static final String TABLE_NAME = "cluster";
    private final ColumnMetaData id = new ColumnMetaData("id", Long.class, false);
    private final ColumnMetaData group = new ColumnMetaData("group", Integer.class, false);

    @Override
    public List<ColumnMetaData> getColumns() {
        return Collections.list( id,  group);
    }

    @Override
    public ColumnMetaData getPrimaryKey() {
        return id;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getTable() {
        return TABLE_NAME;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String createSql() {
        return "CREATE TABLE `cluster` (\n" +
                "\t`id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "\t`group` INT NOT NULL,\n" +
                "\tPRIMARY KEY (`id`)\n" +
                ")\n" +
                "COLLATE='utf8_general_ci'\n" +
                "ENGINE=InnoDB\n" +
                "ROW_FORMAT=DEFAULT";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
