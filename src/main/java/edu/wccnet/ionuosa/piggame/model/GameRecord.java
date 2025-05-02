package edu.wccnet.ionuosa.piggame.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String winner;
    private final int score;
    private final LocalDateTime dateTime;
    private final String gameType;
    
    public GameRecord(String winner, int score, LocalDateTime dateTime, String gameType) {
        this.winner = winner;
        this.score = score;
        this.dateTime = dateTime;
        this.gameType = gameType;
    }
    
    public String getWinner() {
        return winner;
    }
    
    public int getScore() {
        return score;
    }
    
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    public String getFormattedDateTime() {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    public String getGameType() {
        return gameType;
    }
    
    @Override
    public String toString() {
        return "GameRecord{" +
                "winner='" + winner + '\'' +
                ", score=" + score +
                ", dateTime=" + getFormattedDateTime() +
                ", gameType='" + gameType + '\'' +
                '}';
    }
}
