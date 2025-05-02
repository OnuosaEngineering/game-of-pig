package edu.wccnet.ionuosa.piggame.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import edu.wccnet.ionuosa.piggame.GameHistoryService;
import edu.wccnet.ionuosa.piggame.model.GameRecord;

import java.time.format.DateTimeFormatter;
import java.util.Map;

public class HistoryScreen {
    private final Stage stage;
    private final GameHistoryService historyService;
    
    public HistoryScreen(Stage stage, GameHistoryService historyService) {
        this.stage = stage;
        this.historyService = historyService;
    }
    
    public void show() {
        VBox historyLayout = new VBox(20);
        historyLayout.setPadding(new Insets(30));
        historyLayout.setBackground(UIStyles.createBackground(0));
        
        Text headerText = new Text("Game History");
        headerText.setFont(UIStyles.createFont(28, true));
        headerText.setFill(Color.WHITE);
        
        Text statsHeader = new Text("Player Statistics");
        statsHeader.setFont(UIStyles.createFont(18, true));
        statsHeader.setFill(UIStyles.THEME_COLORS[4]);
        
        Map<String, Long> playerWinCounts = historyService.getPlayerWinCounts();
        
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(15);
        statsGrid.setVgap(10);
        statsGrid.setPadding(new Insets(10));
        statsGrid.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 5;");
        
        Text playerHeader = new Text("Player");
        playerHeader.setFont(UIStyles.createFont(14, true));
        playerHeader.setFill(Color.WHITE);
        
        Text winsHeader = new Text("Wins");
        winsHeader.setFont(UIStyles.createFont(14, true));
        winsHeader.setFill(Color.WHITE);
        
        statsGrid.add(playerHeader, 0, 0);
        statsGrid.add(winsHeader, 1, 0);
        
        int row = 1;
        for (Map.Entry<String, Long> entry : playerWinCounts.entrySet()) {
            Text playerText = new Text(entry.getKey());
            playerText.setFill(Color.WHITE);
            
            Text winsText = new Text(entry.getValue().toString());
            winsText.setFill(UIStyles.THEME_COLORS[3]);
            
            statsGrid.add(playerText, 0, row);
            statsGrid.add(winsText, 1, row);
            row++;
        }
        
        if (historyService.getGamesPlayedCount() == 0) {
            Text noGamesText = new Text("No games played yet.");
            noGamesText.setFill(Color.WHITE);
            statsGrid.add(noGamesText, 0, 1, 2, 1);
        }
        
        if (historyService.getGamesPlayedCount() > 0) {
            Text highScoreHeader = new Text("Highest Score");
            highScoreHeader.setFont(UIStyles.createFont(14, true));
            highScoreHeader.setFill(Color.WHITE);
            
            Text avgScoreHeader = new Text("Average Score");
            avgScoreHeader.setFont(UIStyles.createFont(14, true));
            avgScoreHeader.setFill(Color.WHITE);
            
            Text highScoreText = new Text(String.valueOf(historyService.getHighestScore()));
            highScoreText.setFill(UIStyles.THEME_COLORS[3]);
            
            Text avgScoreText = new Text(String.format("%.1f", historyService.getAverageWinningScore()));
            avgScoreText.setFill(UIStyles.THEME_COLORS[3]);
            
            statsGrid.add(highScoreHeader, 2, 0);
            statsGrid.add(highScoreText, 2, 1);
            statsGrid.add(avgScoreHeader, 3, 0);
            statsGrid.add(avgScoreText, 3, 1);
        }
        
        Text historyTableHeader = new Text("Detailed Game Records");
        historyTableHeader.setFont(UIStyles.createFont(18, true));
        historyTableHeader.setFill(UIStyles.THEME_COLORS[4]);
        
        TableView<GameRecord> historyTable = new TableView<>();
        historyTable.setMaxHeight(300);
        
        TableColumn<GameRecord, String> resultCol = new TableColumn<>("Winner");
        resultCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getWinner()));
        
        TableColumn<GameRecord, String> dateCol = new TableColumn<>("Date & Time");
        dateCol.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDateTime().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        
        TableColumn<GameRecord, String> pointsCol = new TableColumn<>("Score");
        pointsCol.setCellValueFactory(cell -> new SimpleStringProperty(
                String.valueOf(cell.getValue().getScore())));
        
        TableColumn<GameRecord, String> typeCol = new TableColumn<>("Game Type");
        typeCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGameType()));
        
        historyTable.getColumns().addAll(resultCol, dateCol, pointsCol, typeCol);
        
        resultCol.setSortable(true);
        dateCol.setSortable(true);
        pointsCol.setSortable(true);
        typeCol.setSortable(true);
        
        setCellFactory(resultCol);
        setCellFactory(dateCol);
        setCellFactory(pointsCol);
        setCellFactory(typeCol);
        
        ObservableList<GameRecord> data = FXCollections.observableArrayList(historyService.getAllGameRecords());
        historyTable.setItems(data);
        
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        Button backButton = UIStyles.createStyledButton("Back to Main Menu", 3);
        backButton.setOnAction(e -> {
            WelcomeScreen welcomeScreen = new WelcomeScreen(stage, historyService);
            welcomeScreen.show();
        });
        
        historyLayout.getChildren().addAll(
                headerText, 
                statsHeader, statsGrid,
                historyTableHeader, historyTable,
                backButton);
        
        Scene historyScene = new Scene(historyLayout, 600, 700);
        stage.setTitle("Game of Pig - History");
        stage.setScene(historyScene);
    }
    
    private <T> void setCellFactory(TableColumn<GameRecord, T> column) {
        column.setCellFactory(new Callback<>() {
            @Override
            public TableCell<GameRecord, T> call(TableColumn<GameRecord, T> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item.toString());
                            setTextFill(Color.WHITE);
                            setStyle("-fx-background-color: rgba(89, 50, 89, 0.7);");
                        }
                    }
                };
            }
        });
    }
}
