package com.hugovs.isim.model;

import com.hugovs.isim.model.exchange.Companies;
import com.hugovs.isim.model.exchange.Database;
import com.hugovs.isim.model.exchange.Historic;
import com.hugovs.isim.model.exchange.Quotation;
import com.hugovs.isim.model.ia.SmartInvestor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * User: hugo_<br/>
 * Date: 29/04/2018<br/>
 * Time: 00:07<br/>
 */
public class Season {

    public SmartInvestor investor;
    public final Historic unknownQuotations = Database.ALL_2016;

    public Date today;
    public final Date initialDate;
    public final Date finalDate;

    public Season(SmartInvestor investor) {
        this.investor = investor;
        investor.season = this;

        try {
            today = DateUtils.dateFormat.parse("01/01/16");
            initialDate = DateUtils.dateFormat.parse("01/01/16");
            finalDate = DateUtils.dateFormat.parse("31/12/16");
        } catch (ParseException e) {
            throw new RuntimeException("Deu ruim na data.");
        }

        setUp();

    }

    public void setUp() {

        try {
            List<Quotation> todayQuotations = investor.getKnowledge().getFromDate(((new SimpleDateFormat("dd/MM/yy")).parse("30/12/2015")));
            updateCompanies(todayQuotations);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private double money = 100000d;
    public void oneMoreDay() {

        if (!investor.isPrepared) {
            investor.prepareToFight();
            investor.isPrepared = true;
        }

        List<Quotation> todayQuotations = getTodayQuotations();

        if (!todayQuotations.isEmpty()) {
            updateCompanies(todayQuotations);
            investor.learn(todayQuotations);
        }


        investor.letmeWorkPlease();
        incrementDate();

    }

    public void updateCompanies(List<Quotation> todayQuotations) {
        Companies.total = 0f;
        for (Quotation tq : todayQuotations) {
            tq.getCompany().update(tq);
            Companies.total += tq.getCompany().getPrice();
        }
    }

    private void incrementDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, 1);

        if (c.get(Calendar.MONTH) != today.getMonth()) {
            String key;
            switch (today.getMonth()) {
                case Calendar.JANUARY:      key = "Janeiro";     break;
                case Calendar.FEBRUARY:     key = "Fevereiro";   break;
                case Calendar.MARCH:        key = "Março";       break;
                case Calendar.APRIL:        key = "Abril";       break;
                case Calendar.MAY:          key = "Maio";        break;
                case Calendar.JUNE:         key = "Junho";       break;
                case Calendar.JULY:         key = "Julho";       break;
                case Calendar.AUGUST:       key = "Agosto";      break;
                case Calendar.SEPTEMBER:    key = "Setembro";    break;
                case Calendar.OCTOBER:      key = "Outubro";     break;
                case Calendar.NOVEMBER:     key = "Novembro";    break;
                case Calendar.DECEMBER:     key = "Dezembro";    break;
                default: key = "";
            }
            System.out.println("sart: " + money + "  \tfinal: " + investor.getPoorGuy().balance());
            investor.monthProfit.put(key, investor.getPoorGuy().balance() / money - 1);
            money = investor.getPoorGuy().balance();
        }

        today = c.getTime();
        today.setHours(0);
    }

    private List<Quotation> getTodayQuotations() {
        return unknownQuotations.getFromDate(today);
    }

    public void setInvestor(SmartInvestor investor) {
        this.investor = investor;
        investor.season = this;
    }

}
