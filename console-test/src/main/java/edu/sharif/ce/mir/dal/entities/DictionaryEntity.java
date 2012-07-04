package edu.sharif.ce.mir.dal.entities;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/4/12
 * Time: 6:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class DictionaryEntity {
    private Long id;
    private String word;

    public void setId(Long id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

}
