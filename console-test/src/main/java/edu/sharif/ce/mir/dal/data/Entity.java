package edu.sharif.ce.mir.dal.data;

import edu.sharif.ce.mir.dal.ColumnMetaData;
import edu.sharif.ce.mir.dal.DataSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/18/12, 19:13)
 */
public class Entity {

    private DataSource dataSource;
    protected Map<String, Object> map = new HashMap<String, Object>();

    /**
     * @param dataSource    the data source for which this entity is being created
     */
    public Entity(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * The data source of this entity
     * @return target data source
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Sets specified column's value
     * @param column name of the column
     * @param data the data to be injected
     * @return pointer to this entity to allow chaining
     */
    public Entity set(String column, Object data) {
        if (data == null) {
            throw new Error("You cannot set the value of a column to NULL. Nullification can be done by simply not setting the column");
        }
        final int index = dataSource.getColumns().indexOf(new ColumnMetaData(column, data.getClass()));
        if (index < 0) {
            throw new Error("No such column (" + column + ":" + data.getClass() + ") in " + dataSource.getTable());
        }
        final ColumnMetaData metaData = dataSource.getColumns().get(index);

        map.put(column, data);
        return this;
    }

    /**
     * Returns all the data in this entity
     * @return the data
     */
    public Map<String, Object> getData() {
        return map;
    }

    /**
     * Returns the data of the specified column
     * @param column    the name of the column
     * @return data in the column
     */
    public Object get(String column) {
        return map.get(column);
    }

    private static Method getMethod(Class<?> clazz, String name) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }

    /**
     * Converts an object into an instance of the entity
     * @param dataSource the data source
     * @param obj the object to be converted
     * @return equivalent entity
     */
    public static Entity fromObject(DataSource dataSource, Object obj) {
        final Entity entity = new Entity(dataSource);
        for (ColumnMetaData metaData : dataSource.getColumns()) {
            String name = metaData.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            name = "get" + name;
            final Method method = getMethod(obj.getClass(), name);
            Object value;
            try {
                value = method.invoke(obj);
            } catch (IllegalAccessException e) {
                throw new Error("Method <" + obj.getClass().getName() + "." + name + "> must be public");
            } catch (InvocationTargetException e) {
                throw new Error("Method <" + obj.getClass().getName() + "." + name + "> threw an error", e.getCause());
            }
            entity.set(metaData.getName(), value);
        }
        return entity;
    }

    /**
     * Returns an object instance equivalent to the entity
     * @param type the target type
     * @param <E> type parameter
     * @return the converted instance
     */
    public <E> E toObject(Class<E> type) {
        final E object;
        try {
            object = type.newInstance();
        } catch (InstantiationException e) {
            throw new Error("Could not instantiate: " + type.getName());
        } catch (IllegalAccessException e) {
            throw new Error("The default constructor in " + type.getName() + " must be public");
        }
        for (String property : map.keySet()) {
            String name = property;
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            name = "set" + name;
            final Method method = getMethod(type, name);
            try {
                method.invoke(object, map.get(property));
            } catch (IllegalAccessException e) {
                throw new Error("Method <" + type.getName() + "." + name + "> must be public");
            } catch (InvocationTargetException e) {
                throw new Error("Method <" + type.getName() + "." + name + "> threw an error", e.getCause());
            }
        }
        return object;
    }
}
