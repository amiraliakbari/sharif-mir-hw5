package edu.sharif.ce.mir.dal.entities;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/4/12
 * Time: 9:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class Cluster {
    private Long id;
    private Integer group;

    public Long getId() {
        return id;
    }

    public Integer getGroup() {
        return group;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }
}
