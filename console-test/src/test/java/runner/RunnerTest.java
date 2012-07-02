package runner;

import edu.sharif.ce.mir.dal.data.Entity;
import edu.sharif.ce.mir.dal.datasource.Songs;
import edu.sharif.ce.mir.dal.entities.Song;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/2/12
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class RunnerTest {

    @Test
    public void dbTest() throws SQLException {
        ArrayList<Song>songs = new ArrayList<Song>();
//        String sql = "select * from `songs`";
        MySqlDataStorage storage = new MySqlDataStorage("localhost", "musics", "root", "mysql");
        storage.connect();
        List<Entity> entities = storage.selectAll(new Songs());
        System.out.println("ssss " + entities.size());
        for (Entity entity : entities) {
            Song song = entity.toObject(Song.class);
            System.out.println("for " + song + "    " + entity);
            songs.add(song);
        }
    }
}
