package Sever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatSever {

	public static void main(String[] agrs) throws IOException {
		ServerSocket severSocket = new ServerSocket(5555);
		System.out.println("Sever gestartet. wartet auf Client...");

		Socket client1 = severSocket.accept();
		System.out.print("Client 1 connectet");
		Socket client2 = severSocket.accept();
		System.out.println("Client 2 connect");

		// streams
		BufferedReader in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
		PrintWriter out1 = new PrintWriter(client1.getOutputStream(), true);

		BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
		PrintWriter out2 = new PrintWriter(client2.getOutputStream(), true);

		// masage
		Thread t1 = new Thread(() -> {
			try {
				String msg;
				while ((msg = in1.readLine()) != null) {
					out2.println("Clinet 1: " + msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Thread t2 = new Thread(() -> {
			try {
				String msg;
				while ((msg = in2.readLine()) != null) {
					out1.println("Client 2: " + msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		t1.start();
		t2.start();

	}

}
