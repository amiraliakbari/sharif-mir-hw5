package edu.sharif.ce.mir.crawler;

import de.umass.lastfm.Track;
import edu.sharif.ce.mir.aggregator.SongBusiness;
import edu.sharif.ce.mir.aggregator.entities.SongBean;
import edu.sharif.ce.mir.dal.data.Entity;
import edu.sharif.ce.mir.dal.datasource.Queue;
import edu.sharif.ce.mir.dal.datasource.Songs;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 6/27/12
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Crawler implements Runnable {
    private SongBusiness songBusiness = new SongBusiness();
    private Lyrics lyrics = new Lyrics();
    private MySqlDataStorage mySqlDataStorage;
    private Songs datasource = new Songs();
    private Queue queueDatasource = new Queue();
    private final int maxNewTrack = 10;
    private static boolean runner = true;

    public Crawler(MySqlDataStorage mySqlDataStorage) {
        this.mySqlDataStorage = mySqlDataStorage;
    }

    public void crawlOneMusic(String artist, String songName) {
        SongBean songBean;
        String lyric;
        try {
            songBean = songBusiness.getSongMetadata(artist, songName);
        } catch (Exception e) {
            return;
        }

        try {
            lyric = lyrics.getLyric(artist, songName);
            songBean.setLyrics(lyric);
//            System.out.println(lyric);
        } catch (IOException e) {
            System.out.println("++lyric not found++");
        }

        Entity entity = new Entity(datasource);
        try {
//            System.out.println(songBean);
            mySqlDataStorage.insert(entity.set("title", songBean.getTitleS()).set("genre", songBean.getGenreS())
                    .set("artist", songBean.getArtistS()).set("album", songBean.getAlbumS()).set("lyric", songBean.getLyricsS()).set("releaseyear", songBean.getReleaseYear()));
            updateQueue(artist, songName, maxNewTrack);

        } catch (SQLException e) {
            System.out.println("duplicated name:" + songBean.getTitle());

//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public void updateQueue(String artist, String songName, int length) throws SQLException {
        List<Track> tracks = songBusiness.getSimilarTracks(artist, songName);
//        for(Track t:tracks){
//        System.out.println( t.getName()+":: "+t.getArtist());
//        }
        List<Track> newTracks = getNewTracks(tracks, length);
//        System.out.println("2:   " + newTracks.size());
        List<SongBean> similar = songBusiness.getSimilarSongs(newTracks);
//        System.out.println("3:   " + similar.size());
        Entity entity;
        for (SongBean bean : similar) {
            entity = new Entity(queueDatasource);
            mySqlDataStorage.insert(entity.set("title", bean.getTitleS()).set("artist", bean.getArtistS()));
        }
    }

    /**
     * delete duplicate musics
     *
     * @param tracks
     * @return
     * @throws SQLException
     */
    public List<Track> getNewTracks(List<Track> tracks, int length) throws SQLException {
        ArrayList<Track> newTracks = new ArrayList<Track>();
        for (Track track : tracks) {
            if (newTracks.size() == length)
                break;
            String selectSong=selectSong(track.getName(),track.getArtist());
            ResultSet rs = mySqlDataStorage.execute(selectSong);
            String selectQueue=selectQueue(track.getName(),track.getArtist());
            ResultSet
                    rs2 = mySqlDataStorage.execute(selectQueue);
            if (!rs.next() && !rs2.next()) {
                newTracks.add(track);
            }
        }
        return newTracks;
    }
    private String selectQueue(String title,String artist){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from `queue` where title = '");
        sb.append(title.replace("\'", "\\\'"));
        sb.append("' and artist= '");
        sb.append(artist.replace("\'", "\\\'"));
        sb.append("'");
        return sb.toString();
    }
    private String selectSong(String title,String artist){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from `songs` where title = '");
        sb.append(title.replace("\'", "\\\'"));
        sb.append("' and artist= '");
        sb.append(artist.replace("\'", "\\\'"));
        sb.append("'");
        return sb.toString();
    }
    private String delete(String title,String artist){
        StringBuilder sb = new StringBuilder();
        sb.append("delete from `queue` where title = '");
        sb.append(title.replace("\'", "\\\'"));
        sb.append("' and artist= '");
        sb.append(artist.replace("\'", "\\\'"));
        sb.append("'");
        return sb.toString();
    }

    @Override
    public void run() {
        while (runner) {
            String sql = "select title,artist from queue limit 1";
            ResultSet rs = null;
            String title = null;
            String artist = null;
            boolean safe = false;
            try {
                rs = mySqlDataStorage.execute(sql);
                if (!rs.next()) {
                    System.out.println("queue in empty");
                    System.exit(0);
                }
                title = rs.getString("title");
                artist = rs.getString("artist");

//                StringBuilder sb = new StringBuilder();
//                sb.append("delete from `queue` where title = '");
//                sb.append(title);
//                sb.append("' and artist= '");
//                sb.append(artist);
//                sb.append("'");
                String delete=delete(title,artist);
//                System.out.println(sb.toString());
                mySqlDataStorage.execute2(delete);
                safe = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("crawling start: name=" + title + " artist=" + artist);
            crawlOneMusic(artist, title);
            System.out.println("-------------------------------------------------");
        }
    }

    public static void main(String[] args) {
        final MySqlDataStorage storage = new MySqlDataStorage("localhost", "musics", "root", "mysql");
        storage.connect();
        Thread crawler = new Thread(new Crawler(storage));
        crawler.start();
    }
}
