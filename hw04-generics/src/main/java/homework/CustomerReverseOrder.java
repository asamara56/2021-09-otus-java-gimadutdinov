package homework;

import java.util.LinkedList;

public class CustomerReverseOrder {

    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    private final LinkedList<Customer> arrayList = new LinkedList<>();

    public void add(Customer customer) {
        arrayList.add(customer);
    }

    public Customer take() {
        return arrayList.pollLast(); // это "заглушка, чтобы скомилировать"
    }
}
