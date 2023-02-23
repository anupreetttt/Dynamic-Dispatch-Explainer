package edu.uic.cs474.s23.a2.solution;

import edu.uic.cs474.s23.a2.ObjectInspector;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class A2Solution implements ObjectInspector {

    @Override
    public Map<String, String> describeObject(Object o) {

        Map<String, String> ret = new HashMap<>();

        Class<?> c = o.getClass();  // getting class of o
        while (c != null) {
            Field[] fs = c.getDeclaredFields(); // now we can get all the fields in c by declaring Field[] in String
            for (Field f : fs) {
                try {
                    String key = f.getName();
                    Object value = f.get(o);
                    String setValue = (String) value;
                    ret.put(key, setValue);
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
