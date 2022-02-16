package com.hugovs.isim.controller;

import com.hugovs.isim.model.exchange.Companies;
import com.hugovs.isim.model.exchange.Database;
import com.hugovs.isim.model.exchange.Quotation;
import fxscenario.NodeCustomizer;
import fxscenario.Scenario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

import static com.hugovs.isim.model.exchange.Database.getHistoric;

/**
 * User: hugo_<br/>
 * Date: 29/04/2018<br/>
 * Time: 23:33<br/>
 */
public class HistoriesScenario extends Scenario {

    @FXML private Pane hbMenuBar;
    @FXML private Button btExit;

    @FXML private LineChart<String, Number> lcBBAS3;
    @FXML private LineChart<String, Number> lcBBDC4;
    @FXML private LineChart<String, Number> lcCIEL3;
    @FXML private LineChart<String, Number> lcITUB4;
    @FXML private LineChart<String, Number> lcJBSS3;
    @FXML private LineChart<String, Number> lcNATU3;
    @FXML private LineChart<String, Number> lcPETR4;
    @FXML private LineChart<String, Number> lcSANB4;
    @FXML private LineChart<String, Number> lcUGPA3;
    @FXML private LineChart<String, Number> lcVALE3;

    @FXML private DatePicker dpStart;
    @FXML private DatePicker dpEnd;

    private LineChart[] charts;

    public HistoriesScenario() {
        super("view/fxml/scenario_histories.fxml");
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
        NodeCustomizer.setUpMenuBar(this, hbMenuBar, btExit, null, null);
        stage.setTitle("Histórico de Cotações");

        charts = new LineChart[10];
        charts[0] = lcBBAS3;
        charts[1] = lcBBDC4;
        charts[2] = lcCIEL3;
        charts[3] = lcITUB4;
        charts[4] = lcJBSS3;
        charts[5] = lcNATU3;
        charts[6] = lcPETR4;
        charts[7] = lcSANB4;
        charts[8] = lcUGPA3;
        charts[9] = lcVALE3;

        dpStart.setValue(LocalDate.of(2014, Month.JANUARY, 1));
        dpEnd.setValue(LocalDate.of(2015, Month.DECEMBER, 31));
        createAChartForEachCompany(2);


    }

    private void createAChartForEachCompany(int round) {

        for (Companies company : Companies.values()) {

            final LineChart<String, Number> chart = charts[company.ordinal()];
            chart.setTitle(company.name());
            chart.setCreateSymbols(false);

            XYChart.Series series = new XYChart.Series();
            List<Quotation> quotations = getHistoric(company).getFromInterval(java.sql.Date.valueOf(dpStart.getValue()), java.sql.Date.valueOf(dpEnd.getValue()));
            for (Quotation q : quotations)
                series.getData().add(new XYChart.Data<>((new SimpleDateFormat("dd/MM/yy")).format(q.getDate()), q.getPrice()));
            chart.getData().add(series);
        }

    }

    @FXML
    private void btRefresh_onAction(ActionEvent event) {
        for (LineChart chart : charts) {
            chart.getData().clear();
        }
        createAChartForEachCompany(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ControllerUtils.historiesScenario = null;
    }
}
