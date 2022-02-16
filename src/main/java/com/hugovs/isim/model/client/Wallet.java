package com.hugovs.isim.model.client;

/**
 * User: hugo_<br/>
 * Date: 28/04/2018<br/>
 * Time: 22:51<br/>
 */
public class Wallet {

    private double money;
    private double initialMoney;

    public Wallet(double money) {
        this.initialMoney = this.money = money;
    }

    public double deposit(double money) {
        if (money < 0)
            throw new IllegalArgumentException("Money should be a positive value.");
        this.money += money;
        return money;
    }

    public double cashOut(double money) {

        if (money < 0)
            throw new IllegalArgumentException("Money should be a positive value.");
        if (money > this.money)
            throw new IllegalArgumentException("Insufficient money. Max value: " + this.money + ", parameter: " + money); //*

        this.money -= money;
        return money;
    }

    public double balance() {
        return money;
    }

    public double initialBalance() {
        return initialMoney;
    }

}
