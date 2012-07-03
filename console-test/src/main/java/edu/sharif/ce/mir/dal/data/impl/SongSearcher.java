package edu.sharif.ce.mir.dal.data.impl;


import edu.sharif.ce.mir.dal.entities.Artist;
import edu.sharif.ce.mir.dal.entities.Song;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SongSearcher implements Searcher {
    private Iterable<Song> songs;
    private Iterable<Artist> artists;
    private int rankingMethod = Searcher.RANK_BASIC;
    private MySqlDataStorage storage;

    public SongSearcher(MySqlDataStorage storage, Iterable<Song> songs, Iterable<Artist> artists){
        this.storage = storage;
        this.songs = songs;
        this.artists = artists;
    }
    
    public void setRankingMethod(int rankingMethod){
        this.rankingMethod = rankingMethod;
    }

    /**
     * 
     * @param query The search query
     * @param column Target Column, null to search all columns
     * @param limit needed results count, defaults to 1000
     */
    public Map<Song, Double> search(String query, String column, int limit){
        if (limit==0)
            limit = 1000;
        MinHeap results = new MinHeap(limit);
        for (Song song: songs){
            double score = getScore(song, query, column);
            if (score == 0) continue;
            if (results.getLen() < limit) {
                results.add(score, song);
            } else if (score > results.min()){
                results.removeMin();
                results.add(score, song);
            }
        }
        Map<Song, Double> ranking = new HashMap<Song, Double>();
        while (!results.isEmpty()){
            ranking.put((Song) results.minId(), results.min());
            results.removeMin();
        }
        return ranking;
    }

    public Map<Song, Double> search(String query, String column){
        return search(query, column, 0);
    }

    public Map<Song, Double> search(String query){
        return search(query, null);
    }

    @Override
    public Iterable<String> artist_era_search(String era) {
        ArrayList<String> artists = new ArrayList<String>();
        try {
            ResultSet rs = storage.execute("SELECT artist FROM artists WHERE years LIKE '%,"+era.charAt(0)+"_,%'");
            while (rs.next()){
                artists.add(rs.getString("artist"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }

    @Override
    public Iterable<String> artist_name_search(String name) {
        ArrayList<String> artists = new ArrayList<String>();
        try {
            ResultSet rs = storage.execute("SELECT artist FROM artists WHERE artist LIKE '%"+name+"%'");
            while (rs.next()){
                artists.add(rs.getString("artist"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }

    @Override
    public Map<Artist, Double> artist_similar_search(Artist artist, int limit) {
        if (limit==0)
            limit = 1000;
        MinHeap results = new MinHeap(limit);
        for (Artist other: artists){
            double score = artist.compareTo(other);
            if (score == 0) continue;
            if (results.getLen() < limit) {
                results.add(score, other);
            } else if (score > results.min()){
                results.removeMin();
                results.add(score, other);
            }
        }
        Map<Artist, Double> ranking = new HashMap<Artist, Double>();
        while (!results.isEmpty()){
            ranking.put((Artist) results.minId(), results.min());
            results.removeMin();
        }
        return ranking;
    }

    @Override
    public Map<Artist, Double> artist_similar_search(Artist artist) {
        return artist_similar_search(artist, 0);
    }

    public int countSubstring(String str, String findStr){
        int lastIndex = 0;
        int count =0;
        while(lastIndex != -1){
            lastIndex = str.indexOf(findStr,lastIndex);

            if( lastIndex != -1){
                count ++;
                lastIndex+=findStr.length();
            }
        }
        return count;
    }
    
    public String processContext(Song song, String column){
        String context = "";
        Map<String, Object> data = song.getData();
        if (column != null){
            context = song.get(column).toString();
        } else {
            for (String key: data.keySet()){
                if (key.equals("id")) continue;
                context += " " + data.get(key).toString();
            }
        }
        return context.toLowerCase();
    }
    
    public double getScore(Song song, String query, String column){
        String context = processContext(song, column);
        query = query.toLowerCase();
        Map<String, Object> data = song.getData();
        String[] terms = query.split(" ");
        String[] docTerms = context.split("");

        double score = 0;
        switch (rankingMethod){
            case Searcher.RANK_BASIC:
                for (String term: terms){
                    if (context.contains(term)){
                        score = 1;
                        break;
                    }
                }
                break;
            case Searcher.RANK_ZONE:
                Set<String> keys = data.keySet();
                for (String key: keys){
                    double impact = Song.getColumnImpact(key);
                    for (String term: terms){
                        if (data.get(key).toString().toLowerCase().contains(term)){
                            score += impact;
                            break;
                        }
                    }
                }
                break;
            case Searcher.RANK_JACCARD:
                int intersect = 0;
                for (String term: terms){
                    intersect += countSubstring(context, term);
                }
                score = intersect * 1.0 / (1 + terms.length + docTerms.length - intersect);
                break;
            case Searcher.RANK_2SHINGLE:
                context = " " + context + " ";
                int cnt = 0;
                for (int i=0; i<terms.length-1; i++){
                    cnt += countSubstring(context, " " + terms[i] + " " + terms[i+1] + " ");
                }
                double score1 = cnt * 1.0 / (3 + terms.length + docTerms.length - cnt - 2);

                int single = 0;
                for (String term: terms){
                    single += countSubstring(context, term);
                }
                double score2 = single * 1.0 / (1 + terms.length + docTerms.length - single);
                score = score1*0.9 + score2*0.1;
                break;
        }
        return score;
    }
}
