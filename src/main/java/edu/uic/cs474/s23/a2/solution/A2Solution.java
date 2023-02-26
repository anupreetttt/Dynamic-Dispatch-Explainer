package edu.uic.cs474.s23.a2.solution;

import edu.uic.cs474.s23.a2.ObjectInspector;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class A2Solution implements ObjectInspector {

    @Override
    public Map<String, String> describeObject(Object o) {
        Map<String, String> ret = new HashMap<>();
        Class<?> c = o.getClass();
        while (c != null) {
            Field[] fs = c.getDeclaredFields();
            for (Field f : fs) {
                if (!f.canAccess(o)) {
                    f.setAccessible(true);
                }
                try {
                    String key = f.getName();
                    Object value = f.get(o);
                    String setValue;
                    if (value == null) {
                        setValue = "null";
                    } else if (value instanceof Long) {
                        setValue = "Boxed " + value + "#L";
                    } else if (value instanceof Float) {
                        setValue = "Boxed " + value + "#F";
                    } else if (value instanceof Double) {
                        setValue = value + "#D";
                    } else if (value instanceof Short) {
                        setValue = "0" + Integer.toOctalString((Short) value);
                    } else if (value instanceof Byte) {
                        setValue = "0x" + String.format("%02x", (Byte) value);
                    } else if (value instanceof Character) {
                        setValue = "'" + value + "'";
                    } else {
                        setValue = value.toString();
                    }
                    ret.put(key, setValue);
                } catch (IllegalAccessException e) {
                    // Do nothing and move on to the next field
                }
            }
            c = c.getSuperclass();
        }
        return ret;
    }

    @Override
    public void updateObject(Object o, Map<String, Object> updateMap) {
        Class<?> c = o.getClass();
        while (c != null) {
            Field[] fs = c.getDeclaredFields();
            for (Field f : fs) {
                if (!updateMap.containsKey(f.getName())) {
                    continue;
                }
                if (!f.canAccess(o)) {
                    f.setAccessible(true);
                }
                try {
                    Object newValue = updateMap.get(f.getName());
                    if (newValue == null) {
                        f.set(o, null);
                    } else if (f.getType().isAssignableFrom(newValue.getClass())) {
                        f.set(o, newValue);
                    } else {
                        // Attempt to convert newValue to the field's type
                        Object convertedValue = UpdatePrimitives(newValue, f.getType());
                        f.set(o, convertedValue);
                    }
                } catch (IllegalAccessException e) {
                    // Do nothing and move on to the next field
                }
//                for (Field f : fs) {
//                    try {
//                        f.setAccessible(true);
//                        Object value = f.get(o);
//
//                        if (value == null) {
//                            ret.put(f.getName(), "");
//                        } else {
//                            String setValue = "";
//                            try {
//                                setValue = value.toString();
//                            } catch (Throwable t) {
//                                if (t instanceof InvocationTargetException && t.getCause() != null) {
//                                    t = t.getCause();
//                                }
//
//                                if (t instanceof RuntimeException) {
//                                    setValue = "Thrown exception: " + t.getClass().getName();
//                                } else if (t instanceof Error) {
//                                    setValue = "Raised error: " + t.getClass().getName();
//                                } else {
//                                    setValue = "Unhandled throwable: " + t.getClass().getName();
//                                }
//                            }
//                            ret.put(f.getName(), setValue);
//                        }
//                    } catch (IllegalAccessException e) {
//                        throw new AssertionError("Unexpected IllegalAccessException", e);
//                    }
//                }
            }
            c = c.getSuperclass();
        }
    }

    private Object UpdatePrimitives(Object obj, Class<?> findClass) {
        if (obj == null) {
            return null;
        }
        if (findClass.isAssignableFrom(obj.getClass())) {
            return obj;
        }
        if (findClass == boolean.class || findClass == Boolean.class) {
            return Boolean.parseBoolean(obj.toString());
        } else if (findClass == byte.class || findClass == Byte.class) {
            return Byte.parseByte(obj.toString());
        } else if (findClass == char.class || findClass == Character.class) {
            return obj.toString().charAt(0);
        } else if (findClass == double.class || findClass == Double.class) {
            return Double.parseDouble(obj.toString());
        } else if (findClass == float.class || findClass == Float.class) {
            return Float.parseFloat(obj.toString());
        } else if (findClass == int.class || findClass == Integer.class) {
            return Integer.parseInt(obj.toString());
        }
        return obj;
    }
}