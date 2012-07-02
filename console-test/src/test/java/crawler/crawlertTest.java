package crawler;

import edu.sharif.ce.mir.crawler.Crawler;
import edu.sharif.ce.mir.crawler.Lyrics;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 6/27/12
 * Time: 10:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class crawlertTest {


    @Test
    public void htmlTest(){
        final MySqlDataStorage storage = new MySqlDataStorage("localhost", "musics", "root", "mysql");
        storage.connect();
        Crawler crawler = new Crawler(storage);
        crawler.crawlOneMusic("Cher","Believe");
//        crawler.crawlOneMusic("Csddwher","Believe");
    }
    @Test
    public void similarTest() throws SQLException {
        final MySqlDataStorage storage = new MySqlDataStorage("localhost", "musics", "root", "mysql");
        storage.connect();
        Crawler crawler = new Crawler(storage);
        crawler.updateQueue("Cher", "Believe",4);
//        crawler.crawlOneMusic("Csddwher","Believe");
    }

    @Test
    public void crawlerTest(){
        final MySqlDataStorage storage = new MySqlDataStorage("localhost", "musics", "root", "mysql");
        storage.connect();
        Thread crawler = new Thread(new Crawler(storage));
        crawler.start();
    }

    @Test
    public void patternTest(){
        Lyrics lyrics = new Lyrics();
        lyrics.getTxt("123aa rr rtyg34 de34 ff435 aa23");

    }
}
