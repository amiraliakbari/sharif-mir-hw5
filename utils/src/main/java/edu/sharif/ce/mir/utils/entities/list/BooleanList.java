package edu.sharif.ce.mir.utils.entities.list;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 14:54)
 */
public class BooleanList extends TypedList<Boolean> {

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public boolean addString(String o) {
        return add(Boolean.parseBoolean(o));
    }

}
