package aggregator;

import edu.sharif.ce.mir.aggregator.SongBusiness;
import edu.sharif.ce.mir.aggregator.entities.SongBean;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 12:08)
 */
public class SongBusinessTest {

    @Test
    public void catchMetadata() throws Exception {
        SongBusiness songBusiness = new SongBusiness();
        SongBean songBean = songBusiness.getSongMetadata("Cher", "Believe");
        Assert.assertEquals("Believe", songBean.getTitle());
        Assert.assertEquals("Cher", songBean.getArtist());
        Assert.assertEquals("The Very Best of Cher", songBean.getAlbum());
        Assert.assertEquals("pop", songBean.getGenre());
        Assert.assertEquals(2005, songBean.getReleaseYear());
    }

    @Test
    public void aggregateSongs() throws Exception {
        SongBusiness songBusiness = new SongBusiness();
        int count = 0;
        Queue queue = new LinkedList();
        SongBean curSongBean = songBusiness.getSongMetadata("Carly Rae Jepsen", "Call Me Maybe");
        queue.add(curSongBean);
        while (count < 500) {
            curSongBean = (SongBean) queue.remove();
            List<SongBean> similars = songBusiness.getSimilarSongs(curSongBean.getArtist(), curSongBean.getTitle());
            queue.addAll(similars);
            count += similars.size();
        }
    }

}
