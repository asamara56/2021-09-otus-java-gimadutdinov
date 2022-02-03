package ru.asamara56.atm.cell;

import ru.asamara56.atm.Banknote;

public interface Cell {

    /**
     * Добавить купюры в ячейку
     * @param number количество купюр
     * @return внесенная сумма
     */
    int addMoney(int number);

    /**
     * Взять купюры из ячейки
     * @return номинал взятой купюры
     */
    int getMoney();

    /**
     * @return номинал ячейки
     */
    int getDenominate();

    /**
     * @return тип банкноты
     */
    Banknote getBanknoteType();

    /**
     * @return количество купюр в ячейке
     */
    int getNumber();
}
