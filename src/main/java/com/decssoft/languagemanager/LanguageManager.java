package com.decssoft.languagemanager;

import com.decssoft.languagemanager.utils.Language;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LanguageManager extends Application {

    public static ResourceBundle bundle;
    public static Locale locale;

    @Override
    public void init() throws Exception {
        super.init();
        locale = Locale.getDefault();
        ResourceBundle.Control control = new ResourceBundle.Control() {

            @Override
            public Locale getFallbackLocale(String baseName, Locale locale) {
                LanguageManager.this.locale = Locale.ENGLISH;
                return LanguageManager.this.locale;
            }
        };
        bundle = ResourceBundle.getBundle("language", this.locale, control);
    }

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
