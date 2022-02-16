package com.hugovs.isim.controller;

import com.hugovs.isim.model.DateUtils;
import com.hugovs.isim.model.Season;
import com.hugovs.isim.model.exchange.Companies;
import com.hugovs.isim.model.exchange.Database;
import com.hugovs.isim.model.ia.SmartInvestor;
import com.hugovs.isim.model.ia.investors.InvestorHolder;
import com.hugovs.isim.model.ia.investors.ZeInvestor;
import fxscenario.NodeCustomizer;
import fxscenario.Scenario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.text.ParseException;
import java.util.List;

/**
 * User: hugo_<br/>
 * Date: 30/04/2018<br/>
 * Time: 01:53<br/>
 */
public class TransactionsScenario extends Scenario {

    @FXML
    private Pane menuBar;
    @FXML
    private Button btExit;

    @FXML
    private Label lbBuy;
    @FXML
    private Label lbSell;
    @FXML
    private Label lbTotal;
    @FXML
    private Label lbPositive;
    @FXML
    private Label lbNegative;
    @FXML
    private Label lbPercent;
    @FXML
    private Label lbBuyList;
    @FXML
    private Label lbDateList;
    @FXML
    private Label lbNameList;
    @FXML
    private Label lbQntList;

    @FXML private Button btZe;
    @FXML private Button btKanda;
    @FXML private Button btYuri;
    @FXML private Button btOracle;


    @FXML
    private BarChart<String, Integer> bcTransactions;

    private Season season;

    public TransactionsScenario(Season season) {
        super("view/fxml/scenario_transactions.fxml");
        this.season = season;
    }

    @Override
    protected void onConfigScene(Scene scene) {
        super.onConfigScene(scene);
        scene.getStylesheets().add("view/css/Style.css");
    }

    @Override
    protected void onConfigStage(Stage stage) {
        super.onConfigStage(stage);
        setUpScenarioStyle(ScenarioStyle.BETTER_UNDECORATED);
        System.out.println(btExit);
        System.out.println(menuBar);
        NodeCustomizer.setUpMenuBar(this, menuBar, btExit, null, null);
        updateAll();
    }

    public void updateAll() {

        SmartInvestor investor = season.investor;
        List<SmartInvestor.Transaction> transactions = investor.getTransactions();

        btZe.setDisable(investor == InvestorHolder.ze);
        btKanda.setDisable(investor == InvestorHolder.kanda);
        btYuri.setDisable(investor == InvestorHolder.yuri);
        btOracle.setDisable(investor == InvestorHolder.oracle);

        int qntBuy = investor.getBuyTransactions().size();
        int qntSell = investor.getSellTransactions().size();
        int qntTotal = qntBuy + qntSell;

        int qntPositive = 0, qntNegative = 0;
        for (SmartInvestor.Transaction transaction : transactions) {
            //System.out.println("Value: " + transaction.value + "\tNow: " + transaction.company.getPrice());
            if (transaction.value >= transaction.quantity * transaction.company.getPrice()) qntNegative++;
            else qntPositive++;
        }

        float percent = qntTotal == 0 ? 0 : ((float)qntPositive / (float)qntTotal) * 100f;

        lbBuy.setText(String.valueOf(qntBuy));
        lbSell.setText(String.valueOf(qntSell));
        lbTotal.setText(String.valueOf(qntTotal));
        lbPositive.setText(String.valueOf(qntPositive));
        lbNegative.setText(String.valueOf(qntNegative));
        lbPercent.setText(String.valueOf(percent) + "%");

        StringBuilder dateBuilder = new StringBuilder(), typeBuilder = new StringBuilder(), nameBuilder = new StringBuilder(), qntBuilder = new StringBuilder();
        for (SmartInvestor.Transaction transaction : transactions) {
            dateBuilder.append(DateUtils.dateFormat.format(transaction.date)).append("\n");
            typeBuilder.append(transaction.action.name()).append("\n");
            nameBuilder.append(transaction.company.name()).append("\n");
            qntBuilder.append(transaction.quantity).append("\n");
        }

        lbDateList.setText(dateBuilder.toString());
        lbBuyList.setText(typeBuilder.toString());
        lbNameList.setText(nameBuilder.toString());
        lbQntList.setText(qntBuilder.toString());

        bcTransactions.getData().clear();
        XYChart.Series buySeries = new XYChart.Series(); buySeries.setName("Compras");
        XYChart.Series sellSeries = new XYChart.Series(); sellSeries.setName("Vendas");
        for (Companies company : Companies.values()) {

            List<SmartInvestor.Transaction> companyTransactions = investor.getTransactionsFrom(company);
            int cBuy = 0, cSell = 0;
            for (SmartInvestor.Transaction companyTransaction : companyTransactions) {
                if (companyTransaction.action == SmartInvestor.Action.BUY) cBuy++;
                else cSell++;
            }

            buySeries.getData().add(new XYChart.Data(company.name(), cBuy));
            sellSeries.getData().add(new XYChart.Data(company.name(), cSell));

        }

        bcTransactions.getData().addAll(buySeries, sellSeries);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ControllerUtils.transactionsScenario = null;
    }

    @FXML
    public void btZe_onAction(ActionEvent event) {
        btZe.setDisable(true);
        btKanda.setDisable(false);
        btYuri.setDisable(false);
        btOracle.setDisable(false);
        season.setInvestor(InvestorHolder.ze);
        updateAll();
    }

    @FXML
    public void btKanda_onAction(ActionEvent event) {
        btZe.setDisable(false);
        btKanda.setDisable(true);
        btYuri.setDisable(false);
        btOracle.setDisable(false);
        season.setInvestor(InvestorHolder.kanda);
        updateAll();
    }

    @FXML
    public void btYuri_onAction(ActionEvent event) {
        btZe.setDisable(false);
        btKanda.setDisable(false);
        btYuri.setDisable(true);
        btOracle.setDisable(false);
        season.setInvestor(InvestorHolder.yuri);
        updateAll();
    }

    @FXML
    public void btOracle_onAction(ActionEvent event) {
        btZe.setDisable(false);
        btKanda.setDisable(false);
        btYuri.setDisable(false);
        btOracle.setDisable(true);
        season.setInvestor(InvestorHolder.oracle);
        updateAll();
    }

}
