package ru.asamara56;

import ru.asamara56.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ioc {

    private Ioc() {}

    static TestLogging createClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(Demo.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }


    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLogging testLogging;
        private final List<Method> methods = new ArrayList<>();

        DemoInvocationHandler(TestLogging testLogging) {
            this.testLogging = testLogging;

            for (Method method : TestLoggingImpl.class.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Log.class)) {
                    methods.add(method);
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            for (Method item : methods) {
                Class<?>[] parameterTypes = item.getParameterTypes();
                Class<?>[] parameterTypes1 = method.getParameterTypes();
                if (item.getName().equals(method.getName())
                        && Arrays.equals(parameterTypes, parameterTypes1))
                {
                    System.out.print("executed method:" + item.getName());
                    for (int n = 0; n < args.length; n++) {
                        System.out.print(", param" + (n + 1) + "=" + args[n]);
                    }
                    System.out.println();
                }
            }
            return method.invoke(testLogging, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler {" +
                    "testLoggingClass = " + testLogging +
                    '}';
        }
    }
}
