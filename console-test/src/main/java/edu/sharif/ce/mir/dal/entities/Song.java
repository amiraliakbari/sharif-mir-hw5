package edu.sharif.ce.mir.dal.entities;

public class Song {
    private static double[] columnImpacts = {0,0.2, 0.1, 0.2,0.2,0.1,0.2};
    Long id;
    public String title;
    String genre;
    String artist;
    String album;
    int releaseyear;
    public String lyric;

//    /**
//     * @param dataSource the data source for which this entity is being created
//     */
//    public Song(DataSource dataSource) {
//        super(dataSource);
//    }
//
//
//    public Song(DataSource dataSource, String title, String genre, String artist, String album, int releaseyear, String lyrics) {
//        super(dataSource);
////        this.title = title;
////        this.genre = genre;
////        this.artist = artist;
////        this.album = album;
////        this.releaseyear = releaseyear;
////        this.lyrics = lyrics;
//    }
//    public Song( String title, String genre, String artist, String album, int releaseyear, String lyrics) {
//        super(null);
//        this.title = title;
//        this.genre = genre;
//        this.artist = artist;
//        this.album = album;
//        this.releaseyear = releaseyear;
//        this.lyrics = lyrics;
//    }

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

    public static void setColumnImpacts(double[] columnImpacts) {
        Song.columnImpacts = columnImpacts;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setReleaseyear(int releaseyear) {
        this.releaseyear = releaseyear;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public static double[] getColumnImpacts() {
        return columnImpacts;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public int getReleaseyear() {
        return releaseyear;
    }

    public String getLyric() {
        return lyric;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
