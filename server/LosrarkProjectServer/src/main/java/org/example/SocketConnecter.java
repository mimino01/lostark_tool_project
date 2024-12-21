package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class SocketConnecter {
    private static ConcurrentHashMap<Socket, PrintWriter> clientMap = new ConcurrentHashMap<>();

    public void socketConnect() {
        try (ServerSocket serverSocket = new ServerSocket(8112)) {
            System.out.println("서버 대기중 ~~");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("클라이언트 연결됨: " + socket.getInetAddress());

                new Thread(() -> handleClient(socket)).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            clientMap.put(clientSocket, writer);

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String input;
            while ((input = reader.readLine()) != null) {
                System.out.println(input);
                writer.println(input);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            clientMap.remove(clientSocket);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void socket() {
        socketConnect();
    }
}
