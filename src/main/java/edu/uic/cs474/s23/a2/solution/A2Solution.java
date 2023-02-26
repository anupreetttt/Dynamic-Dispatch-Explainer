package edu.uic.cs474.s23.a2.solution;

import edu.uic.cs474.s23.a2.ObjectInspector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class A2Solution implements ObjectInspector {

    @Override
    public Map<String, String> describeObject(Object o) {
// key: field name, value: value of the field
        Map<String, String> ret = new HashMap<>();

        try{
            Class<?> c = (Class<?>) o;
//            System.out.println(c.getSimpleName());
            while (c != null) {
                Field[] fields = c.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (Modifier.isStatic(field.getModifiers())) {
                        fieldName = c.getSimpleName() + "." + fieldName;
                    }
                    try {
                        Object getObjVal = field.get(o);
                        if (getObjVal != null) {
                            ret.put(fieldName, getObjVal.toString());
                        } else {
                            ret.put(fieldName, "null");
                        }
                    } catch (IllegalArgumentException e) {
//                        throw new Error(e);
                    }
                }
                c = c.getSuperclass();
            }
            return ret;
        } catch (Exception e) {
            System.out.println("Test" + e);
//                    throw new RuntimeException("Error describing object", e);
        }
        try {
            Class<?> c = o.getClass();  // getting class of o
            while (c != null) {
                Field[] fs = c.getDeclaredFields(); // now we can get all the fields in c by declaring Field[] in String
                for (Field f : fs) {
                    if (!f.canAccess(o)) {
                        f.setAccessible(true);
                    }
                    try {
                        String key = f.getName();
                        Object value = f.get(o); // Returns the value of the field represented by this Field, on the specified object.
//                    System.out.println(value);
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
                        } else {
                            setValue = value.toString();
//                        System.out.println(setValue);
                        }
                        ret.put(key, setValue);
//                    System.out.println(ret.keySet());
//                    System.out.println(ret.entrySet());
                    } catch (ReflectiveOperationException e) {
                        throw new Error(e);
                    }
                }

                c = c.getSuperclass();
            }
        } catch (Exception | Error e)  {

        }
        Class<?> c1 = o.getClass();

        while(c1 != null) {
            Field[] fs = c1.getDeclaredFields();
            for (Field f : fs) {
                try {
                    f.setAccessible(true);
                    Object value = f.get(o);

                    if (value == null) {
                        ret.put(f.getName(), "");
                    } else {
                        String setValue = "";
                        try {
                            setValue = value.toString();
                        } catch (Throwable t) {
                            System.out.println(t);
                            if (t instanceof InvocationTargetException && t.getCause() != null) {
                                t = t.getCause();
                            }

                            if (t instanceof RuntimeException) {
                                setValue = "Thrown exception: " + t.getClass().getName();
                            } else if (t instanceof Error) {
                                setValue = "Raised error: " + t.getClass().getName();
                            } else {
                                setValue = "Unhandled throwable: " + t.getClass().getName();
                            }
                        }
                        ret.put(f.getName(), setValue);
                    }
                } catch (Exception e) {
//                    throw new AssertionError("Unexpected IllegalAccessException", e);
                }
                return ret;
            }
            c1 = c1.getSuperclass();
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
                        Object convertedValue = convertValue(newValue, f.getType());
                        f.set(o, convertedValue);
                    }
                } catch (IllegalAccessException e) {
                    // Do nothing and move on to the next field
                }
            }
            c = c.getSuperclass();
        }
    }

    private Object convertValue(Object value, Class<?> targetClass) {
        if (value == null) {
            return null;
        }
        if (targetClass.isAssignableFrom(value.getClass())) {
            return value;
        }
        if (targetClass == boolean.class || targetClass == Boolean.class) {
            return Boolean.parseBoolean(value.toString());
        } else if (targetClass == byte.class || targetClass == Byte.class) {
            return Byte.parseByte(value.toString());
        } else if (targetClass == char.class || targetClass == Character.class) {
            return value.toString().charAt(0);
        } else if (targetClass == double.class || targetClass == Double.class) {
            return Double.parseDouble(value.toString());
        } else if (targetClass == float.class || targetClass == Float.class) {
            return Float.parseFloat(value.toString());
        } else if (targetClass == int.class || targetClass == Integer.class) {
            return Integer.parseInt(value.toString());
        }
        return value;
    }
}
