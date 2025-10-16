package client.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginView {
    private String username;

    public Optional<String> showLogin(Stage stage) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(300, 150);

        Label label = new Label("Gib deinen Benutzernamen ein:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Benutzername");

        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            if (!usernameField.getText().isBlank()) {
                username = usernameField.getText().trim();
                stage.close(); // Fenster schließen und zurück zum Main starten
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte gib einen Benutzernamen ein!");
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(label, usernameField, loginButton);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.showAndWait();

        return Optional.ofNullable(username);
    }
}
