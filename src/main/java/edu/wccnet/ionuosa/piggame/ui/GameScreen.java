package edu.wccnet.ionuosa.piggame.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import edu.wccnet.ionuosa.piggame.GameHistoryService;
import edu.wccnet.ionuosa.piggame.model.GameModel;
import edu.wccnet.ionuosa.piggame.model.GameRecord;

import javax.swing.Timer;
import java.time.LocalDateTime;

public class GameScreen {
    private final Stage stage;
    private final GameHistoryService historyService;
    private final GameModel gameModel;
    
    private Label player1ScoreLabel;
    private Label player2ScoreLabel;
    private Label turnScoreLabel;
    private Label statusLabel;
    private Button rollButton;
    private Button holdButton;
    private BorderPane mainLayout;
    private VBox diceDisplay;
    
    public GameScreen(Stage stage, GameHistoryService historyService) {
        this.stage = stage;
        this.historyService = historyService;
        this.gameModel = new GameModel();
    }
    
    public void configureGame(String player1Name, String player2Name, boolean isComputerOpponent) {
        gameModel.configureGame(player1Name, player2Name, isComputerOpponent);
    }
    
    public void show() {
        mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setBackground(new Background(new BackgroundFill(
                Color.rgb(40, 40, 45), CornerRadii.EMPTY, Insets.EMPTY)));
        
        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        
        Text gameTitle = new Text("Game of Pig");
        gameTitle.setFont(UIStyles.createFont(32, true));
        gameTitle.setFill(UIStyles.THEME_COLORS[4]);
        
        statusLabel = new Label("Game started! " + gameModel.getPlayer1Name() + "'s turn");
        statusLabel.setFont(UIStyles.createFont(16, true));
        statusLabel.setTextFill(Color.WHITE);
        
        topBox.getChildren().addAll(gameTitle, statusLabel);
        mainLayout.setTop(topBox);
        
        VBox centerBox = new VBox(30);
        centerBox.setAlignment(Pos.CENTER);
        
        diceDisplay = new VBox(15);
        diceDisplay.setAlignment(Pos.CENTER);
        
        Text diceText = new Text(UIStyles.DICE_FACES[0]);
        diceText.setFont(UIStyles.createFont(100, false));
        diceText.setFill(UIStyles.THEME_COLORS[3]);
        
        turnScoreLabel = new Label("Current Turn: 0");
        turnScoreLabel.setFont(UIStyles.createFont(18, false));
        turnScoreLabel.setTextFill(Color.WHITE);
        
        diceDisplay.getChildren().addAll(diceText, turnScoreLabel);
        
        HBox controlsBox = new HBox(20);
        controlsBox.setAlignment(Pos.CENTER);
        
        rollButton = UIStyles.createStyledButton("Roll Dice", 3);
        holdButton = UIStyles.createStyledButton("Hold", 2);
        Button menuButton = UIStyles.createStyledButton("Main Menu", 1);
        
        controlsBox.getChildren().addAll(rollButton, holdButton, menuButton);
        
        centerBox.getChildren().addAll(diceDisplay, controlsBox);
        mainLayout.setCenter(centerBox);
        
        VBox player1Box = createPlayerScoreBox(gameModel.getPlayer1Name(), true);
        VBox player2Box = createPlayerScoreBox(gameModel.getPlayer2Name(), false);
        
        mainLayout.setLeft(player1Box);
        mainLayout.setRight(player2Box);
        
        rollButton.setOnAction(e -> rollDice());
        holdButton.setOnAction(e -> holdTurn());
        menuButton.setOnAction(e -> {
            WelcomeScreen welcomeScreen = new WelcomeScreen(stage, historyService);
            welcomeScreen.show();
        });
        
        Scene gameScene = new Scene(mainLayout, 800, 600);
        stage.setTitle("Game of Pig");
        stage.setScene(gameScene);
        stage.show();
        
        if (gameModel.isComputerOpponent() && !gameModel.isPlayer1Turn()) {
            playComputerTurn();
        }
    }
    
    private VBox createPlayerScoreBox(String playerName, boolean isPlayer1) {
        VBox playerBox = new VBox(15);
        playerBox.setAlignment(Pos.CENTER);
        playerBox.setPadding(new Insets(20));
        playerBox.setMinWidth(150);
        
        if ((isPlayer1 && gameModel.isPlayer1Turn()) || (!isPlayer1 && !gameModel.isPlayer1Turn())) {
            playerBox.setBorder(UIStyles.createBorder(4));
        }
        
        Text nameText = new Text(playerName);
        nameText.setFont(UIStyles.createFont(18, true));
        nameText.setFill(Color.WHITE);
        
        Label scoreLabel = new Label("Score: " + (isPlayer1 ? gameModel.getPlayer1Score() : gameModel.getPlayer2Score()));
        scoreLabel.setFont(UIStyles.createFont(22, false));
        scoreLabel.setTextFill(UIStyles.THEME_COLORS[3]);
        
        if (isPlayer1) {
            player1ScoreLabel = scoreLabel;
        } else {
            player2ScoreLabel = scoreLabel;
        }
        
        Circle avatar = new Circle(40);
        avatar.setFill(isPlayer1 ? UIStyles.THEME_COLORS[1] : UIStyles.THEME_COLORS[2]);
        
        Text initial = new Text(playerName.substring(0, 1).toUpperCase());
        initial.setFont(UIStyles.createFont(36, true));
        initial.setFill(Color.WHITE);
        
        StackPane avatarPane = new StackPane(avatar, initial);
        
        playerBox.getChildren().addAll(avatarPane, nameText, scoreLabel);
        return playerBox;
    }
    
    private void rollDice() {
        rollButton.setDisable(true);
        holdButton.setDisable(true);
    
        animateDiceRoll();
    }
    
    private void animateDiceRoll() {
        final int[] frameCount = {0};
        final int totalFrames = 10;
        
        Text diceText = (Text) diceDisplay.getChildren().get(0);
        
        Timer timer = new Timer(50, e -> {
            if (frameCount[0] < totalFrames) {
                int randomIndex = (int) (Math.random() * 6);
                diceText.setText(UIStyles.DICE_FACES[randomIndex]);
                diceText.setFill(UIStyles.THEME_COLORS[frameCount[0] % UIStyles.THEME_COLORS.length]);
                
                frameCount[0]++;
            } else {
                ((Timer)e.getSource()).stop();
                
                int roll = gameModel.rollDice();
                diceText.setText(UIStyles.getDieFace(roll));
                diceText.setFill(roll == 1 ? Color.RED : UIStyles.THEME_COLORS[3]);
                
                // Make sure game is not incorrectly marked as over
                gameModel.setGameOver(false);
                
                processRollResult(roll);
            }
        });
        
        timer.start();
    }
    
    private void processRollResult(int roll) {
        boolean continueTurn = gameModel.processRoll(roll);
        
        // Update display elements
        turnScoreLabel.setText("Current Turn: " + gameModel.getCurrentTurnScore());
        updatePlayerScoreLabels();
        
        // Make absolutely sure game is not incorrectly marked as over when rolling a 1
        if (roll == 1) {
            gameModel.setGameOver(false);
        }
        
        if (continueTurn) {
            // Turn continues - player rolled 2-6
            statusLabel.setText(gameModel.getCurrentPlayerName() + " rolled a " + roll + 
                              ". Roll again or hold?");
            
            // Re-enable buttons
            rollButton.setDisable(false);
            holdButton.setDisable(false);
            
            if (gameModel.isComputerOpponent() && !gameModel.isPlayer1Turn()) {
                Timer timer = new Timer(1000, e -> {
                    ((Timer)e.getSource()).stop();
                    decideComputerMove();
                });
                timer.setRepeats(false);
                timer.start();
            }
        } else {
            // Turn ends - player rolled 1
            String currentPlayerName = gameModel.isPlayer1Turn() ? gameModel.getPlayer2Name() : gameModel.getPlayer1Name();
            String nextPlayerName = gameModel.isPlayer1Turn() ? gameModel.getPlayer1Name() : gameModel.getPlayer2Name();
            statusLabel.setText(currentPlayerName + " rolled a 1! Turn over. " + nextPlayerName + "'s turn");
            
            // Update player boxes to show the active player
            updatePlayerBoxes();
            
            // Enable buttons after a small delay
            Timer enableButtonsTimer = new Timer(500, e -> {
                ((Timer)e.getSource()).stop();
                if (!gameModel.isGameOver()) {
                    rollButton.setDisable(false);
                    holdButton.setDisable(false);
                }
            });
            enableButtonsTimer.setRepeats(false);
            enableButtonsTimer.start();
            
            // If computer's turn, play after a delay
            if (gameModel.isComputerOpponent() && !gameModel.isPlayer1Turn() && !gameModel.isGameOver()) {
                Timer timer = new Timer(1500, e -> {
                    ((Timer)e.getSource()).stop();
                    playComputerTurn();
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
    
    private void updatePlayerScoreLabels() {
        player1ScoreLabel.setText("Score: " + gameModel.getPlayer1Score());
        player2ScoreLabel.setText("Score: " + gameModel.getPlayer2Score());
    }
    
    private void holdTurn() {
        boolean gameWon = gameModel.holdTurn();
        
        updatePlayerScoreLabels();
        turnScoreLabel.setText("Current Turn: 0");
        
        if (gameWon) {
            endGame(!gameModel.isPlayer1Turn());
        } else {
            String currentPlayerName = gameModel.isPlayer1Turn() ? gameModel.getPlayer2Name() : gameModel.getPlayer1Name();
            int currentScore = gameModel.isPlayer1Turn() ? gameModel.getPlayer2Score() : gameModel.getPlayer1Score();
            statusLabel.setText(currentPlayerName + " holds with " + currentScore + 
                              " points. " + gameModel.getCurrentPlayerName() + "'s turn");
            
            updatePlayerBoxes();
            
            // Enable buttons for next player
            rollButton.setDisable(false);
            holdButton.setDisable(false);
            
            if (gameModel.isComputerOpponent() && !gameModel.isPlayer1Turn() && !gameModel.isGameOver()) {
                Timer timer = new Timer(1000, e -> {
                    ((Timer)e.getSource()).stop();
                    playComputerTurn();
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
    
    private void updatePlayerBoxes() {
        mainLayout.setLeft(createPlayerScoreBox(gameModel.getPlayer1Name(), true));
        mainLayout.setRight(createPlayerScoreBox(gameModel.getPlayer2Name(), false));
    }
    
    private void playComputerTurn() {
        statusLabel.setText(gameModel.getPlayer2Name() + "'s turn (thinking)...");
        rollButton.setDisable(true);
        holdButton.setDisable(true);
        
        Timer timer = new Timer(800, e -> {
            ((Timer)e.getSource()).stop();
            rollDice();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void decideComputerMove() {
        if (gameModel.shouldComputerHold()) {
            holdTurn();
        } else {
            playComputerTurn();
        }
    }
    
    private void endGame(boolean player1Won) {
        String winnerName = player1Won ? gameModel.getPlayer1Name() : gameModel.getPlayer2Name();
        int winnerScore = player1Won ? gameModel.getPlayer1Score() : gameModel.getPlayer2Score();
        statusLabel.setText("Game Over! " + winnerName + " wins with " + winnerScore + " points!");
        
        rollButton.setDisable(true);
        holdButton.setDisable(true);
        
        String gameType = gameModel.isComputerOpponent() ? 
                (player1Won ? "Human vs Computer" : "Computer vs Human") :
                "Human vs Human";
        
        GameRecord record = new GameRecord(
            winnerName,
            winnerScore,
            LocalDateTime.now(),
            gameType
        );
        historyService.addGameRecord(record);
        
        showWinnerDialog(player1Won);
    }
    
    private void showWinnerDialog(boolean player1Won) {
        WinnerDialog winnerDialog = new WinnerDialog(
                stage,
                player1Won ? gameModel.getPlayer1Name() : gameModel.getPlayer2Name(),
                player1Won ? gameModel.getPlayer1Score() : gameModel.getPlayer2Score());
        
        winnerDialog.setOnPlayAgain(() -> {
            gameModel.resetGame();
            show();
        });
        
        winnerDialog.setOnMainMenu(() -> {
            WelcomeScreen welcomeScreen = new WelcomeScreen(stage, historyService);
            welcomeScreen.show();
        });
        
        winnerDialog.show();
    }
}
