package edu.sharif.ce.mir.utils.entities;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 14:47)
 */
public class Pair<F, S> {

    private F first;
    private S second;

    public Pair() {
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }
    
    @Override
    public String toString() {
        return "[" + ((first == null) ? "null" : first.toString()) + ";" +
                    ((second == null) ? "null" : second.toString()) + "]";
    }

}
