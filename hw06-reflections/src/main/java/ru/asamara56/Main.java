package ru.asamara56;

import ru.asamara56.tests.TestClass;

public class Main {

    public static void main(String[] args) {
        try {
            TestRunner.run(TestClass.class.getName());
        } catch (ClassNotFoundException ex) {
            System.out.println("Некорректное имя класса с тестами.");
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }

    }
}
