package edu.sharif.ce.mir.aggregator.entities;

public class SongBean {
    String title;
    String genre;
    String artist;
    String album;
    int releaseYear;
    String lyrics;

    public SongBean() {
    }

    public SongBean(String title, String genre, String artist, String album, int releaseYear, String lyrics) {
        super();
        this.title = title;
        this.genre = genre;
        this.artist = artist;
        this.album = album;
        this.releaseYear = releaseYear;
        this.lyrics = lyrics;
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

    public String getLyrics() {
        return lyrics;
    }

    public String getTitleS() {
        if (title != null)
            return title.replace("\'", "\\\'");
        return "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenreS() {
        if (genre != null)
            return genre.replace("\'", "\\\'");
        return "";
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtistS() {
        if (artist != null)
            return artist.replace("\'", "\\\'");
        return "";
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumS() {
        if (album != null)
            return album.replace("\'", "\\\'");
        return "";
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getLyricsS() {
        if (lyrics != null)
            return lyrics.replace("\'", "\\\'");
        return "";
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    @Override
    public String toString() {
        return "title:" + title+" artist: "+artist+" genre:"+genre+" album:"+album+" releaseYear:"+releaseYear+" lyric:"+releaseYear;
    }
}
