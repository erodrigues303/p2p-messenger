package com.example.p2pmessenger;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    @FXML
    private TextField messageField;
    @FXML
    private ListView<String> messageList;
    @FXML
    private Button sendButton;
    @FXML
    public void initialize() {
        sendButton.setOnAction(event -> {
            String message = messageField.getText();
            messageList.getItems().add("You: " + message);
            messageField.clear();
        });
    }
}
