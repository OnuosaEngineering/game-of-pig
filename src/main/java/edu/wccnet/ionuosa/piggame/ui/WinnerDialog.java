package edu.wccnet.ionuosa.piggame.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WinnerDialog {
    private final Stage parentStage;
    private final String winnerName;
    private final int winnerScore;
    private Runnable onPlayAgain;
    private Runnable onMainMenu;
    
    public WinnerDialog(Stage parentStage, String winnerName, int winnerScore) {
        this.parentStage = parentStage;
        this.winnerName = winnerName;
        this.winnerScore = winnerScore;
    }
    
    public void setOnPlayAgain(Runnable onPlayAgain) {
        this.onPlayAgain = onPlayAgain;
    }
    
    public void setOnMainMenu(Runnable onMainMenu) {
        this.onMainMenu = onMainMenu;
    }
    
    public void show() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("Game Over");
        
        VBox dialogBox = new VBox(20);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setPadding(new Insets(30));
        dialogBox.setBackground(UIStyles.createBackground(0));
        
        Text congratsText = new Text("🎉 CONGRATULATIONS! 🎉");
        congratsText.setFont(UIStyles.createFont(24, true));
        congratsText.setFill(Color.WHITE);
        
        Text winnerText = new Text(winnerName + " wins!");
        winnerText.setFont(UIStyles.createFont(20, false));
        winnerText.setFill(UIStyles.THEME_COLORS[4]);
        
        Text scoreText = new Text("Final Score: " + winnerScore);
        scoreText.setFont(UIStyles.createFont(16, false));
        scoreText.setFill(Color.WHITE);
        
        Button playAgainBtn = UIStyles.createStyledButton("Play Again", 3);
        Button mainMenuBtn = UIStyles.createStyledButton("Main Menu", 2);
        
        playAgainBtn.setOnAction(e -> {
            dialogStage.close();
            if (onPlayAgain != null) {
                onPlayAgain.run();
            }
        });
        
        mainMenuBtn.setOnAction(e -> {
            dialogStage.close();
            if (onMainMenu != null) {
                onMainMenu.run();
            }
        });
        
        dialogBox.getChildren().addAll(
                congratsText, winnerText, scoreText,
                playAgainBtn, mainMenuBtn);
        
        Scene dialogScene = new Scene(dialogBox, 400, 300);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }
}
