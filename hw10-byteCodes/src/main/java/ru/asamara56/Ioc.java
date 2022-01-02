package ru.asamara56;

import ru.asamara56.annotation.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class Ioc {

    private Ioc() {}

    static TestLogging createClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(Demo.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }


    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLogging testLogging;

        DemoInvocationHandler(TestLogging testLogging) {
            this.testLogging = testLogging;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            /*
                Аннотация @Log стоит над методами класса TestLoggingImpl и она не отображается через рефлексию,
                т.к. у нас в коде вызывается метод интерфейса.
                Не придумал как еще можно достать аннотации методов класса TestLoggingImpl.
                Сравниваю имена и типы входных аргуметов методов.
             */
            for (Method item : TestLoggingImpl.class.getDeclaredMethods()) {
                if (item.getName().equals(method.getName())
                        && Arrays.equals(item.getParameterTypes(), method.getParameterTypes()))
                {
                    for (Annotation annotation : item.getDeclaredAnnotations()) {
                        if (annotation instanceof Log) {
                            System.out.print("executed method: " + item.getName());
                            for (int n = 0; n < args.length; n++) {
                                System.out.print(", param" + (n + 1) + ":" + args[n]);
                            }
                            System.out.println();
                        }
                    }
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
