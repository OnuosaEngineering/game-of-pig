package edu.wccnet.ionuosa.piggame.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UIStyles {
    
    public static final Color[] THEME_COLORS = {
        Color.rgb(89, 50, 89),
        Color.rgb(144, 79, 89),
        Color.rgb(217, 102, 89),
        Color.rgb(252, 163, 89),
        Color.rgb(255, 214, 89)
    };
    
    public static final String[] DICE_FACES = {
        "⚀", "⚁", "⚂", "⚃", "⚄", "⚅"
    };
    
    public static final int[] ANIMATION_DELAYS = {120, 80, 150, 60, 100, 90};
    
    public static Background createBackground(int colorIndex) {
        return new Background(new BackgroundFill(
                THEME_COLORS[colorIndex], CornerRadii.EMPTY, Insets.EMPTY));
    }
    
    public static Button createStyledButton(String text, int colorIndex) {
        Button button = new Button(text);
        button.setPrefSize(180, 50);
        button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        
        Color color = THEME_COLORS[colorIndex];
        
        button.setStyle(
                "-fx-background-color: #" + color.toString().substring(2, 8) + ";" +
                "-fx-text-fill: white;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;"
        );
        
        button.setOnMouseEntered(e -> 
            button.setEffect(new DropShadow(15, color.darker()))
        );
        button.setOnMouseExited(e -> 
            button.setEffect(null)
        );
        
        return button;
    }
    
    public static Border createBorder(int colorIndex) {
        return new Border(new BorderStroke(
                THEME_COLORS[colorIndex], BorderStrokeStyle.SOLID, 
                new CornerRadii(5), new BorderWidths(3)));
    }
    
    public static Font createFont(double size, boolean bold) {
        return Font.font("Verdana", bold ? FontWeight.BOLD : FontWeight.NORMAL, size);
    }
    
    public static String getDieFace(int roll) {
        if (roll < 1 || roll > 6) {
            return "?";
        }
        return DICE_FACES[roll - 1];
    }
    
    public static Insets createPadding(double size) {
        return new Insets(size);
    }
}
