package com.hugovs.isim.model.exchange;

import java.util.ArrayList;

/**
 * User: hugo_<br/>
 * Date: 28/04/2018<br/>
 * Time: 20:38<br/>
 */
public class Database {

    public static final Historic BBAS3_2016         = Historic.fromCSV( "BBAS3_2016",       "data/current/BBAS3_2016.tsv");
    public static final Historic BBDC4_2016         = Historic.fromCSV( "BBDC4_2016",       "data/current/BBDC4_2016.tsv");
    public static final Historic CIEL3_2016         = Historic.fromCSV( "CIEL3_2016",       "data/current/CIEL3_2016.tsv");
    public static final Historic ITUB4_2016         = Historic.fromCSV( "ITUB4_2016",       "data/current/ITUB4_2016.tsv");
    public static final Historic JBSS3_2016         = Historic.fromCSV( "JBSS3_2016",       "data/current/JBSS3_2016.tsv");
    public static final Historic NATU3_2016         = Historic.fromCSV( "NATU3_2016",       "data/current/NATU3_2016.tsv");
    public static final Historic PETR4_2016         = Historic.fromCSV( "PETR4_2016",       "data/current/PETR4_2016.tsv");
    public static final Historic SANB4_2016         = Historic.fromCSV( "SANB4_2016",       "data/current/SANB4_2016.tsv");
    public static final Historic UGPA3_2016         = Historic.fromCSV( "UGPA3_2016",       "data/current/UGPA3_2016.tsv");
    public static final Historic VALE3_2016         = Historic.fromCSV( "VALE3_2016",       "data/current/VALE3_2016.tsv");
    public static final Historic BBAS3_2014_2015    = Historic.fromCSV( "BBAS3_2014-2015",  "data/histories/BBAS3_2014-2015.tsv");
    public static final Historic BBDC4_2014_2015    = Historic.fromCSV( "BBDC4_2014-2015",  "data/histories/BBDC4_2014-2015.tsv");
    public static final Historic CIEL3_2014_2015    = Historic.fromCSV( "CIEL3_2014-2015",  "data/histories/CIEL3_2014-2015.tsv");
    public static final Historic ITUB4_2014_2015    = Historic.fromCSV( "ITUB4_2014-2015",  "data/histories/ITUB4_2014-2015.tsv");
    public static final Historic JBSS3_2014_2015    = Historic.fromCSV( "JBSS3_2014-2015",  "data/histories/JBSS3_2014-2015.tsv");
    public static final Historic NATU3_2014_2015    = Historic.fromCSV( "NATU3_2014-2015",  "data/histories/NATU3_2014-2015.tsv");
    public static final Historic PETR4_2014_2015    = Historic.fromCSV( "PETR4_2014-2015",  "data/histories/PETR4_2014-2015.tsv");
    public static final Historic SANB4_2014_2015    = Historic.fromCSV( "SANB4_2014-2015",  "data/histories/SANB4_2014-2015.tsv");
    public static final Historic UGPA3_2014_2015    = Historic.fromCSV( "UGPA3_2014-2015",  "data/histories/UGPA3_2014-2015.tsv");
    public static final Historic VALE3_2014_2015    = Historic.fromCSV( "VALE3_2014-2015",  "data/histories/VALE3_2014-2015.tsv");

    public static final Historic ALL_2014_2015 = new Historic("ALL_2014-2015", new ArrayList<>()).
            merge(BBAS3_2014_2015).
            merge(BBDC4_2014_2015).
            merge(CIEL3_2014_2015).
            merge(ITUB4_2014_2015).
            merge(JBSS3_2014_2015).
            merge(NATU3_2014_2015).
            merge(PETR4_2014_2015).
            merge(SANB4_2014_2015).
            merge(UGPA3_2014_2015).
            merge(VALE3_2014_2015)
            .sort();

    public static final Historic ALL_2016 = new Historic("ALL_2016", new ArrayList<>()).
            merge(BBAS3_2016).
            merge(BBDC4_2016).
            merge(CIEL3_2016).
            merge(ITUB4_2016).
            merge(JBSS3_2016).
            merge(NATU3_2016).
            merge(PETR4_2016).
            merge(SANB4_2016).
            merge(UGPA3_2016).
            merge(VALE3_2016)
            .sort();

    public static Historic getHistoric(Companies company) {
        switch (company) {
            case BBAS3: return BBAS3_2014_2015;
            case BBDC4: return BBDC4_2014_2015;
            case CIEL3: return CIEL3_2014_2015;
            case ITUB4: return ITUB4_2014_2015;
            case JBSS3: return JBSS3_2014_2015;
            case NATU3: return NATU3_2014_2015;
            case PETR4: return PETR4_2014_2015;
            case SANB4: return SANB4_2014_2015;
            case UGPA3: return UGPA3_2014_2015;
            case VALE3: return VALE3_2014_2015;
            default:    return null;
        }
    }

    public static Historic getCurrent(Companies company) {
        switch (company) {
            case BBAS3: return BBAS3_2016;
            case BBDC4: return BBDC4_2016;
            case CIEL3: return CIEL3_2016;
            case ITUB4: return ITUB4_2016;
            case JBSS3: return JBSS3_2016;
            case NATU3: return NATU3_2016;
            case PETR4: return PETR4_2016;
            case SANB4: return SANB4_2016;
            case UGPA3: return UGPA3_2016;
            case VALE3: return VALE3_2016;
            default:    return null;
        }
    }

}
