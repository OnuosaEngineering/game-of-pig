package edu.wccnet.ionuosa.piggame.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import edu.wccnet.ionuosa.piggame.GameHistoryService;

public class WelcomeScreen {
    private final Stage stage;
    private final GameHistoryService historyService;
    
    private boolean isComputerOpponent = true;
    
    public WelcomeScreen(Stage stage, GameHistoryService historyService) {
        this.stage = stage;
        this.historyService = historyService;
    }
    
    public void show() {
        VBox welcomeLayout = new VBox(20);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setPadding(new Insets(30));
        welcomeLayout.setBackground(UIStyles.createBackground(0));
        
        Text titleText = new Text("🎲 GAME OF PIG 🎲");
        titleText.setFont(UIStyles.createFont(36, true));
        titleText.setFill(Color.WHITE);
        
        Text subtitleText = new Text("First to 100 points wins!");
        subtitleText.setFont(UIStyles.createFont(18, false));
        subtitleText.setFill(Color.WHITE);
        
        Button newGameBtn = UIStyles.createStyledButton("Start New Game", 3);
        Button historyBtn = UIStyles.createStyledButton("Game History", 2);
        Button exitBtn = UIStyles.createStyledButton("Exit", 1);
        
        HBox gameModeBox = new HBox(15);
        gameModeBox.setAlignment(Pos.CENTER);
        
        ToggleGroup gameModeGroup = new ToggleGroup();
        RadioButton humanVsComputerRB = new RadioButton("Human vs Computer");
        humanVsComputerRB.setToggleGroup(gameModeGroup);
        humanVsComputerRB.setSelected(true);
        humanVsComputerRB.setTextFill(Color.WHITE);
        
        RadioButton humanVsHumanRB = new RadioButton("Human vs Human");
        humanVsHumanRB.setToggleGroup(gameModeGroup);
        humanVsHumanRB.setTextFill(Color.WHITE);
        
        gameModeBox.getChildren().addAll(humanVsComputerRB, humanVsHumanRB);
        
        GridPane playerInputGrid = new GridPane();
        playerInputGrid.setHgap(10);
        playerInputGrid.setVgap(10);
        playerInputGrid.setAlignment(Pos.CENTER);
        
        Label player1Label = new Label("Player 1 Name:");
        player1Label.setTextFill(Color.WHITE);
        TextField player1Input = new TextField("Player 1");
        
        Label player2Label = new Label("Player 2 Name:");
        player2Label.setTextFill(Color.WHITE);
        TextField player2Input = new TextField("Player 2");
        player2Input.setDisable(true);
        
        playerInputGrid.add(player1Label, 0, 0);
        playerInputGrid.add(player1Input, 1, 0);
        playerInputGrid.add(player2Label, 0, 1);
        playerInputGrid.add(player2Input, 1, 1);
        
        humanVsComputerRB.setOnAction(e -> {
            isComputerOpponent = true;
            player2Input.setText("Computer");
            player2Input.setDisable(true);
        });
        
        humanVsHumanRB.setOnAction(e -> {
            isComputerOpponent = false;
            player2Input.setText("Player 2");
            player2Input.setDisable(false);
        });
        
        newGameBtn.setOnAction(e -> {
            String player1Name = player1Input.getText().trim();
            if (player1Name.isEmpty()) player1Name = "Player 1";
            
            String player2Name = player2Input.getText().trim();
            if (player2Name.isEmpty()) player2Name = isComputerOpponent ? "Computer" : "Player 2";
            
            GameScreen gameScreen = new GameScreen(stage, historyService);
            gameScreen.configureGame(player1Name, player2Name, isComputerOpponent);
            gameScreen.show();
        });
        
        historyBtn.setOnAction(e -> {
            HistoryScreen historyScreen = new HistoryScreen(stage, historyService);
            historyScreen.show();
        });
        
        exitBtn.setOnAction(e -> stage.close());
        
        welcomeLayout.getChildren().addAll(
                titleText, subtitleText, 
                gameModeBox, playerInputGrid,
                newGameBtn, historyBtn, exitBtn);
        
        Scene welcomeScene = new Scene(welcomeLayout, 600, 500);
        stage.setTitle("Game of Pig");
        stage.setScene(welcomeScene);
        stage.show();
    }
}
