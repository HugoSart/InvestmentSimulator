package com.hugovs.isim.model.exchange;

import com.opencsv.CSVReader;
import org.apache.commons.lang3.ArrayUtils;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: hugo_<br/>
 * Date: 18/04/2018<br/>
 * Time: 20:55<br/>
 */
public class Historic {

    private String name;
    private List<Quotation> quotations;

    public Historic(String name, List<Quotation> quotations) {
        this.name = name;
        this.quotations = quotations;
    }

    public static Historic fromCSV(String historicName, String path) {

        List<Quotation> quotations = new ArrayList<>();

        try {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");

            CSVReader reader = new CSVReader(new FileReader(path), '\t');
            String[] line, header = reader.readNext();
            while ((line = reader.readNext()) != null) {
                String name = line[0];
                Date date = formatter.parse(line[1]);
                float quot = Float.valueOf(line[2].replace(",", "."));
                float max = Float.valueOf(line[3].replace(",", "."));
                float min = Float.valueOf(line[4].replace(",", "."));
                float var = Float.valueOf(line[5].replace(",", "."));
                float varPer = Float.valueOf(line[6].replace(",", "."));
                float vol = Float.valueOf(line[7].replace(".", ""));
                Quotation quotation = new Quotation(Companies.valueOf(name), date, quot, max, min, var, varPer, vol);
                quotations.add(quotation);
            }

            Collections.reverse(quotations);
            return new Historic(historicName, quotations);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }

    public Historic merge(Historic h) {
        Historic ret = new Historic(name, new ArrayList<>(quotations));
        ret.getQuotations().addAll(h.quotations);
        return ret;
    }

    public Historic sort() {
        quotations.sort(Comparator.comparing(Quotation::getDate));
        return this;
    }

    public List<Quotation> getFromDate(Date date) {
        List<Quotation> result = new ArrayList<>();
        for (Quotation q : quotations)
            if (q.getDate().equals(date)) result.add(q);
        return result;
    }

    public List<Quotation> getFromCompany(Companies company) {
        List<Quotation> result = new ArrayList<>();
        for (Quotation q : quotations)
            if (q.getCompany() == company)
                result.add(q);
        return result;
    }

    public List<Quotation> getFromInterval(Date start, Date end) {
        List<Quotation> result = new ArrayList<>();
        for (Quotation q : quotations) {
            if (q.getDate().after(start)) {
                if(q.getDate().after(end)) break;
                result.add(q);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append("<name=").append(name).append(",\n");
        for (Quotation quotation : quotations)
            sb.append(quotation.toString()).append("\n");
        System.out.println(">\n");
        return sb.toString();
    }

}
