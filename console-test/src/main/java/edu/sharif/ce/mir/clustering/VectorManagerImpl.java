package edu.sharif.ce.mir.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 7/3/12
 * Time: 8:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class VectorManagerImpl implements VectorManager {
    public List<Vector> getAllMusics() {
        List<Vector> list = new ArrayList<Vector>();
        HashMap<Long, Double> map = new HashMap<Long, Double>();
        map.put(1l, 10.0);
        map.put(3l, 5.0);
        list.add(new Vector(1l, map, 0));
        map = new HashMap<Long, Double>();
        map.put(2l, 4.0);
        map.put(3l, 3.0);
        list.add(new Vector(2l, map, 0));
        map = new HashMap<Long, Double>();
        map.put(1l, 4.0);
        map.put(5l, 1.0);
        list.add(new Vector(3l, map, 0));
        map = new HashMap<Long, Double>();
        map.put(2l, 3.0);
        map.put(5l, 2.0);
        map.put(3l, 1.0);
        list.add(new Vector(4l, map, 0));
        return list;
    }
}
