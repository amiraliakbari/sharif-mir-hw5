package edu.sharif.ce.mir.utils;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 23:21)
 */
public abstract class ArrayUtils {
    
    public static String toString(Object[] items, String glue) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            Object item = items[i];
            if (i > 0) {
                builder.append(glue);
            }
            builder.append(item);
        }
        return builder.toString();
    }

    public static <T> boolean contains(T item, T[] array) {
        if (item == null) {
            return false;
        }
        for (T t : array) {
            if (item.equals(t)) {
                return true;
            }
        }
        return false;
    }

    public static <T> int indexOf(T item, T[] array) {
        if (item == null) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            T t = array[i];
            if (item.equals(t)) {
                return i;
            }
        }
        return -1;
    }
}
