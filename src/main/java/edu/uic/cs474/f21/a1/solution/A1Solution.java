package edu.uic.cs474.f21.a1.solution;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import edu.uic.cs474.f21.a1.DynamicDispatchExplainer;

import java.util.*;


public class A1Solution implements DynamicDispatchExplainer {
    @Override
    public Set<String> explain(Map<String, ClassOrInterfaceDeclaration> classes, String receiverType, String methodName, String... argumentTypes) {
        Set<String> ret = new HashSet<>();

        ClassOrInterfaceDeclaration d = classes.get(receiverType);

        if(!d.getMethodsByName(methodName).isEmpty()){
            ret.add(d.getNameAsString());
            return ret;
        }

        while (!d.isEmpty()) {
            ClassOrInterfaceType a = d.getExtendedTypes().get(0);
            d = classes.get(a.getName().asString());
            if (!d.getMethodsByName(methodName).isEmpty()) {
                ret.add(d.getNameAsString());
                return ret;
            }

        }
        for(ClassOrInterfaceType classOrInterfaceType : d.getExtendedTypes()) {
            String name = classOrInterfaceType.getNameAsString();
            if(d.getMethodsByName(methodName).isEmpty()) {
                ret.addAll(explain(classes, name, methodName, argumentTypes));
            }
        }
//        ClassOrInterfaceType b = d.getExtendedTypes();
//        if (!d.getExtendedTypes().isEmpty()){
//            for (ClassOrInterfaceType classOrInterfaceType: a) {
//                explain(classes, d.getNameAsString(), methodName, argumentTypes);
//            }
//        }
        return ret;
    }
}
