package edu.sharif.ce.mir.dal.entities;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 6/28/12
 * Time: 7:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class queueEntity {

    public String title;
    String artist;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}
