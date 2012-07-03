package Clustering;

import edu.sharif.ce.mir.clustering.StopWords;
import edu.sharif.ce.mir.clustering.Vector;
import edu.sharif.ce.mir.clustering.stemming.Stemmer;
import edu.sharif.ce.mir.clustering.stemming.impl.EnglishStemmer;
import edu.sharif.ce.mir.dal.entities.Song;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/3/12
 * Time: 8:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class VectorTest {
    @Test
    public void StopWordTest() {
        StopWords sw = new StopWords();
        String result = sw.removeStopWords("shlfd s sdhf s fs fs fsjhf s");
        Assert.assertEquals("shlfd  sdhf  fs fs fsjhf ", result);
    }

    @Test
    public void stemming() {
        Stemmer st = new EnglishStemmer();
        StopWords sw = new StopWords();
//        String result=sw.removeStopWords("the the the");
        String result = st.stem("ants hours played");
//        Assert.assertEquals("shlfd  sdhf  fs fs fsjhf ",result);
    }

    @Test
    public void tokenTest() {
        Song song= new Song(new Long(1),null,null,null,1,null,"ants the played");
        Vector v = new Vector(song);

    }
}
