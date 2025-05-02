package edu.wccnet.ionuosa.piggame;

import javafx.application.Application;
import javafx.stage.Stage;
import edu.wccnet.ionuosa.piggame.ui.WelcomeScreen;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game of Pig");
        GameHistoryService historyService = new GameHistoryService();
        historyService.loadGameHistory();
        WelcomeScreen welcomeScreen = new WelcomeScreen(primaryStage, historyService);
        welcomeScreen.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
