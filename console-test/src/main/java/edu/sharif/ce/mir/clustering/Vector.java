package edu.sharif.ce.mir.clustering;

import edu.sharif.ce.mir.clustering.stemming.Stemmer;
import edu.sharif.ce.mir.clustering.stemming.impl.EnglishStemmer;
import edu.sharif.ce.mir.dal.entities.Song;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/3/12
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vector {

    private Long id;
    private Map<Long, Double> list;
    private Integer clusterId;
    private static Stemmer stemmer = new EnglishStemmer();
    private static StopWords stopWords = new StopWords();
    private Dictionary dic;


    public Long getDBId(String name) throws SQLException {
        return dic.getDBId(name);
    }


    private String getStemmed(String str) {
        return stemmer.stem(str);
    }

    private String removeStopWord(String lyric) {
        return null;
    }



    public Vector(Dictionary dic,Song song) throws SQLException {
        this.id = song.getId();
        this.dic=dic;
        list = new HashMap<Long, Double>();
        StringTokenizer st = new StringTokenizer(song.getLyric()," \n/u\0160?,(){}.-_+=!@#'’:[]\\/*;");
        while (st.hasMoreTokens()) {
            String t = st.nextToken().trim();
            if (!stopWords.isStopWord(t)) {
                t = stemmer.stem(t);
//                System.out.println("vector: "+t);

                Long id = dic.getDBId(t);
                if (list.containsKey(id)) {
                    list.put(id, list.get(id) + 1);
                } else {
                    list.put(id, 1.0);
                }
            }
        }
    }

    public Vector(Long id, Map<Long, Double> list, Integer clusterId) {
        this.id = id;
        this.list = list;
        this.clusterId = clusterId;
    }

    public Long getId() {
        return id;
    }

    public Map<Long, Double> getList() {
        return list;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public double getMagnitude() {
        double sum = 0;
        for (long termId : list.keySet()) {
            double value = list.get(termId);
            sum += value * value;
        }
        return Math.sqrt(sum);
    }

    public double getDistance(Vector vec2) {
        Map<Long, Double> list2 = vec2.getList();
        Set<Long> mergedSet = new HashSet<Long>();
        mergedSet.addAll(list.keySet());
        mergedSet.addAll(list2.keySet());
        double sum = 0;
        for (long termId : mergedSet) {
            Double value1 = 0.0, value2 = 0.0;
            if (list.keySet().contains(termId)) {
                value1 = list.get(termId);
            }
            if (list2.keySet().contains(termId)) {
                value2 = list2.get(termId);
            }
            sum += Math.pow(value1 - value2, 2);
        }
        return Math.sqrt(sum);
    }


    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) {
            return false;
        }
        return id == ((Vector) obj).getId();
    }

    public static Vector calcSum(List<Vector> vectors) {
        Map<Long, Double> sumList = new HashMap<Long, Double>();
        for (Vector vector : vectors) {
            Map<Long, Double> vecList = vector.getList();
            for (long termId : vecList.keySet()) {
                if (sumList.keySet().contains(termId)) {
                    double sum = sumList.get(termId) + vecList.get(termId);
                    sumList.put(termId, sum);
                } else {
                    sumList.put(termId, vecList.get(termId));
                }
            }
        }
        return new Vector(0l, sumList, 0);
    }
    
    public static Vector calcAvg(List<Vector> vectors) {
        Vector sum = calcSum(vectors);
        Map<Long, Double> sumList = sum.getList();
        int size = vectors.size();
        for (long termId : sumList.keySet()) {
            sumList.put(termId, sumList.get(termId) / size);
        }
        return sum;
    }
}
