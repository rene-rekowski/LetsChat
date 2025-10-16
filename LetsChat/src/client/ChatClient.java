package client;

import client.controller.ChatController;
import client.model.ChatModel;
import client.view.ChatView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ChatClient extends Application {
    @Override
    public void start(Stage stage) {
        ChatModel model = new ChatModel();

        String username = "User"; // optional Dialog
        String serverIP = "192.168.178.22";
        int serverPort = 5560;

        ChatController controller = new ChatController(model, serverIP, serverPort, username);
        try {
            controller.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ChatView view = new ChatView(model, controller);
        view.start(stage);

        stage.setOnCloseRequest(e -> {
            controller.disconnect();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
