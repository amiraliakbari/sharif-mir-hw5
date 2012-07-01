package edu.sharif.ce.mir.dal.data;

import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/18/12, 18:58)
 */
public interface DataInjector {

    /**
     * Injects data into statement
     * @param sql statement
     * @param data data
     * @return output
     */
    String inject(String sql, Map<String, Object> data);

}
