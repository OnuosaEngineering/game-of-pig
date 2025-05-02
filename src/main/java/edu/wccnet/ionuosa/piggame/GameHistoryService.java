package edu.wccnet.ionuosa.piggame;

import edu.wccnet.ionuosa.piggame.model.GameRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameHistoryService {
    private static final String GAME_HISTORY_FILE = "pig_game_history.dat";
    private List<GameRecord> gameHistory = new ArrayList<>();
    
    public void loadGameHistory() {
        try {
            File historyFile = new File(GAME_HISTORY_FILE);
            System.out.println("Looking for history file at: " + historyFile.getAbsolutePath());
            System.out.println("File exists: " + historyFile.exists());
            
            if (historyFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(historyFile))) {
                    Object readObject = ois.readObject();
                    if (readObject instanceof List) {
                        gameHistory = (List<GameRecord>) readObject;
                        System.out.println("Loaded " + gameHistory.size() + " game records");
                    }
                }
            } else {
                System.out.println("No history file found, starting with empty history");
                gameHistory = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            System.out.println("History file not found, starting with empty history");
            gameHistory = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game history: " + e.getMessage());
            e.printStackTrace();
            gameHistory = new ArrayList<>();
        }
    }
    
    public void saveGameHistory() {
        try {
            File historyFile = new File(GAME_HISTORY_FILE);
            System.out.println("Saving " + gameHistory.size() + " game records to: " + historyFile.getAbsolutePath());
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(historyFile))) {
                oos.writeObject(gameHistory);
                System.out.println("Game history saved successfully");
            }
        } catch (IOException e) {
            System.err.println("Error saving game history: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void addGameRecord(GameRecord record) {
        System.out.println("Adding game record: " + record);
        gameHistory.add(record);
        saveGameHistory();
    }
    
    public List<GameRecord> getAllGameRecords() {
        System.out.println("Getting all game records: " + gameHistory.size() + " records");
        return new ArrayList<>(gameHistory);
    }
    
    public Map<String, Long> getPlayerWinCounts() {
        return gameHistory.stream()
                .collect(Collectors.groupingBy(GameRecord::getWinner, Collectors.counting()));
    }
    
    public int getGamesPlayedCount() {
        return gameHistory.size();
    }
    
    public int getHighestScore() {
        return gameHistory.stream()
                .mapToInt(GameRecord::getScore)
                .max()
                .orElse(0);
    }
    
    public double getAverageWinningScore() {
        return gameHistory.stream()
                .mapToInt(GameRecord::getScore)
                .average()
                .orElse(0);
    }
    
    public Map<String, Long> getHumanVsComputerWins() {
        return gameHistory.stream()
                .filter(record -> record.getGameType().contains("Computer"))
                .collect(Collectors.groupingBy(
                        record -> record.getGameType().startsWith("Computer") ? "Computer" : "Human",
                        Collectors.counting()));
    }
}
