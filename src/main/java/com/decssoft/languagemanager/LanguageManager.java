package com.decssoft.languagemanager;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LanguageManager extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        log.info("loaded");
    }

    public static void main(String args[]) {
        launch(args);
    }
}
