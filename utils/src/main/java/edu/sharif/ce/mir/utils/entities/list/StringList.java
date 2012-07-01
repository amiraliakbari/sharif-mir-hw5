package edu.sharif.ce.mir.utils.entities.list;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 14:52)
 */
public class StringList extends TypedList<String> {

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public boolean addString(String o) {
        return add(o);
    }

}
