package edu.sharif.ce.mir.clustering;

import edu.sharif.ce.mir.clustering.stemming.Stemmer;
import edu.sharif.ce.mir.clustering.stemming.impl.EnglishStemmer;
import edu.sharif.ce.mir.dal.entities.Song;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/3/12
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vector {

    private Long id;
    private Map<Long, Integer> list;
    private static Stemmer stemmer = new EnglishStemmer();
    private static StopWords stopWords = new StopWords();


    private String getStemmed(String str) {
        return stemmer.stem(str);
    }

    private String removeStopWord(String lyric) {
        return null;
    }

    private Long getDBId(String name) {
        return new Long(1);
    }

    public Vector(Song song) {
        this.id = song.getId();
        list = new HashMap<Long, Integer>();
        StringTokenizer st = new StringTokenizer(song.getLyric());
//        ArrayList<String> words=new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String t = st.nextToken();
            if (!stopWords.isStopWord(t)) {
                t = stemmer.stem(t);
                Long id = getDBId(t);
                if (list.containsKey(id)) {
                    list.put(id, list.get(id) + 1);
                } else {
                    list.put(id, 1);
                }
            }
        }
    }

    public Long getId() {
        return id;
    }

    public Map<Long, Integer> getList() {
        return list;
    }
}
