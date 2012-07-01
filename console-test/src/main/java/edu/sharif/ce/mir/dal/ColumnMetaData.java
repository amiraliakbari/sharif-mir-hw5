package edu.sharif.ce.mir.dal;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/18/12, 18:28)
 */
public class ColumnMetaData {

    public static final boolean DEFAULT_NULLABLE_VALUE = true;
    private final String name;
    private final Class<?> type;
    private final boolean nullable;

    public ColumnMetaData(String name, Class<?> type) {
        this(name, type, DEFAULT_NULLABLE_VALUE);
    }

    public ColumnMetaData(String name, Class<?> type, boolean nullable) {
        this.nullable = nullable;
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isNullable() {
        return nullable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnMetaData that = (ColumnMetaData) o;

        return !(name != null ? !name.equals(that.name) : that.name != null) && !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
