package edu.sharif.ce.mir.dal.entities;

import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Artist {
    private String name;
    private String years;
    private String genres;
    private MySqlDataStorage storage;

    public Artist(MySqlDataStorage storage, String name){
        this.name = name;
        this.storage = storage;
    }

    public Artist(String name, String years, String genres) {
        this.name = name;
        this.years = years;
        this.genres = genres;
    }

    public String getName() {
        return name;
    }

    public String getYears() {
        return years;
    }

    public String getGenres() {
        return genres;
    }

    public boolean load(){
        try {
            ResultSet rs = storage.execute(Artist.selectArtistQuery(name));
            if (!rs.next())
                return false;
            years = rs.getString("years");
            genres = rs.getString("genres");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void addYear(String year) throws SQLException {
        storage.execute2(Artist.addArtistYearQuery(name, year));
    }

    public void addGenre(String genre) throws SQLException {
        storage.execute2(Artist.addArtistGenreQuery(name, genre));
    }
    
    public static double commonItems(String a, String b){
        int common = 0;
        for (String s: a.split(",")){
            if (s.equals("")) continue;
            for (String t: b.split(",")){
                if (s.equals(t))
                    common++;
            }
        }
        return common / (1.0 * (Math.max(a.split(",").length + b.split(",").length - 4, 1)));
    }

    public double compareTo(Artist other){
        double score;

        double common_years = Artist.commonItems(years, other.getYears());
        double common_genres = Artist.commonItems(genres, other.getGenres());

        score = (common_genres) * 0.7 + (common_years) * 0.3;
        //System.out.println(common_genres+","+common_years+":"+score);
        return score;
    }


    public static String selectArtistQuery(String artist){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from `artists` where artist = '");
        sb.append(artist.replace("\'", "\\\'"));
        sb.append("'");
        return sb.toString();
    }

    public static String createArtistQuery(String artist, String years){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `artists` (`artist`, `years`) VALUES ('");
        sb.append(artist.replace("\'", "\\\'"));
        sb.append("', '");
        sb.append(years.replace("\'", "\\\'"));
        sb.append("')");
        return sb.toString();
    }

    public static String addArtistYearQuery(String artist, String year){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE `artists` SET `years`=CONCAT(years,'"+year+",') WHERE artist = '");
        sb.append(artist.replace("\'", "\\\'"));
        sb.append("' and years NOT LIKE '%,"+year+",%'");
        return sb.toString();
    }

    public static String addArtistGenreQuery(String artist, String genre){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE `artists` SET `genres`=CONCAT(genres,'"+genre+",') WHERE artist = '");
        sb.append(artist.replace("\'", "\\\'"));
        sb.append("' and genres NOT LIKE '%,"+genre+",%'");
        return sb.toString();
    }
}
