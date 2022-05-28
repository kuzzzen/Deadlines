package fit.biepjv.deadlines;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class DeadlinesApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        System.out.println(System.getProperty("javafx.runtime.version"));
        String css = Objects.requireNonNull(this.getClass().getResource("main.css")).toExternalForm();
        FXMLLoader fxmlLoader = new FXMLLoader(DeadlinesApp.class.getResource("main.fxml"));
        Font.loadFont(Objects.requireNonNull(DeadlinesApp.class.getResource("gogh.otf")).toExternalForm(), 10);
        Scene scene = new Scene(fxmlLoader.load(), 920, 640);
        scene.getStylesheets().add(css);
        stage.setTitle("TodoList");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}