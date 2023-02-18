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

        if(!d.getMethodsBySignature(methodName, argumentTypes).isEmpty() && !d.getMethodsBySignature(methodName, argumentTypes).get(0).isAbstract()){
            ret.add(d.getNameAsString());
//            return ret;
        }
//        if(d.getExtendedTypes() == null) {
//            return ret;
//        }

        while (!d.getExtendedTypes().isEmpty()) {
            ClassOrInterfaceType a = d.getExtendedTypes().get(0);
            d = classes.get(a.getName().asString());
            if (!d.getMethodsBySignature(methodName, argumentTypes).isEmpty() && !d.getMethodsBySignature(methodName, argumentTypes).get(0).isAbstract() && !d.getMethodsBySignature(methodName, argumentTypes).get(0).isPrivate() && !d.getMethodsBySignature(methodName, argumentTypes).get(0).isStatic()) {
                ret.add(d.getNameAsString());
                break;
            }

            if (d.getNameAsString().equals("Object")) {
                System.out.println(d.getNameAsString());
                ret.add("java.lang.Object");
                break;
            }
//            else {
//                d = classes.get("java.lang.Object");
//                System.out.println(d.getNameAsString());
//                ret.add("java.lang.Object");
//            }

            // Move up the class hierarchy
//            if (!d.getExtendedTypes().isEmpty()) {
//                ClassOrInterfaceType nextClass = d.getExtendedTypes().get(0);
//                d = classes.get(nextClass.getNameAsString());

        }
        return ret;
    }
}
