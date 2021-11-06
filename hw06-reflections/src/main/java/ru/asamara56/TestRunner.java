package ru.asamara56;

import ru.asamara56.annotations.After;
import ru.asamara56.annotations.Before;
import ru.asamara56.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

public class TestRunner {

    public static void run(String clazzName) throws ClassNotFoundException {
        final Class<?> clazz = Class.forName(clazzName);
        final String simpleName = classNameSimplifier(clazzName);
        final Method[] methods = clazz.getDeclaredMethods();

        if (methods.length == 0) {
            System.out.println("No methods found in the class .");
        }
        List<String> beforeMethods = new ArrayList<>();
        List<String> testMethods = new ArrayList<>();
        List<String> afterMethods = new ArrayList<>();

        for(Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();

            for(Annotation annotation : annotations) {
                if (annotation instanceof Before) {
                    beforeMethods.add(method.getName());
                } else if (annotation instanceof Test) {
                    testMethods.add(method.getName());
                } else if (annotation instanceof After) {
                    afterMethods.add(method.getName());
                }
            }
        }

        if (testMethods.isEmpty()) {
            System.out.println("Methods to execute not found.");
        }
        final Map<String, Exception> resultMap = new LinkedHashMap<>();

        for (String testMethod : testMethods) {

            final Object object;
            try {
                object = ReflectionHelper.instantiate(clazz);
                System.out.println("\n" + simpleName + " instance for method " + testMethod + ": " + simpleName);
            } catch (RuntimeException var17) {
                System.out.println("\n" + simpleName + " instance creation failed.");
                throw new RuntimeException("Object creation failed.");
            }

            RuntimeException testException = null;
            try {
                for (String before : beforeMethods) {
                    ReflectionHelper.callMethod(object, before);
                }
            } catch (RuntimeException ex) {
                System.out.println("Method @Before execution error");
                testException = ex;
            }

            try {
                ReflectionHelper.callMethod(object, testMethod);
            } catch (RuntimeException ex) {
                System.out.println("Method \"" + testMethod + "\" execution error");
                testException = ex;
            }

            try {
                for (String after : afterMethods) {
                    ReflectionHelper.callMethod(object, after);
                }
            } catch (RuntimeException ex) {
                System.out.println("Method @After execution error");
                testException = ex;
            }
            resultMap.put(testMethod, testException);
        }

        System.out.println();
        for (Map.Entry<String, Exception> pair : resultMap.entrySet()) {
            Exception ex = pair.getValue();
            System.out.println(simpleName + " > " + pair.getKey() + "()" + (ex == null ? " PASSED" : " FAILED ---> " + ex));
        }

        long failed = resultMap.values().stream().filter(Objects::nonNull).count();
        System.out.println("\nTests passed: " + ((long)testMethods.size() - failed));
        System.out.println("Tests failed: " + failed);
        System.out.println("Total: " + testMethods.size());
    }

    private static String classNameSimplifier(String name) {
        String[] path = name.split("\\.");
        return path.length != 0 ? path[path.length - 1] : name;
    }
}
