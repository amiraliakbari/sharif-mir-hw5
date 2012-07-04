package edu.sharif.ce.mir.clustering;

import edu.sharif.ce.mir.dal.data.Entity;
import edu.sharif.ce.mir.dal.datasource.Songs;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/4/12
 * Time: 12:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class Dictionary {
    MySqlDataStorage storage;

    public Dictionary() throws SQLException {
        storage = new MySqlDataStorage("localhost", "musics", "musics", "1234");
        storage.connect();
         List<Entity> entities=storage.selectAll(new Songs());
//        int c =0;
//        ResultSet rs = storage.execute("SELECT * FROM songs");
//        while (rs.next()){
//            new Song(rs.getLong("id"), rs.getString("genre"), rs.getString("artist"), rs.getString("album"),
//                    rs.getInt("releaseyear"), rs.getString("title"), rs.getString("lyric"));
//            c++;
//        }
    }
}
