package edu.sharif.ce.mir.console.command.definition.parse;

import edu.sharif.ce.mir.utils.ArrayUtils;
import edu.sharif.ce.mir.utils.entities.EnumWrapper;
import edu.sharif.ce.mir.utils.entities.list.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 18:59)
 */
public class ParameterCommandParseTree extends AbstractCommandParseTree {

    private Class<?> type;
    private String typeArguments = null;
    private final String name;
    private Object value;

    public ParameterCommandParseTree(String name, String type) {
        this.name = name;
        this.typeArguments = null;
        if ("integer".equals(type)) {
            this.type = Integer.class;
        } else if ("real".equals(type)) {
            this.type = Double.class;
        } else if ("boolean".equals(type)) {
            this.type = Boolean.class;
        } else if ("string".equals(type)) {
            this.type = String.class;
        } else if ("list:integer".equals(type)) {
            this.type = IntegerList.class;
        } else if ("list:real".equals(type)) {
            this.type = DoubleList.class;
        } else if ("list:boolean".equals(type)) {
            this.type = BooleanList.class;
        } else if ("list:string".equals(type)) {
            this.type = StringList.class;
        } else if (type.matches("enum\\[[^\\|\\[\\]]+(\\|[^\\|\\[\\]]+)*\\]")) {
            this.type = EnumWrapper.class;
            this.typeArguments = type.substring("enum[".length(), type.length() - 1);
        } else {
            throw new IllegalArgumentException("Invalid parameter type specified: " + type);
        }
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        if (!accepts(value)) {
            throw new IllegalArgumentException(this + " cannot accept argument " + value);
        }
        if (Integer.class.equals(type)) {
            this.value = Integer.parseInt(value);
        } else if (Double.class.equals(type)) {
            this.value = Double.parseDouble(value);
        } else if (Boolean.class.equals(type)) {
            this.value = ArrayUtils.contains(value, new String[]{"y", "yes", "true"});
        } else if (EnumWrapper.class.equals(type)) {
            final EnumWrapper wrapper = EnumWrapper.getWrapper(typeArguments);
            wrapper.setValue(value);
            this.value = wrapper;
        } else if (TypedList.class.isAssignableFrom(type)) {
            TypedList typeObject = null;
            try {
                typeObject = (TypedList) type.newInstance();
            } catch (Exception ignored) {
            }
            if (!value.matches("\\{.*?\\}")) {
                typeObject.addString(value);
                this.value = typeObject;
                return;
            }
            value = value.substring(1, value.length() - 1);
            String token = "";
            while (!value.isEmpty()) {
                int pos = 0;
                Character quote = null;
                if ("\"'`".contains(Character.toString(value.charAt(0)))) {
                    quote = value.charAt(0);
                    pos ++;
                }
                while (pos < value.length()) {
                    if (value.substring(pos).startsWith("\\\\") ||
                            value.substring(pos).startsWith("\\\"") ||
                            value.substring(pos).startsWith("\\'") ||
                            value.substring(pos).startsWith("\\`")
                            ) {
                        token += value.charAt(pos + 1);
                        pos += 2;
                        continue;
                    }
                    if (value.substring(pos).startsWith("\\n")) {
                        token += "\n";
                        pos += 2;
                        continue;
                    }
                    if (value.substring(pos).startsWith("\\t")) {
                        token += "\t";
                        pos += 2;
                        continue;
                    }
                    if (value.substring(pos).startsWith("\\r")) {
                        token += "\r";
                        pos += 2;
                        continue;
                    }
                    token += value.charAt(pos);
                    if (quote == null && token.endsWith(",")) {
                        token = token.substring(0, token.length() - 1);
                        break;
                    }
                    if (quote != null && !token.isEmpty() && token.charAt(token.length() - 1) == quote) {
                        break;
                    }
                    pos ++;
                }
                if (quote != null) {
                    token = token.substring(0, token.length() - 1);
                }
                value = value.substring(token.length());
                typeObject.addString(token);
                if (!value.startsWith(",")) {
                    token = value;
                    break;
                } else {
                    token = "";
                }
                value = value.substring(1);
            }
            this.value = typeObject;
        } else {
            this.value = value;
        }
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }
    
    public static boolean accepts(String value, Class<?> type, String typeArguments) {
        try {
            if (Integer.class.equals(type)) {
                Integer.parseInt(value);
            } else if (Double.class.equals(type)) {
                Double.parseDouble(value);
            } else if (EnumWrapper.class.equals(type)) {
                final EnumWrapper wrapper = EnumWrapper.getWrapper(typeArguments);
                return wrapper.matches(value);
            } else if (Boolean.class.equals(type)) {
                return ArrayUtils.contains(value.toLowerCase(), new String[]{"y", "n", "yes", "no", "true", "false"});
            } else if (TypedList.class.isAssignableFrom(type)) {
                final TypedList typeObject = (TypedList) type.newInstance();
                if (!value.matches("\\{.*?\\}")) {
                    return accepts(value, typeObject.getType(), typeArguments);
                }
                value = value.substring(1, value.length() - 1);
                String token = "";
                while (!value.isEmpty()) {
                    int pos = 0;
                    Character quote = null;
                    if ("\"'`".contains(Character.toString(value.charAt(0)))) {
                        quote = value.charAt(0);
                        pos ++;
                    }
                    while (pos < value.length()) {
                        if (value.substring(pos).startsWith("\\\\") ||
                                value.substring(pos).startsWith("\\\"") ||
                                value.substring(pos).startsWith("\\'") ||
                                value.substring(pos).startsWith("\\`")
                                ) {
                            token += value.charAt(pos + 1);
                            pos += 2;
                            continue;
                        }
                        if (value.substring(pos).startsWith("\\n")) {
                            token += "\n";
                            pos += 2;
                            continue;
                        }
                        if (value.substring(pos).startsWith("\\t")) {
                            token += "\t";
                            pos += 2;
                            continue;
                        }
                        if (value.substring(pos).startsWith("\\r")) {
                            token += "\r";
                            pos += 2;
                            continue;
                        }
                        token += value.charAt(pos);
                        if (quote == null && token.endsWith(",")) {
                            token = token.substring(0, token.length() - 1);
                            break;
                        }
                        if (quote != null && !token.isEmpty() && token.charAt(token.length() - 1) == quote) {
                            break;
                        }
                        pos ++;
                    }
                    if (quote != null) {
                        token = token.substring(0, token.length() - 1);
                    }
                    value = value.substring(token.length());
                    if (!accepts(token, typeObject.getType(), typeArguments)) {
                        System.out.println(token);
                        return false;
                    }
                    if (!value.startsWith(",")) {
                        token = value;
                        break;
                    } else {
                        token = "";
                    }
                    value = value.substring(1);
                }
            }
        } catch (NumberFormatException e) {
            return false;
        } catch (InstantiationException e) {
            return false;
        } catch (IllegalAccessException e) {
            return false;
        }
        return true;
    }

    public boolean accepts(String value) {
        return accepts(value, getType(), getTypeArguments());
    }

    @Override
    public String toString() {
        return "#" + getName() + ":" + getType().getSimpleName();
    }

    public String getTypeArguments() {
        return typeArguments;
    }
}
