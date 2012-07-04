package edu.sharif.ce.mir.clustering;

import edu.sharif.ce.mir.dal.data.Entity;
import edu.sharif.ce.mir.dal.datasource.DictionaryDatasource;
import edu.sharif.ce.mir.dal.entities.DictionaryEntity;
import edu.sharif.ce.mir.dal.impl.MySqlDataStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/4/12
 * Time: 12:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class Dictionary {
    private MySqlDataStorage storage;
    private ArrayList<DictionaryEntity> allWord;

    public Dictionary(MySqlDataStorage storage) throws SQLException {
        this.storage = storage;
        try {
            storage.execute2(new DictionaryDatasource().createSql());
        } catch (SQLException e) {
        }
        List<Entity> entities = storage.selectAll(new DictionaryDatasource());
        allWord = new ArrayList<DictionaryEntity>();
        if (entities.size() > 0) {
            for (int i = 0; i < entities.size(); i++) {
                DictionaryEntity word = entities.get(i).toObject(DictionaryEntity.class);
                allWord.add(word);
            }
        }
    }

    public Long getDBId(String word) throws SQLException {
        word = word.toLowerCase();
        for (int i = 0; i < allWord.size(); i++) {
            if (allWord.get(i).getWord().toLowerCase().equals(word)) {
                return allWord.get(i).getId();
            }
        }
        /////////////////
        String sql=null;
        ResultSet rs=null;
        try {
            sql = DictionaryDatasource.selectWord(word);
            rs = storage.execute(sql);
            if (rs.next()) {
                DictionaryEntity en = new DictionaryEntity();
                en.setId(rs.getLong("id"));
                en.setWord(rs.getString("word"));
                allWord.add(en);
                return rs.getLong("id");
            }
        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }
        /////////////
        Entity entity = new Entity(new DictionaryDatasource());
        entity.set("word", word);
        System.out.println(word);
        storage.insert(entity);
//        String sql=DictionaryDatasource.selectWord(word);
        rs = storage.execute(sql);
        rs.next();
        DictionaryEntity en = new DictionaryEntity();
        en.setId(rs.getLong("id"));
        en.setWord(rs.getString("word"));
        allWord.add(en);
        return rs.getLong("id");

    }
}
