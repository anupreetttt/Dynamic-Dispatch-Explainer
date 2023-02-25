package edu.uic.cs474.s23.a2.solution;

import edu.uic.cs474.s23.a2.ObjectInspector;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class A2Solution implements ObjectInspector {

    @Override
    public Map<String, String> describeObject(Object o) {
// key: field name, value: value of the field
        Map<String, String> ret = new HashMap<>();

        Class<?> c = o.getClass();  // getting class of o
        while (c != null) {
            Field[] fs = c.getDeclaredFields(); // now we can get all the fields in c by declaring Field[] in String
            for (Field f : fs) {
                if (!f.isAccessible()) {
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
        return ret;
    }

    @Override
    public void updateObject(Object o, Map<String, Object> fields) {
        throw new Error("Not implemented");
    }
}
