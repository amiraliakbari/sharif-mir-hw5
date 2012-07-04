package edu.sharif.ce.mir.clustering;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/3/12
 * Time: 8:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface VectorManager {
    public List<Vector> getAllMusics() throws SQLException;
}
