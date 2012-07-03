package edu.sharif.ce.mir.clustering;

import edu.sharif.ce.mir.dal.entities.Song;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/3/12
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vector {

    private Long id;
    private HashMap<String, Integer> list;


    private String getStemmed(String str) {
        return null;
    }

    private String removeStopWord(String lyric) {
        return null;
    }

    private void initiateMap(String lyric) {

    }

    public Vector(Song song) {
        this.id = song.getId();
        list = new HashMap<String, Integer>();
        String newLyric = this.removeStopWord(song.getLyric());
        newLyric = this.getStemmed(newLyric);
    }

    public Long getId() {
        return id;
    }

    public HashMap<String, Integer> getList() {
        return list;
    }
}
