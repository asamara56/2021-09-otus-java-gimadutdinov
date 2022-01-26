package ru.asamara56.atm;

import java.util.HashMap;
import java.util.Map;

import static ru.asamara56.atm.Banknote.*;

public class ATMImpl implements ATM {

    private final Map<Banknote, Integer> banknoteStore = new HashMap<>();

    private int balance = 0;

    public ATMImpl() {
        banknoteStore.putAll(Map.of(
                HUNDRED, 0,
                TWO_HUNDRED, 0,
                FIVE_HUNDRED, 0,
                THOUSAND, 0));
    }

    @Override
    public Map<Banknote, Integer> getMoney(int amount) throws IllegalArgumentException {
        if (balance < amount) {
            throw new IllegalArgumentException("Запрошенная сумма " + amount + " рублей не может быть выдана. Баланс " + balance + " рублей");
        }
        if (amount % 100 != 0) {
            throw new IllegalArgumentException("Запрошенная сумма " + amount + " рублей не может быть выдана. Введите сумму, кратную 100");
        }
        balance -= amount;
        return processGet(amount);
    }

    @Override
    public int addMoney(Map<Banknote, Integer> banknotes)  throws IllegalArgumentException {
        final int amount = processAdd(banknotes);
        balance += amount;
        return amount;
    }

    @Override
    public int getBalance() {
        return balance;
    }

    private Map<Banknote, Integer> processGet(int amount) {
        final var returnVal = new HashMap<Banknote, Integer>();

        for (Banknote item : Banknote.values()) {
            if (banknoteStore.get(item) > 0) {
                while (amount - item.getDenomination() >= 0) {
                    banknoteStore.merge(item, -1, Integer::sum);
                    returnVal.merge(item, 1, Integer::sum);
                    amount -= item.getDenomination();
                }
            }
        }
        return returnVal;
    }

    private int processAdd(Map<Banknote, Integer> banknotes) {
        final var banknotesCopy = new HashMap<>(banknotes);
        var returnVal = 0;

        for (Banknote item : Banknote.values()) {
            if (banknotesCopy.get(item) != null) {
                while (banknotesCopy.get(item) != 0) {
                    banknoteStore.merge(item, 1, Integer::sum);
                    banknotesCopy.merge(item, -1, Integer::sum);
                    returnVal += item.getDenomination();
                }
            }
        }
        return returnVal;
    }
}
