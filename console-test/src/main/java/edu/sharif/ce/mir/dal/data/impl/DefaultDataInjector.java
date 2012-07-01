package edu.sharif.ce.mir.dal.data.impl;

import edu.sharif.ce.mir.dal.data.DataInjector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/18/12, 18:59)
 */
public class DefaultDataInjector implements DataInjector {
    private DateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String[] getKeys(Map<String, Object> data) {
        List<String> keys = new ArrayList<String>();
        keys.addAll(data.keySet());
        final String[] strings = keys.toArray(new String[keys.size()]);
        Arrays.sort(strings, new Comparator<String>() {
            @Override
            public int compare(String first, String second) {
                return second.compareTo(first);
            }
        });
        return strings;
    }

    @Override
    public String inject(String sql, Map<String, Object> data) {
        Map<String, String> replaced = new HashMap<String, String>();
        final String[] keys = getKeys(data);
        for (String key : keys) {
            if (sql.contains("#" + key)) {
                replaced.put(key, null);
            }
        }
        for (String key : replaced.keySet()) {
            if (data.get(key) == null) {
                replaced.put(key, "null");
            } else {
                final Object value = data.get(key);
                if (value instanceof Integer || value instanceof Short|| value instanceof Float || value instanceof Double || value instanceof Long ||
                        value instanceof Boolean) {
                    replaced.put(key, value.toString());
                } else if (value instanceof String) {
                    String string = (String) value;
                    string = string.replaceAll("\"", "\\\"");
                    replaced.put(key, '"' + string + '"');
                } else if (value instanceof Character) {
                    replaced.put(key, "'" + Character.toString((Character) value) + "'");
                } else if (value instanceof Date) {
                    replaced.put(key,"'"+dateTimeFormatter.format(value)+"'");
                }
            }
        }
        for (String key : keys) {
            if (!replaced.containsKey(key)) {
                continue;
            }
            sql = sql.replace("#" + key, replaced.get(key));
        }
        return sql;
    }

}