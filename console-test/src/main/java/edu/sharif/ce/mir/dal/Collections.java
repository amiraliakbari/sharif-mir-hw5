package edu.sharif.ce.mir.dal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/18/12, 18:31)
 */
public abstract class Collections {
    
    public static <E> List<E> list(E ... items) {
        final ArrayList<E> list = new ArrayList<E>();
        java.util.Collections.addAll(list, items);
        return list;
    }
    
}
