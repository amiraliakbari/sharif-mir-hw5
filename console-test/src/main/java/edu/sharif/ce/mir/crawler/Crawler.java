package edu.sharif.ce.mir.crawler;

import de.umass.lastfm.Track;
import edu.sharif.ce.mir.aggregator.SongBusiness;
import edu.sharif.ce.mir.aggregator.entities.SongBean;
import edu.sharif.ce.mir.dal.data.Entity;
import edu.sharif.ce.mir.dal.datasource.Queue;
import edu.sharif.ce.mir.dal.datasource.Songs;
import edu.sharif.ce.mir.dal.entities.Artist;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Crawler implements Runnable {
    private SongBusiness songBusiness = new SongBusiness();
    private Lyrics lyrics = new Lyrics();
    private MySqlDataStorage mySqlDataStorage;
    private Songs datasource = new Songs();
    private Queue queueDatasource = new Queue();
    protected final int maxNewTrack = 10;
    private static boolean runner = true;

    public Crawler(MySqlDataStorage mySqlDataStorage) {
        this.mySqlDataStorage = mySqlDataStorage;
    }

    public void crawlOneMusic(String artist, String songName) {
        SongBean songBean;
        try {
            songBean = songBusiness.getSongMetadata(artist, songName);
        } catch (Exception e) {
            return;
        }

        try {
            String lyric = lyrics.getLyric(artist, songName);
            songBean.setLyrics(lyric);
        } catch (IOException e) {
            System.out.println("++lyric not found++");
        }

        Entity entity = new Entity(datasource);
        entity.set("title", songBean.getTitleS())
                .set("genre", songBean.getGenreS())
                .set("artist", songBean.getArtistS())
                .set("album", songBean.getAlbumS())
                .set("lyric", songBean.getLyricsS())
                .set("releaseyear", songBean.getReleaseYear());
        try {
            mySqlDataStorage.insert(entity);
            updateQueue(artist, songName, maxNewTrack);
        } catch (SQLException e) {
            System.out.println("duplicated name:" + songBean.getTitle());
        }

        // Indexing artist data
        artist = songBean.getArtistS();
        String year = String.valueOf(songBean.getReleaseYear());
        try {
            Artist artistBean = new Artist(mySqlDataStorage, artist);
            if (!artistBean.load()) {
                mySqlDataStorage.execute2(Artist.createArtistQuery(artist, ","));
            }
            if (year.length() >= 2) {
                year = year.substring(year.length() - 2);
                artistBean.addYear(year);
            } else {
                System.err.println("Invalid year for song: " + year);
            }
            if (songBean.getGenreS().length() > 0){
                artistBean.addGenre(songBean.getGenreS());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Can not fully index artist data.");
        }
    }

    public void updateQueue(String artist, String songName, int length) throws SQLException {
        List<Track> tracks = songBusiness.getSimilarTracks(artist, songName);
        List<Track> newTracks = getNewTracks(tracks, length);
        List<SongBean> similar = songBusiness.getSimilarSongs(newTracks);
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

    public void migrateCrawlTables() throws SQLException {
        mySqlDataStorage.execute2("CREATE TABLE IF NOT EXISTS `songs` (\n" +
                "`id` BIGINT(20) NOT NULL AUTO_INCREMENT,\n" +
                "`title` VARCHAR(255) NOT NULL  DEFAULT '0',\n" +
                "`genre` VARCHAR(255) NULL DEFAULT '0',\n" +
                "`artist` VARCHAR(255) NOT NULL DEFAULT '0',\n" +
                "`album` VARCHAR(255) NULL DEFAULT '0',\n" +
                "`lyric` MEDIUMTEXT NOT NULL,\n" +
                "`releaseyear` INT(11) NOT NULL,\n" +
                "PRIMARY KEY (`id`),\n" +
                "UNIQUE INDEX `title_artist` (`title`, `artist`)\n" +
                ")\n" +
                "COLLATE='utf8_general_ci'\n" +
                "ENGINE=InnoDB\n" +
                "ROW_FORMAT=DEFAULT\n" +
                "AUTO_INCREMENT=80");

        mySqlDataStorage.execute2("CREATE TABLE IF NOT EXISTS `queue` (\n" +
                "`id` BIGINT(20) NOT NULL AUTO_INCREMENT,\n" +
                "`title` VARCHAR(255) NOT NULL DEFAULT '0',\n" +
                "`artist` VARCHAR(255) NOT NULL DEFAULT '0',\n" +
                "PRIMARY KEY (`id`),\n" +
                "UNIQUE INDEX `title_artist` (`title`, `artist`)\n" +
                ")\n" +
                "COLLATE='utf8_general_ci'\n" +
                "ENGINE=InnoDB\n" +
                "ROW_FORMAT=DEFAULT\n" +
                "AUTO_INCREMENT=140");
        
        mySqlDataStorage.execute2("CREATE TABLE IF NOT EXISTS `artists` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `artist` varchar(255) COLLATE utf8_unicode_ci NOT NULL,\n" +
                "  `years` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT ',',\n" +
                "  `genres` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT ',',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1");
    }

    @Override
    public void run() {
        try {
            migrateCrawlTables();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Can not create database schema, aborting.");
            return;
        }
        while (runner) {
            String sql = "select title,artist from queue limit 1";
            ResultSet rs = null;
            String title = null;
            String artist = null;
            boolean safe = false;
            try {
                rs = mySqlDataStorage.execute(sql);
                if (!rs.next()) {
                    System.out.println("queue is empty");
                    System.exit(0);
                }
                title = rs.getString("title");
                artist = rs.getString("artist");

                String delete=delete(title,artist);
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
//        StringBuilder sb = new StringBuilder();
//        String year = "09";
//        sb.append("UPDATE `artists` SET `years` = years + '"+year+",' WHERE artist = '");
//        sb.append("aname".replace("\'", "\\\'"));
//        sb.append("' and years NOT LIKE '%,"+year+",%'");
//        System.err.println(sb.toString());
        final MySqlDataStorage storage = new MySqlDataStorage("81.31.190.236", "musics", "musics", "1234");
        storage.connect();
        Thread crawler = new Thread(new Crawler(storage));
        crawler.start();
    }
}
