package com.example.p2pmessenger;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea messageArea;
    @FXML
    private TextField inputField;

    private List<Socket> peers = new ArrayList<>();
    private String userName;

    @FXML
    public void connect() {
        String ip = ipField.getText();
        int port = Integer.parseInt(portField.getText());
        userName = nameField.getText();
        connectToBootstrapNode(ip, port);
    }

    private void connectToBootstrapNode(String ip, int port) {
        try (Socket bootstrapSocket = new Socket(ip, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(bootstrapSocket.getInputStream()));
             PrintWriter out = new PrintWriter(bootstrapSocket.getOutputStream(), true)) {

            out.println("JOIN " + InetAddress.getLocalHost().getHostAddress());
            String response;
            while ((response = in.readLine()) != null) {
                connectToPeer(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToPeer(String peerAddress) {
        try {
            String[] parts = peerAddress.split(":");
            String ip = parts[0];
            int port = Integer.parseInt(parts[1]);
            Socket peerSocket = new Socket(ip, port);
            peers.add(peerSocket);
            new Thread(() -> handlePeerCommunication(peerSocket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePeerCommunication(Socket peerSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(peerSocket.getInputStream()));
             PrintWriter out = new PrintWriter(peerSocket.getOutputStream(), true)) {

            String message;
            while ((message = in.readLine()) != null) {
                messageArea.appendText(message + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sendMessage() {
        String message = inputField.getText();
        inputField.clear();
        messageArea.appendText("Me: " + message + "\n");
        broadcastMessage(userName + ": " + message);
    }

    private void broadcastMessage(String message) {
        for (Socket peer : peers) {
            try (PrintWriter out = new PrintWriter(peer.getOutputStream(), true)) {
                out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}