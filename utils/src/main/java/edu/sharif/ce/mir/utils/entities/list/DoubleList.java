package edu.sharif.ce.mir.utils.entities.list;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 14:53)
 */
public class DoubleList extends TypedList<Double> {

    @Override
    public Class<Double> getType() {
        return Double.class;
    }

    @Override
    public boolean addString(String o) {
        return add(Double.parseDouble(o));
    }

}
