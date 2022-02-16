package com.hugovs.isim.model.ia.investors;

import com.hugovs.isim.model.client.Client;
import com.hugovs.isim.model.exchange.Companies;
import com.hugovs.isim.model.exchange.Database;
import com.hugovs.isim.model.exchange.Historic;
import com.hugovs.isim.model.exchange.Quotation;
import com.hugovs.isim.model.ia.SmartInvestor;

import javax.xml.crypto.Data;
import java.util.*;

/**
 * User: hugo_<br/>
 * Date: 09/05/2018<br/>
 * Time: 18:57<br/>
 */
public class OracleInvestor extends SmartInvestor {

    public OracleInvestor(Client poorGuy, Historic knowledge) {
        super(poorGuy, knowledge);
    }

    @Override
    public void prepareToFight() {

    }

    @Override
    public void letmeWorkPlease() {

        Date tomorrow = incrementDate(season.today);

        Historic future = Database.ALL_2016;
        List<Quotation> quotations = future.getFromDate(tomorrow);
        tomorrow.setHours(0);

        if (!quotations.isEmpty()) {

            TreeSet<Quotation> sortedQuotations = new TreeSet<>((o1, o2) -> Double.compare(o2.getVariationPercent(), o1.getVariationPercent()));
            sortedQuotations.addAll(quotations);

            for (Quotation quotation : sortedQuotations)
                roundSell(quotation.getCompany(), Integer.MAX_VALUE);
            for (Quotation quotation : sortedQuotations)
                roundBuy(quotation.getCompany(), Integer.MAX_VALUE);

        }

    }

    private Date incrementDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

}
