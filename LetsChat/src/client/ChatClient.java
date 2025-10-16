package client;

import client.controller.ChatController;
import client.model.ChatModel;
import client.view.ChatView;
import client.view.LoginView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ChatClient extends Application {
    @Override
    public void start(Stage stage) {
        // LoginView anzeigen
        LoginView loginView = new LoginView();
        String username = loginView.showLogin(new Stage()).orElse("User");

        ChatModel model = new ChatModel();
        String serverIP = "192.168.178.22";
        int serverPort = 5565;

        ChatController controller = new ChatController(model, serverIP, serverPort, username);
        try {
            controller.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ChatView chatView = new ChatView(model, controller);
        chatView.start(stage);

        stage.setOnCloseRequest(e -> {
            controller.disconnect();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
