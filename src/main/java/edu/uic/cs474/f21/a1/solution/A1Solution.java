package edu.uic.cs474.f21.a1.solution;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import edu.uic.cs474.f21.a1.DynamicDispatchExplainer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class A1Solution implements DynamicDispatchExplainer {
    @Override
    public Set<String> explain(Map<String, ClassOrInterfaceDeclaration> classes, String receiverType, String methodName, String... argumentTypes) {
        Set<String> ret = new HashSet<>();

        ClassOrInterfaceDeclaration d = classes.get(receiverType);
        if(!d.getMethodsByName(methodName).isEmpty()){
            ret.add(d.getNameAsString());
        }
        return ret;
    }
}
