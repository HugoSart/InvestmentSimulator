package com.hugovs.isim.controller;

import com.hugovs.isim.model.DateUtils;
import com.hugovs.isim.model.Season;
import com.hugovs.isim.model.exchange.Companies;
import com.hugovs.isim.model.exchange.Database;
import com.hugovs.isim.model.exchange.Historic;
import com.hugovs.isim.model.exchange.Quotation;
import fxscenario.NodeCustomizer;
import fxscenario.Scenario;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * User: hugo_<br/>
 * Date: 30/04/2018<br/>
 * Time: 01:53<br/>
 */
public class RealTimeScenario extends Scenario {

    @FXML private Pane menuBar;
    @FXML private Button btExit;

    @FXML private Label lbBBAS3;
    @FXML private Label lbBBDC4;
    @FXML private Label lbCIEL3;
    @FXML private Label lbITUB4;
    @FXML private Label lbJBSS3;
    @FXML private Label lbNATU3;
    @FXML private Label lbPETR4;
    @FXML private Label lbSANB4;
    @FXML private Label lbUGPA3;
    @FXML private Label lbVALE3;

    @FXML private LineChart<String, Number> bcRealtime;

    private Label[] labels;
    private Season season;

    public RealTimeScenario(Season season) {
        super("view/fxml/scenario_realtime.fxml");
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
        NodeCustomizer.setUpMenuBar(this, menuBar, btExit, null, null);
        bcRealtime.setCreateSymbols(false);
        labels = new Label[10];
        labels[0] = lbBBAS3;
        labels[1] = lbBBDC4;
        labels[2] = lbCIEL3;
        labels[3] = lbITUB4;
        labels[4] = lbJBSS3;
        labels[5] = lbNATU3;
        labels[6] = lbPETR4;
        labels[7] = lbSANB4;
        labels[8] = lbUGPA3;
        labels[9] = lbVALE3;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ControllerUtils.realTimeScenario = null;
    }

    public void updateAll() {

        bcRealtime.getData().clear();
        for (Companies company : Companies.values()) {
            labels[company.ordinal()].setText("R$ " + String.valueOf(company.getPrice()));
            XYChart.Series series = new XYChart.Series();
            Historic historic = Database.getCurrent(company);
            series.setName(company.name());

            for (Quotation q : historic.getFromInterval(season.initialDate, season.today)) {
                series.getData().add(new XYChart.Data<>(DateUtils.dateFormat.format(q.getDate()), q.getPrice()));
            }

            bcRealtime.getData().add(series);
        }

    }



}
