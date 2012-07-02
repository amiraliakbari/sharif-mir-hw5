package edu.sharif.ce.mir.dal.sql;

import edu.sharif.ce.mir.dal.ColumnMetaData;
import edu.sharif.ce.mir.dal.DataSource;
import edu.sharif.ce.mir.dal.data.Entity;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 6/28/12
 * Time: 7:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class MySqlSpecificSqlGenerator implements SqlGenerator {
    @Override
    public String getSelectStatement(DataSource dataSource) {
        final String name = dataSource.getPrimaryKey().getName();
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ").append("`").append(dataSource.getTable()).append("`");
        builder.append(" WHERE `").append(name).append("` = #").append(name).append(";");
        return builder.toString();
    }

    public String getSelectAllStatement(DataSource dataSource) {
        final String tableName = dataSource.getTable();
        final StringBuilder sb = new StringBuilder();
        sb.append("select * from `");
        sb.append(tableName);
        sb.append("`");
        return sb.toString();
    }

    @Override
    public String getDeleteStatement(DataSource dataSource) {
        final String name = dataSource.getPrimaryKey().getName();
        final StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM ").append("`").append(dataSource.getTable()).append("`");
        builder.append(" WHERE `").append(name).append("` = #").append(name).append(";");
        return builder.toString();
    }

    @Override
    public String getUpdateStatement(Entity entity) {
        final String name = entity.getDataSource().getPrimaryKey().getName();
        final StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ").append("`").append(entity.getDataSource().getTable()).append("` SET ");
        for (Iterator<ColumnMetaData> iterator = entity.getDataSource().getColumns().iterator(); iterator.hasNext(); ) {
            ColumnMetaData metaData = iterator.next();
//            if (metaData.getName().equals(name)) {
//                continue;
//            }
            if (entity.getData().containsKey(metaData.getName())) {
                builder.append("`").append(metaData.getName()).append("` = #").append(metaData.getName());
                if (iterator.hasNext()) {
                    builder.append(", ");
                }
            }
        }
        builder.append(" WHERE `").append(name).append("` = #").append(name).append(";");
        return builder.toString();
    }

    @Override
    public String getInsertStatement(Entity entity) {
        final StringBuilder builder = new StringBuilder();
        final String pk = entity.getDataSource().getPrimaryKey().getName();
        builder.append("INSERT INTO ").append("`").append(entity.getDataSource().getTable()).append("`(");
        boolean first = true;
        for (Iterator<ColumnMetaData> iterator = entity.getDataSource().getColumns().iterator(); iterator.hasNext(); ) {
            ColumnMetaData metaData = iterator.next();
//            if (metaData.getName().equals(pk)) {
//                continue;
//            }
            if (entity.getData().containsKey(metaData.getName())) {
//                System.out.println("col name: metaData.getName()");
                if (!first) {
                    builder.append(", ");
                }
                first = false;
                builder.append("`").append(metaData.getName()).append("`");
            }
        }
        first = true;
        builder.append(") VALUES (");
        for (Iterator<ColumnMetaData> iterator = entity.getDataSource().getColumns().iterator(); iterator.hasNext(); ) {
            ColumnMetaData metaData = iterator.next();
//            if (metaData.getName().equals(pk)) {
//                continue;
//            }
            if (entity.getData().containsKey(metaData.getName())) {
                if (!first) {
                    builder.append(", ");
                }
                first = false;
                builder.append("#").append(metaData.getName());

            }
        }
        builder.append(");");
        return builder.toString();
    }

    @Override
    public String getCreateTableStatement(DataSource dataSource) {
        final StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS `").append(dataSource.getTable()).append("` (");
        for (Iterator<ColumnMetaData> iterator = dataSource.getColumns().iterator(); iterator.hasNext(); ) {
            ColumnMetaData metaData = iterator.next();
            builder.append("`").append(metaData.getName()).append("` ");
            builder.append(getSqlType(metaData.getType()));
            if (!metaData.isNullable()) {
                builder.append(" NOT NULL");
            }
            if (metaData.equals(dataSource.getPrimaryKey())) {
//                System.out.println("primery key");
                builder.append(" AUTO_INCREMENT");
            }
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append(" ,PRIMARY KEY (`" + dataSource.getPrimaryKey().getName() + "`)");
        builder.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        return builder.toString();
    }

    private Object getSqlType(Class<?> type) {
        if (Integer.class.equals(type)) {
            return "INT";
        } else if (Short.class.equals(type)) {
            return "TINYINT";
        } else if (Long.class.equals(type)) {
            return "BIGINT";
        } else if (Character.class.equals(type)) {
            return "CHAR(1)";
        } else if (String.class.equals(type)) {
            return "TEXT";
        } else if (Boolean.class.equals(type)) {
            return "BOOL";
        } else if (Date.class.equals(type)) {
            return "BIGINT";
        }
        return null;
    }

    @Override
    public String getAlterPrimaryKeyStatement(DataSource dataSource) {
        final StringBuilder builder = new StringBuilder();
        builder.append("ALTER TABLE `").append(dataSource.getTable()).append("` ADD PRIMARY KEY (`");
        builder.append(dataSource.getPrimaryKey().getName()).append("`);");
        return builder.toString();
    }
}
