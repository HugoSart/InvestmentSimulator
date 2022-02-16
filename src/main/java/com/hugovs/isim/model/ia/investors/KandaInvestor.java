package com.hugovs.isim.model.ia.investors;

import com.hugovs.isim.model.DateUtils;
import com.hugovs.isim.model.client.Client;
import com.hugovs.isim.model.exchange.Companies;
import com.hugovs.isim.model.exchange.Historic;
import com.hugovs.isim.model.exchange.Quotation;
import com.hugovs.isim.model.ia.SmartInvestor;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.*;

import static java.util.stream.Collectors.toCollection;

public class KandaInvestor extends SmartInvestor {

    public Genetic genetic;

    public KandaInvestor(Client poorGuy, Historic knowledge) {
        super(poorGuy, knowledge);
    }

    @Override
    public void prepareToFight() {
        genetic = new Genetic();
    }

    @Override
    public void letmeWorkPlease() {
        Genetic.State state = genetic.geneticItself();
        TreeMap<Companies, Integer> buy = new TreeMap<>();
        TreeMap<Companies, Integer> sell = new TreeMap<>();
        for (Companies c : Companies.values()) {
            int difference = state.map.get(c) - getPoorGuy().check(c);
            if (difference < 0) sell.put(c, -difference);
            else if (difference > 0) buy.put(c, difference);
        }

        while (!sell.isEmpty()) {
            roundSell(sell.firstEntry().getKey(), sell.firstEntry().getValue());
            sell.remove(sell.firstKey());
        }
        while (!buy.isEmpty()) {
            roundBuy(buy.firstEntry().getKey(), buy.firstEntry().getValue());
            buy.remove(buy.firstKey());
        }
    }

    public class Genetic {

        private RankingCompanies topCompanies = new RankingCompanies();

        float[] ranking = topCompanies.rankCompanies();

        private TreeSet<State> population = null;

        public class State implements Comparable {
            public Map<Companies, Integer> map = new HashMap<>();

            @Override
            public int compareTo(@NotNull Object o) {
                State s1 = this, s2 = (State)o;
                float fs1 = fitness(s1), fs2 = fitness(s2);
                return Float.compare(fs2, fs1);
            }
        }

        private TreeSet<State> generatePopulation(int n) {
            TreeSet<State> population = new TreeSet<>();
            Random random = new Random();
            for(int i = 0; i < n; i++){
                State state = new State();
                for(Companies c: Companies.values()){
                    state.map.put(c, random.nextInt(1000));
                }
                population.add(state);
            }
            return population;
        }

        public State geneticItself() {

            Random random = new Random();

            if(population == null){
                population = generatePopulation(1000);
                System.out.println("\nPrimeira população: " + population.size() + "\n");
            }

            for(int i = 0; i < 100; i++){
                TreeSet<State> children = new TreeSet<>();
                for(int j = 0; j < 100; j++){
                    State father = select(population, random.nextInt(10));
                    State mother = select(population, random.nextInt(50) + 10);
                    State child1 = crossover(father, mother);
                    State child2 = crossover2(father, mother);
                    child1.map.put(Companies.values()[random.nextInt(10)], random.nextInt(500));
                    child2.map.put(Companies.values()[random.nextInt(10)], random.nextInt(500));
                    children.add(child1);
                    children.add(child2);
                }

                if(i == 99){
                    population.addAll(children);
                    population = population.stream().limit(100).collect(toCollection(TreeSet::new));
                    System.out.print(population.size() + " -> ");
                    for (State s : population)
                        System.out.print(fitness(s) + " ");
                    System.out.println();
                    return population.first();
                }
            }
            return null;
        }

        public float fitness(State state) {

            int i = 0;
            float value = 0;
            double stateValue = 0;
            for (Companies c : Companies.values()) {
                stateValue += c.getPrice() * state.map.get(c);

            }
            for (Companies c : Companies.values()) {
                if ((c.getPrice() * state.map.get(c)) / stateValue >= 0.6d) return 0;
            }
            if (stateValue < getPoorGuy().balance() * 0.90)
                return 0;

            for(Companies c : Companies.values()) {
                value += state.map.get(c) * ranking[i];
                i++;
            }
            return value;
        }


        private State select(TreeSet<State> population, int i) {
            return (State)population.toArray()[i];
        }

        private State crossover(State x, State y) {
            State child = new State();
            for (int i = 0; i < 5; i++) {
                child.map.put(Companies.values()[i], x.map.get(Companies.values()[i]));
            }
            for (int i = 5; i < 10; i++){
                child.map.put(Companies.values()[i], y.map.get(Companies.values()[i]));
            }
            return child;
        }

        private State crossover2(State x, State y){
            State child = new State();
            for (int i = 0; i < 5; i++) {
                child.map.put(Companies.values()[i], y.map.get(Companies.values()[i]));
            }
            for (int i = 5; i < 10; i++){
                child.map.put(Companies.values()[i], x.map.get(Companies.values()[i]));
            }
            return child;
        }

    }

    public class RankingCompanies {

        float[] valorization = new float[10];
        float[] positiveVariance = new float[10];
        float[] negativeVariance = new float[10];
        float[] weight = new float[10];

        public float[] rankCompanies(){

            //valorização do começo de 2014 até o fim de 2015 em porcentagem
            try {
                List<Quotation> firstDay = getKnowledge().getFromDate(DateUtils.dateFormat.parse("02/01/14"));
                List<Quotation> lastDay = getKnowledge().getFromDate(DateUtils.dateFormat.parse("30/12/15"));
                for (int i = 0; i < 10; i++) {
                    valorization[i] = ((lastDay.get(i).getPrice()*100)/firstDay.get(i).getPrice())-100;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //calculo das variancias positivas e negativas
            for (Companies companies : Companies.values()) {
                int i = 0;
                float averageAux = 0, posAux = 0, negAux = 0;
                float delta, average;
                int posCounter = 0, negCounter = 0;

                //calculo da media
                List<Quotation> allQuotations = getKnowledge().getFromCompany(companies);
                for (Quotation allQuotation : allQuotations) {
                    averageAux += allQuotation.getPrice();
                }
                average = averageAux/allQuotations.size();

                //variancias positiva e negativa
                for(Quotation allQuotation : allQuotations) {
                    delta = allQuotation.getPrice() - average;
                    if(delta > 0){
                        posAux += delta * delta;
                        posCounter++;
                    }
                    else {
                        negAux += delta * delta;
                        negCounter++;
                    }
                }
                positiveVariance[i] = posAux/(posCounter-1);
                negativeVariance[i] = negAux/(negCounter-1);


            }

            for(int i = 0; i < 10; i++){
                weight[i] = valorization[i] + positiveVariance[i] - negativeVariance[i];
            }
            return weight;
        }

    }
}