package edu.uic.cs474.s23.a2.solution;

import edu.uic.cs474.s23.a2.ObjectInspector;

import java.util.Map;

public class A2Solution implements ObjectInspector {

    @Override
    public Map<String, String> describeObject(Object o) {
        return null;
    }

    @Override
    public void updateObject(Object o, Map<String, Object> fields) {
        throw new Error("NOt implemented");
    }
}
