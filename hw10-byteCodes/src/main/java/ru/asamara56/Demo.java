package ru.asamara56;

public class Demo {

    public static void main(String[] args) {
        var object = Ioc.createClass();

        object.calculation(10);
        object.calculation(10, 20);
        object.calculation(10, 20, "thirty");
    }
}
