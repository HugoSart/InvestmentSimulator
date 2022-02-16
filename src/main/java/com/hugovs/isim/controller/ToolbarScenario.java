package com.hugovs.isim.controller;

import com.hugovs.isim.model.Season;
import com.hugovs.isim.model.client.Wallet;
import com.hugovs.isim.model.exchange.Companies;
import com.hugovs.isim.model.exchange.Database;
import com.hugovs.isim.model.exchange.Historic;
import com.hugovs.isim.model.exchange.Quotation;
import com.hugovs.isim.model.ia.SmartInvestor;
import fxscenario.NodeCustomizer;
import fxscenario.Scenario;
import fxscenario.Spawner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;

/**
 * User: hugo_<br/>
 * Date: 29/04/2018<br/>
 * Time: 21:02<br/>
 */
public class ToolbarScenario extends Scenario {

    @FXML private Pane apToolbar;
    @FXML private Button btExit;
    @FXML private Label lbDate;
    @FXML private Button btNextDay;
    @FXML private Button btNextMonth;

    private Season season;

    public ToolbarScenario(Season season) {
        super("view/fxml/scenario_main.fxml");
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
        NodeCustomizer.setUpMenuBar(this, apToolbar, btExit, null, null);

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    }

    @FXML
    public void btRealTime_actionHandler(ActionEvent action) {
        if (ControllerUtils.realTimeScenario == null) {
            ControllerUtils.realTimeScenario = new RealTimeScenario(season);
            Spawner.startScenario(ControllerUtils.realTimeScenario, this);
        }
    }

    @FXML
    public void btWallet_actionHandler(ActionEvent action) {
        if (ControllerUtils.walletScenario == null) {
            ControllerUtils.walletScenario = new WalletScenario(season);
            Spawner.startScenario(ControllerUtils.walletScenario, this);
        }
    }

    @FXML
    public void btTransactions_actionHandler(ActionEvent action) {
        if (ControllerUtils.transactionsScenario == null) {
            ControllerUtils.transactionsScenario = new TransactionsScenario(season);
            Spawner.startScenario(ControllerUtils.transactionsScenario, this);
        }
    }

    @FXML
    public void btHistories_actionHandler(ActionEvent action) {
        if (ControllerUtils.transactionsScenario == null) {
            ControllerUtils.historiesScenario = new HistoriesScenario();
            Spawner.startScenario(ControllerUtils.historiesScenario, this);
        }
    }

    @FXML
    public void btExit_onAction(ActionEvent evnet) {
        System.exit(0);
    }

    @FXML
    public void btNextDay_onAction(ActionEvent event) {
        for (int i = 0; i < 1 /*&& !season.today.equals(season.finalDate)*/; i++) {
            season.oneMoreDay();
            updateAll();
        }
        if (season.today.equals(season.finalDate)) {
            //btNextDay.setDisable(true);
            //btNextMonth.setDisable(true);
        }
    }

    @FXML
    public void btNextMonth_onAction(ActionEvent event) {
        for (int i = 0; i < 30 /*&& !season.today.equals(season.finalDate)"*/; i++) {
            season.oneMoreDay();
            updateAll();
        }
        if (season.today.equals(season.finalDate)) {
            //btNextDay.setDisable(true);
            //btNextMonth.setDisable(true);
        }
    }

    public void updateAll() {
        lbDate.setText((new SimpleDateFormat("dd/MM/yy")).format(season.today));

        if (ControllerUtils.walletScenario != null) ControllerUtils.walletScenario.updateAll();
        if (ControllerUtils.transactionsScenario != null) ControllerUtils.transactionsScenario.updateAll();
        if (ControllerUtils.realTimeScenario != null) ControllerUtils.realTimeScenario.updateAll();

    }



}
