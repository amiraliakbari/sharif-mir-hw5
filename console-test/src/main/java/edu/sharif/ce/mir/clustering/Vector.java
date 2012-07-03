package edu.sharif.ce.mir.clustering;

import edu.sharif.ce.mir.dal.entities.Song;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/3/12
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vector {

    private Long id;
    private HashMap<String, Integer> list;


    public Vector(Song song) {
        this.id = song.getId();
        list=new HashMap<String, Integer>();
    }
}
