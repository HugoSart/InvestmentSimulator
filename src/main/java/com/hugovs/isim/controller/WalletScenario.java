package com.hugovs.isim.controller;

import com.hugovs.isim.model.DateUtils;
import com.hugovs.isim.model.Season;
import com.hugovs.isim.model.exchange.Companies;
import com.hugovs.isim.model.exchange.Database;
import com.hugovs.isim.model.ia.SmartInvestor;
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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: hugo_<br/>
 * Date: 30/04/2018<br/>
 * Time: 01:52<br/>
 */
public class WalletScenario extends Scenario {

    @FXML private Pane menuBar;
    @FXML private Button btExit;
    @FXML private Button btCompany;
    @FXML private Button btMonth;
    @FXML private Label lbInitial;
    @FXML private Label lbCurrent;
    @FXML private Label lbProfit;
    @FXML private Label lbProfitPercent;

    @FXML private Label lbQnt1;
    @FXML private Label lbQnt2;
    @FXML private Label lbQnt3;
    @FXML private Label lbQnt4;
    @FXML private Label lbQnt5;
    @FXML private Label lbQnt6;
    @FXML private Label lbQnt7;
    @FXML private Label lbQnt8;
    @FXML private Label lbQnt9;
    @FXML private Label lbQnt10;
    @FXML private Label lbQntTotal;

    @FXML private Label lbPu1;
    @FXML private Label lbPu2;
    @FXML private Label lbPu3;
    @FXML private Label lbPu4;
    @FXML private Label lbPu5;
    @FXML private Label lbPu6;
    @FXML private Label lbPu7;
    @FXML private Label lbPu8;
    @FXML private Label lbPu9;
    @FXML private Label lbPu10;
    @FXML private Label lbPuTotal;

    @FXML private Label lbPt1;
    @FXML private Label lbPt2;
    @FXML private Label lbPt3;
    @FXML private Label lbPt4;
    @FXML private Label lbPt5;
    @FXML private Label lbPt6;
    @FXML private Label lbPt7;
    @FXML private Label lbPt8;
    @FXML private Label lbPt9;
    @FXML private Label lbPt10;
    @FXML private Label lbPtTotal;

    @FXML private BarChart<String, Number> bcProfit;

    private Label[] qntLabels, puLabels, ptLabels;

    private Season season;
    private SmartInvestor investor;

    public WalletScenario(Season season) {
        super("view/fxml/scenario_wallet.fxml");
        this.season = season;
        this.investor = season.investor;
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
        NodeCustomizer.setUpMenuBar(this, menuBar, btExit, null, null);

        initLabels();
        updateAll();

    }

    private void initLabels() {
        qntLabels = new Label[10]; puLabels = new Label[10]; ptLabels = new Label[10];
        qntLabels[0] = lbQnt1;
        qntLabels[1] = lbQnt2;
        qntLabels[2] = lbQnt3;
        qntLabels[3] = lbQnt4;
        qntLabels[4] = lbQnt5;
        qntLabels[5] = lbQnt6;
        qntLabels[6] = lbQnt7;
        qntLabels[7] = lbQnt8;
        qntLabels[8] = lbQnt9;
        qntLabels[9] = lbQnt10;

        puLabels[0] = lbPu1;
        puLabels[1] = lbPu2;
        puLabels[2] = lbPu3;
        puLabels[3] = lbPu4;
        puLabels[4] = lbPu5;
        puLabels[5] = lbPu6;
        puLabels[6] = lbPu7;
        puLabels[7] = lbPu8;
        puLabels[8] = lbPu9;
        puLabels[9] = lbPu10;

        ptLabels[0] = lbPt1;
        ptLabels[1] = lbPt2;
        ptLabels[2] = lbPt3;
        ptLabels[3] = lbPt4;
        ptLabels[4] = lbPt5;
        ptLabels[5] = lbPt6;
        ptLabels[6] = lbPt7;
        ptLabels[7] = lbPt8;
        ptLabels[8] = lbPt9;
        ptLabels[9] = lbPt10;
    }

    @Override
    protected void onDestroy() {
        ControllerUtils.walletScenario = null;
    }



    public void updateAll() {

        DecimalFormat dFormat = new DecimalFormat("##.00");

        double initialBalance = investor.getPoorGuy().getWallet().initialBalance();
        double profit = investor.getPoorGuy().getWallet().balance() + investor.getPoorGuy().balance() - investor.getPoorGuy().getWallet().initialBalance();
        double aggregated = investor.getPoorGuy().getWallet().balance() + investor.getPoorGuy().balance();

        lbInitial.setText("R$ " + String.valueOf(dFormat.format(initialBalance)));
        lbCurrent.setText("R$ " + String.valueOf(dFormat.format(aggregated)));
        lbProfit.setText("R$ " + String.valueOf(dFormat.format(profit)));
        lbProfitPercent.setText(String.valueOf(dFormat.format((aggregated / initialBalance - 1.0) * 100)) + "%");

        int qntTotal = 0;
        double puTotal = 0, ptTotal = 0;
        for (Companies company : Companies.values()) {
            qntLabels[company.ordinal()].setText(String.valueOf(investor.check(company)));
            puLabels[company.ordinal()].setText("R$ " + String.valueOf(dFormat.format(company.getPrice())));
            ptLabels[company.ordinal()].setText("R$ " + String.valueOf(dFormat.format((double)investor.check(company) * company.getPrice())));
            qntTotal += investor.check(company);
            puTotal += company.getPrice();
            ptTotal += investor.check(company) * company.getPrice();
        }

        lbQntTotal.setText(String.valueOf(qntTotal));
        lbPuTotal.setText("R$ " + String.valueOf(dFormat.format(puTotal)));
        lbPtTotal.setText("R$ " + String.valueOf(dFormat.format(ptTotal)));

        if (btCompany.isDisable()) setUpCompanyChart();
        else setUpMonthChart();

    }

    private void setUpMonthChart() {
        bcProfit.getData().clear();
        XYChart.Series series = new XYChart.Series();
        for (String month : investor.monthProfit.keySet())
            series.getData().add(new XYChart.Data(month, investor.monthProfit.get(month)));

        bcProfit.getData().add(series);
        bcProfit.setTitle("Lucro por mês");
    }

    private void setUpCompanyChart() {
        bcProfit.getData().clear();
        XYChart.Series series = new XYChart.Series();
        for (Companies company : Companies.values()) {
            try {
                series.getData().add(new XYChart.Data(company.name(), company.getPrice() * investor.check(company) - investor.check(company) * Database.getHistoric(company).getFromDate(DateUtils.dateFormat.parse("30/12/15")).get(0).getPrice()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        bcProfit.getData().add(series);
        bcProfit.setTitle("Lucro por empresa");
    }

    @FXML
    public void btCompany_onAction(ActionEvent event) {
        setUpCompanyChart();
        btCompany.setDisable(true);
        btMonth.setDisable(false);
        setUpCompanyChart();
    }

    @FXML
    public void btMonth_onAction(ActionEvent event) {
        setUpMonthChart();
        btCompany.setDisable(false);
        btMonth.setDisable(true);
        setUpMonthChart();
    }

}
