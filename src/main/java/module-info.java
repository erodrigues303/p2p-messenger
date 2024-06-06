module com.example.p2pmessenger {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.example.p2pmessenger to javafx.fxml;
    exports com.example.p2pmessenger;
}