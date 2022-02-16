package com.hugovs.isim.model.ia.investors;

import com.hugovs.isim.model.exchange.Companies;
import com.hugovs.isim.model.exchange.Historic;
import com.hugovs.isim.model.exchange.Quotation;
import com.hugovs.isim.model.ia.SmartInvestor;
import com.hugovs.isim.model.client.Client;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toConcurrentMap;

/**
 * User: hugo_<br/>
 * Date: 29/04/2018<br/>
 * Time: 00:17<br/>
 */

/** /
 *  Iéia: Algorítmo A*
 *  Heuristica: Maior tempo em queda des do ultimo pico (Não admissível)
 *  Estado: número de ações em cada empresa
 */
public class ZeInvestor extends SmartInvestor {

    public Genetic genetic;

    public ZeInvestor(Client poorGuy, Historic knowledge) {
        super(poorGuy, knowledge);
    }

    @Override
    public void prepareToFight() {
        genetic = new Genetic();
    }

    @Override
    public void letmeWorkPlease() {

        Genetic.State state = genetic.search();

        TreeMap<Companies, Integer> buy = new TreeMap<>();
        TreeMap<Companies, Integer> sell = new TreeMap<>();
        double total = 0f;
        for (Companies c : Companies.values()) {
            int dif = state.map.get(c) - getPoorGuy().check(c);
            if (dif < 0) sell.put(c, dif);
            else if (dif > 0) buy.put(c, dif);
            total += getPoorGuy().check(c) * c.getPrice();
        }

        while (!sell.isEmpty()) {
            roundSell(sell.firstEntry().getKey(), -sell.firstEntry().getValue());
            sell.remove(sell.firstKey());
        }
        while (!buy.isEmpty()) {
            roundBuy(buy.firstEntry().getKey(), buy.firstEntry().getValue());
            buy.remove(buy.firstKey());
        }

        //genetic.statistics.update();

    }

    public class Genetic {

        public Statistics statistics = new Statistics();

        public class State implements Comparable {
            public Map<Companies, Integer> map = new LinkedHashMap<>();

            @Override
            public int compareTo(@NotNull Object o) {
                State s1 = this, s2 = (State)o;
                double fs1 = fitness(s1), fs2 = fitness(s2);
                return Double.compare(fs2, fs1);
            }

            public int total() {
                int r = 0;
                for (Integer integer : map.values())
                    r += integer;
                return r;
            }

            public double totalPrice() {
                double r = 0;
                int i = 0;
                for (Integer integer : map.values()) {
                    r += (double) integer * Companies.values()[i].getPrice();
                    i++;
                }
                return r;
            }
        }

        public double fitness(State state) {

            double h = 0f;

            double total = 0f;
            for (Companies c : Companies.values())
                total += c.getPrice() * state.map.get(c);

            if (total < getPoorGuy().balance() * 0.90) return 0;

            int count = 0;
            for (Companies c : Companies.values()) {

                double var1 = (float)state.map.get(c) / (float)state.total();
                double var2 = c.getPrice() / Companies.total;
                double upProb =  statistics.getUpProb(c);
                double variation = (1d - c.getVariationPercentAcum() / 100d);

                double ch = var1 * variation + upProb;

                h += ch;
                count++;

            }

            return h/(float)count;
        }

        private TreeSet<State> pop = null;
        public State search() {

            if (pop == null) pop = generatePopulation(1000);

            int m = 100;
            Random rand = new Random();
            while (true) {

                TreeSet<State> newPop = new TreeSet<>();
                int n = pop.size();
                if (n > 500) n = 500;
                for (int i = 0; i < n - 1; i++) {
                    State x = select(pop, i), y = select(pop, rand.nextInt(n - 1));
                    State son = crossover(x, y);

                    if ((new Random()).nextInt(100) <= 5)
                        mutate(son);

                    newPop.add(son);
                }

                m--;
                if (m == 0) {
                    pop.addAll(newPop);
                    pop = pop.stream().limit(100).collect(toCollection(TreeSet::new));
                    System.out.print(pop.size() + " -> ");
                    for (State s : pop)
                        System.out.print(fitness(s) + " ");
                    System.out.println();

                    return pop.first();
                }

            }

        }

        private State select(TreeSet<State> population, int i) {
            return (State)population.toArray()[i];
        }

        private State crossover(State x, State y) {

            State son = new State();

            int rand = new Random().nextInt(10);

            for (int i = 0; i < rand; i ++) {
                Companies c = Companies.values()[i];
                son.map.put(c, x.map.get(c));
            }

            for (int i = rand; i < Companies.values().length; i ++) {
                Companies c = Companies.values()[i];
                son.map.put(c, y.map.get(c));
            }

            return son;
        }

        private void mutate(State state) {
            Companies company = Companies.values()[(new Random()).nextInt(Companies.values().length)];
            state.map.put(company, state.map.get(company));
        }

        private TreeSet<State> generatePopulation(int n) {
            TreeSet<State> pop = new TreeSet<>();
            Random rand = (new Random());
            for (int i = 0; i < n; i++) {
                State state = new State();
                for (Companies c : Companies.values()) {
                    int r = rand.nextInt(1000);
                    state.map.put(c, r);
                }
                pop.add(state);
            }
            return pop;
        }

    }

    public class Statistics {

        private Map<Companies, Double> upProb;

        public Statistics() {
            upProb = new LinkedHashMap<>();
            for (Companies c : Companies.values())
                upProb.put(c, 1d);
            init();
        }

        public double getUpProb(Companies company) {
            return upProb.get(company);
        }

        public void init() {

            for (Companies c : Companies.values()) {

                List<Quotation> quotations = getKnowledge().getFromCompany(c);
                double upCount = 0, downCount = 0, upAfterUpCount = 0, downAfterDownCount = 0;
                int lastWasUp = 0, lastWasDown = 0;
                for (int i = 1; i < quotations.size(); i++) {
                    Quotation current = quotations.get(i);
                    Quotation previous = quotations.get(i - 1);
                    if (current.getPrice() >= previous.getPrice()) {
                        upCount++;
                        if (lastWasUp != 0) upAfterUpCount += lastWasUp + 1;
                        lastWasUp++;
                        lastWasDown = 0;
                    } else {
                        downCount++;
                        if (lastWasUp == 0) downAfterDownCount += lastWasDown + 1;
                        lastWasUp = 0;
                        lastWasDown++;
                    }
                }

                double uprob = (((double)upCount / ((double)upCount + (double)downCount)) + 8f * ((double)upAfterUpCount / (((double) upAfterUpCount) + (double)downAfterDownCount))) / 9f;
                upProb.put(c, uprob);

            }
        }

        public void update() {
            for (Companies c : Companies.values()) {
                double gc = upProb.get(c), vp = (1f - c.getVariationPercent() / 100f);
                double nv = gc;
                upProb.put(c, nv);
            }
        }

    }

}