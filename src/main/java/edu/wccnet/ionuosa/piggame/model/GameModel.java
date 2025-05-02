package edu.wccnet.ionuosa.piggame.model;

import java.util.concurrent.ThreadLocalRandom;

public class GameModel {
    public static final int WINNING_SCORE = 100;
    
    private int player1Score;
    private int player2Score;
    private int currentTurnScore;
    private boolean isPlayer1Turn;
    private boolean isComputerOpponent;
    private int lastRoll;
    private boolean gameOver;
    private String player1Name;
    private String player2Name;
    
    public GameModel() {
        resetGame();
    }
    
    public void resetGame() {
        player1Score = 0;
        player2Score = 0;
        currentTurnScore = 0;
        isPlayer1Turn = true;
        gameOver = false;
        lastRoll = 0;
    }
    
    public void configureGame(String player1Name, String player2Name, boolean isComputerOpponent) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.isComputerOpponent = isComputerOpponent;
        resetGame();
    }
    
    public int rollDice() {
        lastRoll = ThreadLocalRandom.current().nextInt(1, 7);
        return lastRoll;
    }
    
    public boolean processRoll(int roll) {
        if (roll == 1) {
            currentTurnScore = 0;
            switchTurns();
            return false;
        } else {
            currentTurnScore += roll;
            return true;
        }
    }
    
    public boolean holdTurn() {
        if (isPlayer1Turn) {
            player1Score += currentTurnScore;
            
            if (player1Score >= WINNING_SCORE) {
                gameOver = true;
                return true;
            }
        } else {
            player2Score += currentTurnScore;
            
            if (player2Score >= WINNING_SCORE) {
                gameOver = true;
                return true;
            }
        }
        
        currentTurnScore = 0;
        switchTurns();
        return false;
    }
    
    private void switchTurns() {
        isPlayer1Turn = !isPlayer1Turn;
    }
    
    public boolean shouldComputerHold() {
        if (player2Score + currentTurnScore >= WINNING_SCORE) {
            return true;
        } else if (currentTurnScore >= 20) {
            return true;
        } else if (player1Score > player2Score + 25) {
            return currentTurnScore >= 15;
        } else if (player2Score > player1Score + 15) {
            return currentTurnScore >= 10;
        } else if (lastRoll <= 2) {
            return currentTurnScore >= 12;
        } else {
            int riskFactor = ThreadLocalRandom.current().nextInt(8, 18);
            return currentTurnScore >= riskFactor;
        }
    }
    
    
    public int getPlayer1Score() {
        return player1Score;
    }
    
    public int getPlayer2Score() {
        return player2Score;
    }
    
    public int getCurrentTurnScore() {
        return currentTurnScore;
    }
    
    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }
    
    public boolean isComputerOpponent() {
        return isComputerOpponent;
    }
    
    public int getLastRoll() {
        return lastRoll;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public String getPlayer1Name() {
        return player1Name;
    }
    
    public String getPlayer2Name() {
        return player2Name;
    }
    
    public String getCurrentPlayerName() {
        return isPlayer1Turn ? player1Name : player2Name;
    }
    
    public int getCurrentPlayerScore() {
        return isPlayer1Turn ? player1Score : player2Score;
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
