package edu.sharif.ce.mir.console.impl.extensions;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.api.Command;
import edu.sharif.ce.mir.console.api.Extension;
import edu.sharif.ce.mir.console.api.OnLoad;
import edu.sharif.ce.mir.console.extension.tags.AutoLoadedExtension;
import edu.sharif.ce.mir.console.io.impl.PrimitiveOutput;
import edu.sharif.ce.mir.dal.data.Entity;
import edu.sharif.ce.mir.dal.data.impl.Searcher;
import edu.sharif.ce.mir.dal.data.impl.SongSearcher;
import edu.sharif.ce.mir.dal.datasource.Songs;
import edu.sharif.ce.mir.dal.entities.Song;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;

import java.sql.SQLException;
import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 2:54)
 */
@Extension
public class QueryConsole implements AutoLoadedExtension {
    private ArrayList<Song> songs;
    private Searcher searcher;
    MySqlDataStorage storage;
    private ArrayList<Song> queryResultHistoryForSuggestion;

    @OnLoad
    public void init(Console console) throws SQLException {
        //TODO: load songs from db here
        songs = new ArrayList<Song>();
        storage = new MySqlDataStorage("localhost", "musics", "musics", "1234");
        storage.connect();
//        List<Entity> entities = storage.selectAll(new Songs());
//        for (Entity entity : entities) {
//            Song song = entity.toObject(Song.class);
//            songs.add(song);
//        }
        int c =0;
//        ResultSet rs = storage.execute(sql);
//        if (rs != null) {
//            while (rs.next()) {
//                String title = rs.getString("title");
//                String genre = rs.getString("genre");
//                String artist = rs.getString("artist");
//                String album = rs.getString("album");
//                int releaseYear = rs.getInt("releaseYear");
//                String lyrics = rs.getString("lyric");
//                Song song = new Song(title, genre, artist, album, releaseYear, lyrics);
//                songs.add(song);
//                c++;
//            }
//        }
//        songs.add(new SongBean("amirali akbari", "Salam salam man oomadam"));
//        songs.add(new SongBean("hamed tahmooresi", "salam Pish Pish Pish"));

        searcher = new SongSearcher(storage, songs);
        console.write(new PrimitiveOutput(c + " songs loaded"));
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
        print(console, results);
    }

    @Command(
            definition = "look for #string(query) show #integer(limit)"
    )
    public void query_limit(Console console, Map<String, Object> arguments) {
        String query = (String) arguments.get("query");
        Integer limit = (Integer) arguments.get("limit");
        console.write(new PrimitiveOutput("Querying:" + query));
        Map<Song, Double> results = searcher.search(query, null, limit);
        print(console, results);
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
        print(console, results);
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
        print(console, results);
    }

    @Command(
            definition = "artists from #string(era)"
    )
    public void era_search(Console console, Map<String, Object> arguments) {
        String era = (String) arguments.get("era");
        int index = 1;
        for (String artist: searcher.era_search(era)){
            console.write(new PrimitiveOutput(index + ") " + artist));
            index++;
        }
    }
    @Command(
            definition = "songs like #string(query)"
    )
    public void likeSearch(Console console, Map<String, Object> arguments) {
        String query = (String) arguments.get("query");
        Map<Song, Double> results = searcher.search(query, "title", 10);
//        print(console,results);
        ArrayList<Song> overallResults = new ArrayList<Song>();
        Song s1 = null;
        Song s2 = null;
        Song s3 = null;

        boolean isFirstIteration = true;
        ArrayList<Song> resultsArrayList =  new ArrayList<Song>();
        for (Iterator it=results.keySet().iterator(); it.hasNext(); ) {
            resultsArrayList.add((Song)it.next());
        }
        if (resultsArrayList.size() < 3){
            overallResults = resultsArrayList;
        }else{
            for (int i = resultsArrayList.size() -1 ;i>=0;i--){
                if (isFirstIteration){
                    s1 = resultsArrayList.get(i);
                    s2 = resultsArrayList.get(i-1);
                    s3 = resultsArrayList.get(i-2);
                    i -= 2;
                    isFirstIteration = false;
                    continue;
                }else{
                    s1 = s2;
                    s2 = s3;
                    s3 = resultsArrayList.get(i);
                }
                Integer relS1 = (int) (results.get(s1) * 100);
                Integer relS2 = (int) (results.get(s2) * 100);
                Integer relS3 = (int) (results.get(s3) * 100);


                Integer relDeltaS1S2 = relS1 - relS2;
                Integer relDeltaS2S3 = relS2 - relS3;
                if (relDeltaS1S2 >= relDeltaS2S3 * 2){
                    overallResults.add(s1);
                    break;
                }else{
                    overallResults.add(s1);
                }
            }
        }
        int index = 0;
        queryResultHistoryForSuggestion = overallResults;
        for (Song song : overallResults) {
            index ++;
            console.write(new PrimitiveOutput(index + "- " + song.getTitle() + " - " + song.getArtist() + " - " + song.getAlbum() + " - " + song.getReleaseyear()));
        }
    }

    @Command(
            definition = "#integer(index)"
    )
    public void likeSearchStep2(Console console, Map<String, Object> arguments) {
        if (this.queryResultHistoryForSuggestion == null){
            console.write(new PrimitiveOutput("You did not entered \'songs like\' commad before!"));
            return;
        }
        Integer index = (Integer) arguments.get("index");
        if (index > this.queryResultHistoryForSuggestion.size() || index < 1){
            console.write(new PrimitiveOutput("Index out of range, boy!"));
            return;
        }
        Map<String,Object> argument = new HashMap<String, Object>();
        argument.put("query",this.queryResultHistoryForSuggestion.get(index).getTitle());
        likeSearch(console,argument);
        return;
    }
    private void print(Console console, Map<Song, Double> results) {
        for (Song song : results.keySet()) {
            Integer rel = (int) (results.get(song) * 100);
            console.write(new PrimitiveOutput("(Song by:" + song.title + " = " + rel.toString() + "%)"));
        }
    }
}
