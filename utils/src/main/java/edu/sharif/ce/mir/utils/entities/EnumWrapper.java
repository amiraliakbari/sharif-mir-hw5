package edu.sharif.ce.mir.utils.entities;

import edu.sharif.ce.mir.utils.ArrayUtils;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/3/12, 14:24)
 */
public class EnumWrapper {

    private String[] types;
    private String value;

    public boolean matches(String item) {
        return ArrayUtils.contains(item, types);
    }
    
    public String get(String item) {
        final int index = ArrayUtils.indexOf(item, types);
        if (index == -1) {
            return null;
        }
        return types[index];
    }
    
    public <T extends Enum<T>> T get(String item, Class<T> type) {
        item = get(item);
        if (item != null) {
            item = item.toUpperCase();
        }
        return T.valueOf(type, item);
    }

    public static EnumWrapper getWrapper(String items) {
        final EnumWrapper wrapper = new EnumWrapper();
        wrapper.types = items.split("\\|");
        return wrapper;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public <T extends Enum<T>> T getValue(Class<T> type) {
        return get(getValue(), type);
    }

}
