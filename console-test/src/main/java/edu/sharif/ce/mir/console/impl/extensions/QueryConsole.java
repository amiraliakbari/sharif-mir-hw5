package edu.sharif.ce.mir.console.impl.extensions;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.api.Command;
import edu.sharif.ce.mir.console.api.Extension;
import edu.sharif.ce.mir.console.api.OnLoad;
import edu.sharif.ce.mir.console.extension.tags.AutoLoadedExtension;
import edu.sharif.ce.mir.console.io.impl.PrimitiveOutput;
import edu.sharif.ce.mir.dal.data.impl.Searcher;
import edu.sharif.ce.mir.dal.data.impl.SongSearcher;
import edu.sharif.ce.mir.dal.entities.Artist;
import edu.sharif.ce.mir.dal.entities.Song;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 2:54)
 */
@Extension
public class QueryConsole implements AutoLoadedExtension {
    private ArrayList<Song> songs;
    private ArrayList<Artist> artists;
    private Searcher searcher;
    MySqlDataStorage storage;


    @OnLoad
    public void init(Console console) throws SQLException {
        //TODO: load songs from db here
        songs = new ArrayList<Song>();
        artists = new ArrayList<Artist>();
        storage = new MySqlDataStorage("localhost", "musics", "musics", "1234");
        storage.connect();


        int ac = 0;
        ResultSet ars = storage.execute("SELECT * FROM artists");
        while (ars.next()){
            artists.add(new Artist(ars.getString("artist"), ars.getString("years"), ars.getString("genres")));
            ac++;
        }

        int c =0;
        ResultSet rs = storage.execute("SELECT * FROM songs");
        while (rs.next()){
            songs.add(new Song(rs.getLong("id"), rs.getString("genre"), rs.getString("artist"), rs.getString("album"),
                    rs.getInt("releaseyear"), rs.getString("title"), rs.getString("lyric")));
            c++;
        }

        searcher = new SongSearcher(storage, songs, artists);
        console.write(new PrimitiveOutput(c + " songs loaded"));
        console.write(new PrimitiveOutput(ac + " artists loaded"));
    }

    @Command(
            definition = "set ranking to ra #integer(alg)"
    )
    public void setRanking(Console console, Map<String, Object> arguments) {
        searcher.setRankingMethod((Integer) arguments.get("alg"));
        console.write(new PrimitiveOutput("Ranking algorithm changed."));
    }

    @Command(
            definition = "look for #string(query)"
    )
    public void query(Console console, Map<String, Object> arguments) {
        String query = (String) arguments.get("query");
        console.write(new PrimitiveOutput("Querying:" + query));
        Map<Song, Double> results = searcher.search(query);
        printSongs(console, results);
    }

    @Command(
            definition = "look for #string(query) show #integer(limit)"
    )
    public void query_limit(Console console, Map<String, Object> arguments) {
        String query = (String) arguments.get("query");
        Integer limit = (Integer) arguments.get("limit");
        console.write(new PrimitiveOutput("Querying:" + query));
        Map<Song, Double> results = searcher.search(query, null, limit);
        printSongs(console, results);
    }

    @Command(
            definition = "look for #string(query) in #string(column)"
    )
    public void query_column(Console console, Map<String, Object> arguments) {
        String query = (String) arguments.get("query");
        String column = (String) arguments.get("column");
//        System.out.print("vahid: "+query+" "+column);

        console.write(new PrimitiveOutput("Querying:" + query));
        Map<Song, Double> results = searcher.search(query, column);
        printSongs(console, results);
    }

    @Command(
            definition = "look for #string(query) in #string(column) show #integer(limit)"
    )
    public void search(Console console, Map<String, Object> arguments) {
        String query = (String) arguments.get("query");
        String column = (String) arguments.get("column");
        Integer limit = (Integer) arguments.get("limit");
        console.write(new PrimitiveOutput("Querying:" + query + "," + (column != null ? column : "all") + "," + limit));
        Map<Song, Double> results = searcher.search(query, column, limit);
        printSongs(console, results);
    }

    @Command(
            definition = "artists from #string(era)"
    )
    public void era_search(Console console, Map<String, Object> arguments) {
        String era = (String) arguments.get("era");
        printList(console, searcher.artist_era_search(era));
    }

    @Command(
            definition = "artists like #string(artist)"
    )
    public void artist_search(Console console, Map<String, Object> arguments) {
        String artist = (String) arguments.get("artist");
        Artist artistBean = new Artist(storage, artist);
        if (!artistBean.load()){
            printList(console, searcher.artist_name_search(artist));
        } else {
            printArtists(console, searcher.artist_similar_search(artistBean));
        }
    }

    @Command(
            definition = "artists like #string(artist) show #integer(limit)"
    )
    public void artist_search_limit(Console console, Map<String, Object> arguments) {
        String artist = (String) arguments.get("artist");
        Integer limit = (Integer) arguments.get("limit");
        Artist artistBean = new Artist(storage, artist);
        if (!artistBean.load()){
            printList(console, searcher.artist_name_search(artist));
        } else {
            printArtists(console, searcher.artist_similar_search(artistBean, limit));
        }
    }

    private void printSongs(Console console, Map<Song, Double> results) {
        for (Song song : results.keySet()) {
            Integer rel = (int) (results.get(song) * 100);
            console.write(new PrimitiveOutput("(Song by:" + song.title + " = " + rel.toString() + "%)"));
        }
    }

    private void printArtists(Console console, Map<Artist, Double> results) {
        for (Artist artist: results.keySet()) {
            Integer rel = (int) (results.get(artist) * 100);
            console.write(new PrimitiveOutput("(Artist:" + artist.getName() + " = " + rel.toString() + "%)"));
        }
    }

    private void printList(Console console, Iterable<String> strings){
        int index = 1;
        for (String artist: strings){
            console.write(new PrimitiveOutput(index + ") " + artist));
            index++;
        }
    }


}
