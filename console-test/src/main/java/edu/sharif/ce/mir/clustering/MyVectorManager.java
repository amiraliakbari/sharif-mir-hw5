package edu.sharif.ce.mir.clustering;

import edu.sharif.ce.mir.dal.data.Entity;
import edu.sharif.ce.mir.dal.datasource.Songs;
import edu.sharif.ce.mir.dal.entities.Song;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/3/12
 * Time: 8:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyVectorManager implements VectorManager {
    MySqlDataStorage storage;
    private Dictionary dic;

    public MyVectorManager(MySqlDataStorage storage) throws SQLException {
        this.storage = storage;
        dic = new Dictionary(storage);
    }


    @Override
    public List<Vector> getAllMusics() throws SQLException {
        List<Entity> entities = storage.selectAll(new Songs());
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        for (Entity entity : entities) {
            Song song = entity.toObject(Song.class);
            if (!song.getLyric().equals("")) {
                Vector v = new Vector(dic, song);
                vectors.add(v);
            } else {
//                System.out.println("noLyric");
            }
        }
        return vectors;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
