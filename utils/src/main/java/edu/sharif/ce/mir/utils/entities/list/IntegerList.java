package edu.sharif.ce.mir.utils.entities.list;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 14:52)
 */
public class IntegerList extends TypedList<Integer> {

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public boolean addString(String o) {
        return add(Integer.parseInt(o));
    }

}
