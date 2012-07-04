package edu.sharif.ce.mir.dal.entities;

import java.util.HashMap;
import java.util.Map;

public class Song{
    private static Map<String, Double> columnImpacts = new HashMap<String, Double>();

    static {
        columnImpacts.put("id", 0.0);
        columnImpacts.put("genre", 0.2);
        columnImpacts.put("artist", 0.1);
        columnImpacts.put("album", 0.2);
        columnImpacts.put("releaseyear", 0.2);
        columnImpacts.put("title", 0.1);
        columnImpacts.put("lyric", 0.2);
    }

    protected Map<String, Object> map = new HashMap<String, Object>();
    Long id;
    String genre;
    String artist;
    String album;
    int releaseyear;
    public String title;
    public String lyric;
    public Song(){

    }

    public Song(Long id, String genre, String artist, String album, int releaseyear, String title, String lyric) {
        setId(id);
        setGenre(genre);
        setArtist(artist);
        setAlbum(album);
        setReleaseyear(releaseyear);
        setTitle(title);
        setLyric(lyric);
    }

    public static double getColumnImpact(String column) {
        return columnImpacts.get(column);
    }

    public void setTitle(String title) {
        this.title = title;
        map.put("title", title);
    }

    public void setGenre(String genre) {
        this.genre = genre;
        map.put("genre", genre);
    }

    public void setArtist(String artist) {
        this.artist = artist;
        map.put("artist", artist);
    }

    public void setAlbum(String album) {
        this.album = album;
        map.put("album", album);
    }

    public void setReleaseyear(int releaseyear) {
        this.releaseyear = releaseyear;
        map.put("releaseyear", releaseyear);
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
        map.put("lyric", lyric);
    }

    public static Map<String, Double> getColumnImpacts() {
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
        map.put("id", id);
    }

    public Map<String, Object> getData() {
        return map;
    }

    public Object get(String column) {
        return map.get(column);
    }
    
    public String toString(){
        return title + "(" + artist + ")";
    }
}
