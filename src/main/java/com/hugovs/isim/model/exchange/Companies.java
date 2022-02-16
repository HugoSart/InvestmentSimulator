package com.hugovs.isim.model.exchange;

/**
 * User: hugo_<br/>
 * Date: 28/04/2018<br/>
 * Time: 20:57<br/>
 */
public enum Companies {

    BBAS3, BBDC4, CIEL3, ITUB4, JBSS3, NATU3, PETR4, SANB4, UGPA3, VALE3;

    private Historic historic;
    private float price;
    private float maximum;
    private float minimum;
    private float variation;
    private float variationPercent;
    private float volume;
    private float variationPercentAcum = 0f;
    public static float total = 0f;

    public void update(Quotation quotation) {
        price = quotation.getPrice();
        maximum = quotation.getMaxPrice();
        minimum = quotation.getMinPrice();
        variation = quotation.getVariation();
        variationPercent = quotation.getVariationPercent();
        volume = quotation.getVolume();
        variationPercentAcum += variationPercent;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getMaximum() {
        return maximum;
    }

    public void setMaximum(float maximum) {
        this.maximum = maximum;
    }

    public float getMinimum() {
        return minimum;
    }

    public void setMinimum(float minimum) {
        this.minimum = minimum;
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

    public float getVariationPercentAcum() {
        return variationPercentAcum;
    }

    public void setVariationPercentAcum(float variationPercentAcum) {
        this.variationPercentAcum = variationPercentAcum;
    }
}
