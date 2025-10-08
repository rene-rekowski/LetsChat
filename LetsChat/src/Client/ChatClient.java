package Client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatClient extends Application {
	private PrintWriter out;
	private BufferedReader in;
	private TextArea chatArea;
	private TextField inputField;

	@Override
	public void start(Stage primaryStage) throws Exception {
		chatArea = new TextArea();
		chatArea.setEditable(false);
		inputField = new TextField();
		
		inputField.setOnAction(e ->{
			String msg = inputField.getText();
			out.println(msg);
			inputField.clear();
		});
		
		VBox root = new VBox(10,chatArea , inputField);
		primaryStage.setScene(new Scene(root,600,400));
		primaryStage.setTitle("LetsChat");
		primaryStage.show();
		
		new Thread(this::setupConnection).start();
	}
	
	private void setupConnection(){
		try {
			Socket socket = new Socket("192.168.178.54", 5555);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String msg;
			while ((msg = in.readLine()) != null) {
				String finalMsg = msg;
				javafx.application.Platform.runLater(()-> chatArea.appendText(finalMsg + "\n"));
			}
		}catch(IOException e) {
			e.printStackTrace();			
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
