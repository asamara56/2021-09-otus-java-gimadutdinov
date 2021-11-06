package ru.asamara56.tests;

import ru.asamara56.annotations.After;
import ru.asamara56.annotations.Before;
import ru.asamara56.annotations.Test;

public class TestClass {
    public TestClass() {
    }

    @Before
    void setUp() {
        System.out.println("Work @Before method.");
    }

    @Test
    void test1() {
        System.out.println("Work test1 method.");
    }

    @Test
    void test2() {
        System.out.println("Work test2 method.");
        throw new RuntimeException("Exception into test method");
    }

    @Test
    void test3() {
        System.out.println("Work test3 method.");
    }

    @After
    void tearDown() {
        System.out.println("Work @After method.");
    }
}
