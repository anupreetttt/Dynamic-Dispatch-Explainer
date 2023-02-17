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
            return ret;
        }
        if(d.getExtendedTypes() == null) {
            return ret;
        }

        while (!d.getExtendedTypes().isEmpty()) {
            ClassOrInterfaceType a = d.getExtendedTypes().get(0);
            d = classes.get(a.getName().asString());
            if (!d.getMethodsBySignature(methodName, argumentTypes).isEmpty() && !d.getMethodsBySignature(methodName, argumentTypes).get(0).isAbstract() && !d.getMethodsBySignature(methodName, argumentTypes).get(0).isPrivate() && !d.getMethodsBySignature(methodName, argumentTypes).get(0).isStatic()) {
                ret.add(d.getNameAsString());
                return ret;
            }

//            for (MethodDeclaration method : d.getMethodsBySignature(methodName, argumentTypes)) {
//                if (method.isAbstract()) {
//                    // add the class to the set if the method is abstract
//                    ret.add(d.getNameAsString());
//                    return ret;
//                }
//                if (method.isPrivate() || method.isStatic()) {
//                    // add the class to the set if the method is private or static
//                    ret.add(d.getNameAsString());
//                    return ret;
//                }
//            }
        }

        return ret;
    }
}
