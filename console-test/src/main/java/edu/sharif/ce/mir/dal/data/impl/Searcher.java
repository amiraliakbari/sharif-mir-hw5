package edu.sharif.ce.mir.dal.data.impl;


import edu.sharif.ce.mir.dal.entities.Song;

import java.util.Map;

public interface Searcher {
    public static int RANK_BASIC = 1;
    public static int RANK_ZONE = 2;
    public static int RANK_JACCARD = 3;
    public static int RANK_2SHINGLE = 4;

    public void setRankingMethod(int rankingMethod);

    /**
     *
     * @param query The search query
     * @param column Target Column, null to search all columns
     * @param limit needed results count, defaults to 1000
     */
    public Map<Song, Double> search(String query, String column, int limit);

    /**
     *
     * @param query The search query
     * @param column Target Column, null to search all columns
     */
    public Map<Song, Double> search(String query, String column);

    /**
     *
     * @param query The search query
     */
    public Map<Song, Double> search(String query);
}
