package com.hugovs.isim.model.exchange;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: hugo_<br/>
 * Date: 18/04/2018<br/>
 * Time: 20:56<br/>
 */
public class Quotation {

    private Companies company;
    private Date date;
    private float price;
    private float minPrice;
    private float maxPrice;
    private float variation;
    private float variationPercent;
    private float volume;

    public Quotation(Companies company, Date date, float price, float minPrice, float maxPrice, float variation, float variationPercent, float volume) {
        this.company = company;
        this.date = date;
        this.price = price;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.variation = variation;
        this.variationPercent = variationPercent;
        this.volume = volume;
    }

    public Companies getCompany() {
        return company;
    }

    public void setCompany(Companies company) {
        this.company = company;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public float getVariation() {
        return variation;
    }

    public void setVariation(float variation) {
        this.variation = variation;
    }

    public float getVariationPercent() {
        return variationPercent;
    }

    public void setVariationPercent(float variationPercent) {
        this.variationPercent = variationPercent;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "<" +
                "company=" + getCompany().name() + ", \t" +
                "date=" + (new SimpleDateFormat("dd/MM/yy")).format(getDate()) + ", \t" +
                "price=" + getPrice() + ", \t" +
                "maximum=" + getMaxPrice() + ", \t" +
                "minimum=" + getMinPrice() + ", \t" +
                "variation=" + getVariation() + ",  \t" +
                "variationPercent=" + getVariationPercent() + ",   \t" +
                "volume=" + getVolume() + ">";
    }
}
