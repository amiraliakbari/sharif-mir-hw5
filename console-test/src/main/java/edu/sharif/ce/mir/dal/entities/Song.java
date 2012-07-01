package edu.sharif.ce.mir.dal.entities;

import edu.sharif.ce.mir.dal.DataSource;
import edu.sharif.ce.mir.dal.data.Entity;


public class Song extends Entity {
    private static double[] columnImpacts = {0.2, 0.1, 0.2,0.2,0.1,0.2};

    public String title;
    String genre;
    String artist;
    String album;
    int releaseYear;
    public String lyrics;

    /**
     * @param dataSource the data source for which this entity is being created
     */
    public Song(DataSource dataSource) {
        super(dataSource);
    }

    public Song(DataSource dataSource, String title, String genre, String artist, String album, int releaseYear, String lyrics) {
        super(dataSource);
        this.title = title;
        this.genre = genre;
        this.artist = artist;
        this.album = album;
        this.releaseYear = releaseYear;
        this.lyrics = lyrics;
    }
    public Song( String title, String genre, String artist, String album, int releaseYear, String lyrics) {
        super(null);
        this.title = title;
        this.genre = genre;
        this.artist = artist;
        this.album = album;
        this.releaseYear = releaseYear;
        this.lyrics = lyrics;
    }

    /* JUST FOR DB-less TESTING, REMOVE AFTER DB INTEGRATION */
//    public SongBean(String a, String l) {
//        super(null);
//        title = a;
//        lyrics = l;
//        map.put("id", 1);
//        map.put("title", title);
//        map.put("lyrics", lyrics);
//    }
//    /* END TEST CODE */

    public static double getColumnImpact(int index) {
        return columnImpacts[index];
    }
}
