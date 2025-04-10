package com.decssoft.languagemanager;

import com.decssoft.languagemanager.utils.Language;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LanguageManager extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        var fxml = new FXMLLoader(getClass().getResource("/com/decssoft/languagemanager/views/Main.fxml"),
                Language.getBundle());
        var scene = new Scene(fxml.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}
