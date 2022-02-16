package com.hugovs.isim.model.client;

import com.hugovs.isim.model.exchange.Companies;

import java.util.HashMap;
import java.util.Map;

/**
 * User: hugo_<br/>
 * Date: 28/04/2018<br/>
 * Time: 22:53<br/>
 */
public class Client {

    private Wallet wallet;
    private Map<Companies, Integer> investments;

    public Client(float startMoney) {
        this.wallet = new Wallet(startMoney);
        this.investments = new HashMap<>();

        for (Companies companies : Companies.values())
            investments.put(companies, 0);

    }

    public Wallet getWallet() {
        return wallet;
    }

    public int check(Companies company) {
        return investments.get(company);
    }

    public double balance() {
        float profit = 0f;

        for (Companies company : Companies.values())
            profit += company.getPrice() * check(company);

        return profit + getWallet().balance();
    }

    public double profit() {
        return balance() - getWallet().balance();
    }

    public void buy(Companies company, int qnt) {

        if (qnt <= 0)
            throw new IllegalArgumentException("Qnt should be a positive value.");

        wallet.cashOut(company.getPrice() * qnt);
        if (investments.containsKey(company)) investments.put(company, investments.get(company) + qnt);
        else investments.put(company, qnt);

    }

    public void sell(Companies company, int qnt) {

        if (qnt <= 0)
            throw new IllegalArgumentException("Amount should be a positive value.");
        if (investments.get(company) < qnt)
            throw new IllegalArgumentException("Not enough stock exchanges.");

        wallet.deposit(qnt * company.getPrice());
        investments.put(company, investments.get(company) - qnt);

    }

}
