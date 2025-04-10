package com.decssoft.languagemanager.views;

import com.decssoft.languagemanager.utils.IgnoreBind;
import com.decssoft.languagemanager.utils.Language;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author alan
 */
public class MainController {

    @FXML
    private Label language;

    @FXML
    @IgnoreBind
    private Label nonbind;

    @FXML
    private Button english, spanish;

    @FXML
    private void initialize() {
        Language.autoBind(this);
    }

    @FXML
    private void espanol() {
        Language.espanol();
    }

    @FXML
    private void english() {
        Language.english();
    }

}
