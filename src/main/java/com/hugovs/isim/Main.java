package com.hugovs.isim;

import com.hugovs.isim.controller.ToolbarScenario;
import com.hugovs.isim.model.Season;
import com.hugovs.isim.model.client.Client;
import com.hugovs.isim.model.exchange.Database;
import com.hugovs.isim.model.ia.SmartInvestor;
import com.hugovs.isim.model.ia.investors.*;
import fxscenario.Spawner;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Client client = new Client(100000);

        InvestorHolder.ze = new ZeInvestor(client, Database.ALL_2014_2015);
        InvestorHolder.yuri = new YuriInvestor(client, Database.ALL_2014_2015);
        InvestorHolder.kanda = new KandaInvestor(client, Database.ALL_2014_2015);
        InvestorHolder.oracle = new OracleInvestor(client, Database.ALL_2014_2015.merge(Database.ALL_2016));

        SmartInvestor investor = InvestorHolder.ze;

        Season season = new Season(investor);

        ToolbarScenario toolbarScenario = new ToolbarScenario(season);
        Spawner.startScenario(toolbarScenario, null, primaryStage);

    }

}
