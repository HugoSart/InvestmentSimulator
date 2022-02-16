package com.hugovs.isim.model.ia.investors;

import com.hugovs.isim.model.DateUtils;
import com.hugovs.isim.model.client.Client;
import com.hugovs.isim.model.exchange.Companies;
import com.hugovs.isim.model.exchange.Historic;
import com.hugovs.isim.model.exchange.Quotation;
import com.hugovs.isim.model.ia.SmartInvestor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.DoubleToIntFunction;

public class YuriInvestor extends SmartInvestor {

    public YuriInvestor(Client poorGuy, Historic knowledge) {

        super(poorGuy, knowledge);
    }

    int daycheck = 0;
    double budgetArray[] = new double[10];
    double budgetArrayFinal[] = new double[10];
    int numberOfPapersArray[] = new int[10];
    double restArray[] = new double[10];
    boolean buyOrderArray[] = new boolean[10];
    boolean moneyWorkingArray[] = new boolean[10];
    Date buyDayArray[] = new Date[10];

    public double buildMME(Companies company, int period){
        double mme, daycotation = 0, m, previousmme = 0;
        mme = 0;
        int i;
        m = (2./(1. + period));

        List <Quotation> quotations = getKnowledge().getFromCompany(company);

//        MME
        for(i = quotations.size()-period-1; i <= quotations.size()-1; i++){

            daycotation = quotations.get(i).getPrice();


            mme = (daycotation * m) + (previousmme * (1-m));


            previousmme = mme;
        }
        return mme;
    }

    public boolean dailyInvestor(double shortestMME, double largestMME, Companies company){
        // mme mais larga - mme mais curta = negativo => buy signal
        // mme mais larga - mme mais curta = positivo => sell signal
        boolean buyOrder = false;

        if(largestMME - shortestMME < 0) {
            buyOrder = true;
//            System.out.println("Ordem de compra na empresa " + company.name() + "Valor do MME: " + (largestMME - shortestMME));
        }
        else{
            buyOrder = false;
//            System.out.println("Ordem de venda na empresa " + company.name() + "Valor do MME: " + (largestMME - shortestMME));
        }

        return buyOrder;
    }

    public boolean setBuyOrder(int position){

        double mme3, mme21;
        Companies momentCompany;
        boolean buyOrder = false;
        int lowperiod = 3, highperiod = 21;


        // gera ordens de compra ou venda
        switch(position){
            case 0:
                momentCompany = Companies.BBAS3;
                mme3 = buildMME(momentCompany, lowperiod);
                mme21 = buildMME(momentCompany, highperiod);
                buyOrder = dailyInvestor(mme3, mme21, momentCompany);
                break;

            case 1:
                momentCompany = Companies.BBDC4;
                mme3 = buildMME(momentCompany, lowperiod);
                mme21 = buildMME(momentCompany, highperiod);
                buyOrder = dailyInvestor(mme3, mme21, momentCompany);
                break;

            case 2:
                momentCompany = Companies.CIEL3;
                mme3 = buildMME(momentCompany, lowperiod);
                mme21 = buildMME(momentCompany, highperiod);
                buyOrder = dailyInvestor(mme3, mme21, momentCompany);
                break;

            case 3:
                momentCompany = Companies.ITUB4;
                mme3 = buildMME(momentCompany, lowperiod);
                mme21 = buildMME(momentCompany, highperiod);
                buyOrder = dailyInvestor(mme3, mme21, momentCompany);
                break;

            case 4:
                momentCompany = Companies.JBSS3;
                mme3 = buildMME(momentCompany, lowperiod);
                mme21 = buildMME(momentCompany, highperiod);
                buyOrder = dailyInvestor(mme3, mme21, momentCompany);
                break;

            case 5:
                momentCompany = Companies.NATU3;
                mme3 = buildMME(momentCompany, lowperiod);
                mme21 = buildMME(momentCompany, highperiod);
                buyOrder = dailyInvestor(mme3, mme21, momentCompany);
                break;

            case 6:
                momentCompany = Companies.PETR4;
                mme3 = buildMME(momentCompany, lowperiod);
                mme21 = buildMME(momentCompany, highperiod);
                buyOrder = dailyInvestor(mme3, mme21, momentCompany);
                break;

            case 7:
                momentCompany = Companies.SANB4;
                mme3 = buildMME(momentCompany, lowperiod);
                mme21 = buildMME(momentCompany, highperiod);
                buyOrder = dailyInvestor(mme3, mme21, momentCompany);
                break;

            case 8:
                momentCompany = Companies.UGPA3;
                mme3 = buildMME(momentCompany, lowperiod);
                mme21 = buildMME(momentCompany, highperiod);
                buyOrder = dailyInvestor(mme3, mme21, momentCompany);
                break;

            case 9:
                momentCompany = Companies.VALE3;
                mme3 = buildMME(momentCompany, lowperiod);
                mme21 = buildMME(momentCompany, highperiod);
                buyOrder = dailyInvestor(mme3, mme21, momentCompany);
                break;
            }

            return buyOrder;
    }

    public Companies setCompany(int position){
        Companies returnCompany = null;

        switch(position){
            case 0:
                returnCompany = Companies.BBAS3;
                break;

            case 1:
                returnCompany = Companies.BBDC4;
                break;

            case 2:
                returnCompany = Companies.CIEL3;
                break;

            case 3:
                returnCompany = Companies.ITUB4;
                break;

            case 4:
                returnCompany = Companies.JBSS3;
                break;

            case 5:
                returnCompany = Companies.NATU3;
                break;

            case 6:
                returnCompany = Companies.PETR4;
                break;

            case 7:
                returnCompany = Companies.SANB4;
                break;

            case 8:
                returnCompany = Companies.UGPA3;
                break;

            case 9:
                returnCompany = Companies.VALE3;
                break;
        }

        return returnCompany;
    }




    @Override
    public void letmeWorkPlease() {
        // 1/10 do capital inicial da carteira vai para cada empresa
        // o pensamento é: melhor investir em todas ao invés de concentrar tudo em uma
        // chance de falencia cedo é menor

        int i, daysAgo, presentDay, numberOfPapers, diaDaVenda;
        double mme21, mme3, buyCotation, sellCotation, percentChange, manipulation, valorCarteira, cashConverted, profit, rest;
        Double numberOfPapersDouble;
        Companies momentCompany;

        // armazena o dia que comprou
        // chega no dia de vender faz o % da diferença entre o dia que comprou e o dia que vendeu
        // a % que for vai ser somado com o budgetInicial

        // 0 = bbas3, 1 = bbdc4, 2 = ciel3, 3 = itub4, 4 = jbss3, 5 = natu3
        // 6 = petr4, 7 = sanb4, 8 = ugpa3, 9 = vale3




        for (i = 0; i < 10; i++){
            List <Quotation> quotations = getKnowledge().getFromCompany(setCompany(i));
            buyOrderArray[i] = setBuyOrder(i);

            // compra
            // condição: ordem de compra

            if (buyOrderArray[i] == true && moneyWorkingArray[i] == false){
                buyDayArray[i] = quotations.get(quotations.size()-1).getDate();

                moneyWorkingArray[i] = true;
                cashConverted = budgetArrayFinal[i] * 100000;
                presentDay = quotations.size() - 1;
                buyCotation = quotations.get(presentDay).getPrice();
                numberOfPapersDouble = (cashConverted / buyCotation);
                rest = numberOfPapersDouble - Math.floor(numberOfPapersDouble);
                numberOfPapers = numberOfPapersDouble.intValue();

                numberOfPapersArray[i] = numberOfPapers;

                buy(setCompany(i), numberOfPapers);

                restArray[i] = rest;
//                System.out.println("Ordem de compra da empresa " + setCompany(i) + " data: " + quotations.get(quotations.size()-1).getDate() + " quantidade de papeis comprados: " + numberOfPapers);
//                System.out.print(" preço pago: " + buyCotation + " resto:" + rest);

            }

            // venda
            // condições: ultimo dia do ano na bolsa ou ordem de venda


            if ((buyOrderArray[i] == false && moneyWorkingArray[i] == true) ){
                presentDay = quotations.size() - 1;
                diaDaVenda = presentDay;



                while(quotations.get(diaDaVenda).getDate() != buyDayArray[i]){
                    diaDaVenda--;
                }

                daysAgo = quotations.size() - (quotations.size() - diaDaVenda);


                buyCotation = quotations.get(daysAgo).getPrice();

                sellCotation = quotations.get(presentDay).getPrice();

                percentChange = (sellCotation / buyCotation) - 1.;

                manipulation = percentChange * 100;

                manipulation = (100 + manipulation) / 100;

                budgetArray[i] = manipulation * budgetArray[i];

                numberOfPapers = numberOfPapersArray[i];
                rest = restArray[i];

                profit = (numberOfPapers * sellCotation) + (rest * buyCotation);

                budgetArrayFinal[i] = profit / 100000;

                sell(setCompany(i), numberOfPapers);

                numberOfPapersArray[i] = 0;


                moneyWorkingArray[i] = false;


//                    System.out.println("Ordem de venda da empresa " + setCompany(i) + " data: " + quotations.get(quotations.size()-1).getDate());
//                    System.out.print(" quantidade de papeis vendidos: " + numberOfPapers + " preco de compra: " + buyCotation + " preco de venda: " + sellCotation);
//                    System.out.print(" dia da compra: " + quotations.get(daysAgo).getDate());

            }

        }


//        valorCarteira = 0;
//        System.out.println(season.today);
//        for (i = 0; i < 10; i++){
//            valorCarteira += budgetArray[i];
//            System.out.println("Empresa: " + setCompany(i).name() + " % de capital " + budgetArrayFinal[i]);
//        }
//        System.out.println("Valor carteira: " + valorCarteira);
    }


    @Override
    public void prepareToFight() {
        int i;

        for (i = 0; i < 10; i++){
            budgetArrayFinal[i] = 0.1;
            budgetArray[i] = 0.1;
            buyOrderArray[i] = false;
            moneyWorkingArray[i] = false;
            numberOfPapersArray[i] = 0;
            restArray[i] = 0;
        }
    }

}
