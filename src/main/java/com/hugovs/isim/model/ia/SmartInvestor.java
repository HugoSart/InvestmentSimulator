package com.hugovs.isim.model.ia;

import com.hugovs.isim.model.Season;
import com.hugovs.isim.model.exchange.Companies;
import com.hugovs.isim.model.exchange.Historic;
import com.hugovs.isim.model.exchange.Quotation;
import com.hugovs.isim.model.client.Client;

import java.util.*;

/**
 * User: hugo_<br/>
 * Date: 29/04/2018<br/>
 * Time: 00:10<br/>
 */
public abstract class SmartInvestor {

    public enum Action { BUY, SELL }

    public class Transaction {
        public final Date date;
        public final Companies company;
        public final Action action;
        public final int quantity;
        public final float value;
        public Transaction(Date date, Companies company, Action action, int quantity) {this.date = date; this.action = action; this.company = company; this.quantity = quantity; value = company.getPrice() * quantity; }
    }

    private List<Transaction> transactions = new ArrayList<>();
    public Map<String, Double> monthProfit = new LinkedHashMap<>();

    public Season season = null;
    private Client poorGuy;
    private Historic knowledge;
    public boolean isPrepared = false;

    public SmartInvestor(Client poorGuy, Historic knowledge) {
        this.poorGuy = poorGuy;
        this.knowledge = knowledge;

        monthProfit.put("Janeiro",  0d);
        monthProfit.put("Fevereiro",0d);
        monthProfit.put("Março",    0d);
        monthProfit.put("Abril",    0d);
        monthProfit.put("Maio",     0d);
        monthProfit.put("Junho",    0d);
        monthProfit.put("Julho",    0d);
        monthProfit.put("Agosto",   0d);
        monthProfit.put("Setembro", 0d);
        monthProfit.put("Outubro",  0d);
        monthProfit.put("Novembro", 0d);
        monthProfit.put("Dezembro", 0d);

    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void learn(List<Quotation> quotations) {
        knowledge.getQuotations().addAll(quotations);
    }

    public void learn(Quotation quotation) {
        knowledge.getQuotations().add(quotation);
    }

    public Client getPoorGuy() {
        return poorGuy;
    }

    public void setPoorGuy(Client poorGuy) {
        this.poorGuy = poorGuy;
    }

    public Historic getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(Historic knowledge) {
        this.knowledge = knowledge;
    }

    public abstract void prepareToFight();

    public abstract void letmeWorkPlease();

    public List<Transaction> getTransactionsFrom(Companies company) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if(transaction.company == company) result.add(transaction);
        }

        return result;
    }

    public List<Transaction> getBuyTransactions() {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions)
            if (transaction.action == Action.BUY) result.add(transaction);
        return result;
    }

    public List<Transaction> getSellTransactions() {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions)
            if (transaction.action == Action.SELL) result.add(transaction);
        return result;
    }

    public void buy(Companies company, int qnt) {
        transactions.add(new Transaction(season.today,  company, Action.BUY, qnt));
        poorGuy.buy(company, qnt);
    }

    public void sell(Companies company, int qnt) {
        transactions.add(new Transaction(season.today,  company, Action.SELL, qnt));
        poorGuy.sell(company, qnt);
    }

    public void roundBuy(Companies company, int qnt) {
        if (company.getPrice() * qnt > poorGuy.getWallet().balance())
            qnt = (int)(poorGuy.getWallet().balance() / company.getPrice());
        if (qnt > 0) {
            try {
                buy(company, qnt);
            } catch (IllegalArgumentException e) {
                buy(company, qnt - 1);
            }
        }
    }

    public void roundSell(Companies company, int qnt) {
        if (check(company) < qnt)
            qnt = check(company);
        if (qnt > 0)
            sell(company, qnt);
    }

    public int check(Companies company) {
        return poorGuy.check(company);
    }

}
