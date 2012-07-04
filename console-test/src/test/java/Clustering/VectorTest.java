package Clustering;

import edu.sharif.ce.mir.clustering.Dictionary;
import edu.sharif.ce.mir.clustering.MyVectorManager;
import edu.sharif.ce.mir.clustering.StopWords;
import edu.sharif.ce.mir.clustering.Vector;
import edu.sharif.ce.mir.clustering.stemming.Stemmer;
import edu.sharif.ce.mir.clustering.stemming.impl.EnglishStemmer;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;
import junit.framework.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

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
        boolean result = sw.isStopWord("across") ;
        Assert.assertEquals(true, result);
    }

    @Test
    public void stemming() {
        Stemmer st = new EnglishStemmer();
        StopWords sw = new StopWords();
//        String result=sw.removeStopWords("the the the");
        String result = st.stem("y");
        System.out.println(result);
//        Assert.assertEquals("shlfd  sdhf  fs fs fsjhf ",result);
    }

    @Test
    public void tokenTest() {
        final MySqlDataStorage storage = new MySqlDataStorage("localhost", "musics", "musics", "1234");
        storage.connect();
//        Song song= new Song(new Long(1),null,null,null,1,null,"ants the played");
//        Vector v = new Vector(song);
        try {
            MyVectorManager manager= new MyVectorManager(storage);
            List<Vector> vectors=manager.getAllMusics();
            System.out.println("final: "+vectors.size());

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    @Test
    public void dictionaryTest(){
        final MySqlDataStorage storage = new MySqlDataStorage("localhost", "musics", "musics", "1234");
        storage.connect();
        try {
            Dictionary dic = new Dictionary(storage);
            System.out.println(dic.getDBId("hhh"));
            System.out.println(dic.getDBId("hhh"));
            System.out.println(dic.getDBId("aaa"));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
