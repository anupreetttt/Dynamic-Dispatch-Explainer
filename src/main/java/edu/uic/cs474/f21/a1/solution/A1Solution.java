package edu.uic.cs474.f21.a1.solution;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import edu.uic.cs474.f21.a1.DynamicDispatchExplainer;

import java.util.Map;
import java.util.Set;

public class A1Solution implements DynamicDispatchExplainer {
    @Override
    public Set<String> explain(Map<String, ClassOrInterfaceDeclaration> classes, String receiverType, String methodName, String... argumentTypes) {
        throw new Error("NOt implemented");
    }
}
