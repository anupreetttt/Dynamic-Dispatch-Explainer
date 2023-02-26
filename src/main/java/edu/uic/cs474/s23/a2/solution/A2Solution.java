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

//        try{
//            Class<?> c = o.getClass();
////            System.out.println(c.getSimpleName());
//            while (c != null) {
//                Field[] fields = c.getDeclaredFields();
//                for (Field field : fields) {
//                    field.setAccessible(true);
//                    String fieldName = field.getName();
//                    if (Modifier.isStatic(field.getModifiers())) {
//                        fieldName = c.getSimpleName() + "." + fieldName;
//                    }
//                    try {
//                        Object getObjVal = field.get(o);
//                        if (getObjVal != null) {
//                            ret.put(fieldName, getObjVal.toString());
//                        } else {
//                            ret.put(fieldName, "null");
//                        }
//                    } catch (IllegalArgumentException e) {
////                        throw new Error(e);
//                    }
//                }
//                c = c.getSuperclass();
//            }
//            return ret;
//            // handling exceptions and errors
//        } catch (Exception | Error e) {
//            System.out.println("Test 5 I: " + e);
////                    throw new RuntimeException("Error describing object", e);
//        }

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
        } catch (Exception | Error e) {
            System.out.println("Test 5 II " + e);
//                    throw new RuntimeException("Error describing object", e);
        }
        try {
            Class<?> c = o.getClass();
            System.out.println("Test class 6: " + c);// getting class of o
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
                        }
//                        else if (value instanceof Byte) {
//                            setValue = "0x" + String.format("%02x", (Byte) value);
//                        } else if (value instanceof Character) {
//                            setValue = "'" + value + "'";
//                        }
                        else {
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
                    Object obj = updateMap.get(f.getName());
                    if (obj == null) {
                        f.set(o, null);
                    } else if (f.getType().isAssignableFrom(obj.getClass())) {
                        f.set(o, obj);
                    } else {
                        // Trying to convert obj to the field's type
                        Object convertedValue = changeType(obj, f.getType());
                        f.set(o, convertedValue);
                    }
                } catch (IllegalAccessException e) {
                    // Move on to next field
                }
            }
            c = c.getSuperclass();
        }
    }

    private Object changeType(Object obj, Class<?> myClass) {
        if (obj == null) {
            return null;
        }
        if (myClass.isAssignableFrom(obj.getClass())) {
            return obj;
        }
        switch(myClass.getSimpleName()) {
            case "boolean":
            case "Boolean":
                return Boolean.parseBoolean(obj.toString());
            case "byte":
            case "Byte":
                return Byte.parseByte(obj.toString());
            case "char":
            case "Character":
                return obj.toString().charAt(0);
            case "double":
            case "Double":
                return Double.parseDouble(obj.toString());
            case "float":
            case "Float":
                return Float.parseFloat(obj.toString());
            case "int":
            case "Integer":
                return Integer.parseInt(obj.toString());
            default:
                return obj;
        }

    }
}
