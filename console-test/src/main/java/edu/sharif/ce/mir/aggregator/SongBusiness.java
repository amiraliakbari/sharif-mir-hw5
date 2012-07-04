package edu.sharif.ce.mir.aggregator;

import de.umass.lastfm.Album;
import de.umass.lastfm.Track;
import edu.sharif.ce.mir.aggregator.entities.SongBean;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class SongBusiness {

    private final String apiKey = "0cc36288b250f08e63ec87878bfe299e";
    private Set<String> validGenres = new HashSet<String>();

    public SongBusiness() {
        InputStream stream=SongBusiness.class
                    .getResourceAsStream("/genres.txt");

        Scanner sc = null;
            sc = new Scanner(stream);
        while (sc.hasNext()) {
            String genre = sc.nextLine();
            validGenres.add(genre.toLowerCase());
        }
    }

    public SongBean getSongMetadata(String artist, String title) {
        try {
            Track track = Track.getInfo(artist, title, apiKey);
            String albumTitle = track.getAlbum();
            Album album = Album.getInfo(artist, albumTitle, apiKey);
            List<String> tagList = (List<String>) track.getTags();
            String genre = "";
            for (String tag : tagList) {
                if (isValidGenre(tag)) {
                    genre = tag;
                }
            }
            int year = 0;
            if (album!=null && album.getReleaseDate() != null) {
                SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
                year = Integer.valueOf(simpleDateformat.format(album.getReleaseDate()));
            }
            return new SongBean(title, genre, artist, albumTitle, year, "");
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public List<Track> getSimilarTracks(String artist, String title){
        return (List<Track>) Track.getSimilar(artist, title, apiKey);
    }

    public List<SongBean> getSimilarSongs(List<Track> similarTracks) {
        List<SongBean> similarSongBeans = new ArrayList<SongBean>();
        for (Track track : similarTracks) {
            SongBean songBean = getSongMetadata(track.getArtist(), track.getName());
            if (songBean != null) {
                similarSongBeans.add(songBean);
                System.out.println("title: " + songBean.getTitle() + "\tartist: " + songBean.getArtist() + "\talbum: " + songBean.getAlbum()
                        + "\tgenre: " + songBean.getGenre() + "\trelease year: " + songBean.getReleaseYear());
            }
        }
        return similarSongBeans;
    }

    public List<SongBean> getSimilarSongs(String artist, String title) {
        List<Track> similarTracks = (List<Track>) Track.getSimilar(artist, title, apiKey);
        System.out.println("similar song, size: "+similarTracks.size());
        List<SongBean> similarSongBeans = new ArrayList<SongBean>();
        for (Track track : similarTracks) {
            SongBean songBean = getSongMetadata(track.getArtist(), track.getName());
            if (songBean != null) {
                similarSongBeans.add(songBean);
                System.out.println("title: " + songBean.getTitle() + "\tartist: " + songBean.getArtist() + "\talbum: " + songBean.getAlbum()
                        + "\tgenre: " + songBean.getGenre() + "\trelease year: " + songBean.getReleaseYear());
            }
        }
        return similarSongBeans;
    }

    private boolean isValidGenre(String tag) {
        return validGenres.contains(tag.toLowerCase());
    }
}