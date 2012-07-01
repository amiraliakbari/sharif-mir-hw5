package edu.sharif.ce.mir.dal.datasource;

import edu.sharif.ce.mir.dal.Collections;
import edu.sharif.ce.mir.dal.ColumnMetaData;
import edu.sharif.ce.mir.dal.DataSource;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 6/28/12
 * Time: 7:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Queue implements DataSource {
    public static final String TABLE_NAME = "queue";
    private final ColumnMetaData id = new ColumnMetaData("id", Long.class, false);
    private final ColumnMetaData title = new ColumnMetaData("title", String.class, false);
    private final ColumnMetaData artist = new ColumnMetaData("artist", String.class, false);
    @Override
    public List<ColumnMetaData> getColumns() {
        return Collections.list(id, title, artist);
    }

    @Override
    public ColumnMetaData getPrimaryKey() {
        return id;
    }

    @Override
    public String getTable() {
        return TABLE_NAME;
    }

    @Override
    public String createSql() {
//        return "CREATE TABLE `wiki` (  `id` INT(10) NOT NULL AUTO_INCREMENT,`title` VARCHAR(255) NOT NULL,`content` MEDIUMTEXT NOT NULL,`time` DATETIME NULL DEFAULT NULL,`permanent` VARCHAR(255) NULL DEFAULT NULL,`pageid` INT(10) NULL DEFAULT NULL,PRIMARY KEY (`id`),UNIQUE INDEX `title` (`title`))COLLATE='utf8_general_ci'ENGINE=InnoDBROW_FORMAT=DEFAULTAUTO_INCREMENT=250 ";
        return "CREATE TABLE `queue` (\n" +
                "\t`id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "\t`title` VARCHAR(255) NOT NULL DEFAULT '0',\n" +
                "\t`artist` VARCHAR(255) NOT NULL DEFAULT '0',\n" +
                "\tPRIMARY KEY (`id`)\n" +
                "\tUNIQUE INDEX `title_artist` (`title`, `artist`)\n" +
                ")\n" +
                "COLLATE='utf8_general_ci'\n" +
                "ENGINE=InnoDB\n" +
                "ROW_FORMAT=DEFAULT";
    }
}
