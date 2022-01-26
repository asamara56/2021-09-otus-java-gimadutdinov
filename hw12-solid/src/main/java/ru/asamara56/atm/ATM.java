package ru.asamara56.atm;

import java.util.Map;

public interface ATM {

    /**
     * Выдача купюр
     * @param amount запрашиваемая сумма
     * @return банкноты разного номиналаа
     * @throws IllegalArgumentException если запрошенная сумма не может быть выдана
     */
    Map<Banknote, Integer> getMoney(int amount) throws IllegalArgumentException;

    /**
     * Внесение денег
     * @param money принятая банкоматом сумма
     * @throws IllegalArgumentException если внесенная сумма некорректна (не кратна минимальной банкноте)
     * @return принятая сумма
     */
    int addMoney(Map<Banknote, Integer> money) throws IllegalArgumentException;

    /**
     * Запрос баланса
     * @return остаток средств в банкомате
     */
    int getBalance();
}
