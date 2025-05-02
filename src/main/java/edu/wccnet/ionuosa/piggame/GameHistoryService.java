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
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GAME_HISTORY_FILE))) {
            Object readObject = ois.readObject();
            if (readObject instanceof List) {
                gameHistory = (List<GameRecord>) readObject;
            }
        } catch (FileNotFoundException e) {
            gameHistory = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game history: " + e.getMessage());
            gameHistory = new ArrayList<>();
        }
    }
    
    public void saveGameHistory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GAME_HISTORY_FILE))) {
            oos.writeObject(gameHistory);
        } catch (IOException e) {
            System.err.println("Error saving game history: " + e.getMessage());
        }
    }
    
    public void addGameRecord(GameRecord record) {
        gameHistory.add(record);
        saveGameHistory();
    }
    
    public List<GameRecord> getAllGameRecords() {
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
