package com.decssoft.languagemanager.utils;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;

/**
 *
 * @author alan
 */
public class Language {

    private static final ObjectProperty<ResourceBundle> bundleProperty
            = new SimpleObjectProperty<>(ResourceBundle.getBundle("language", Locale.ENGLISH));

    public static ResourceBundle getBundle() {
        return bundleProperty.get();
    }

    public static ObjectProperty<ResourceBundle> bundleProperty() {
        return bundleProperty;
    }

    public static StringBinding bind(String key) {
        return Bindings.createStringBinding(() -> {
            ResourceBundle bundle = bundleProperty.get();
            if (bundle.containsKey(key)) {
                return bundle.getString(key);
            } else {
                return key;
            }
        }, bundleProperty);
    }

    public static void setLocale(Locale locale) {
        bundleProperty.set(ResourceBundle.getBundle("language", locale));
    }

    // Helper methods for your buttons
    public static void espanol() {
        setLocale(Locale.forLanguageTag("es"));
    }

    public static void english() {
        setLocale(Locale.ENGLISH);
    }

    public static void autoBind(Object controller) {
        var fields = controller.getClass().getDeclaredFields();
        for (var field : fields) {
            if (!field.isAnnotationPresent(FXML.class)) continue;
            if (field.isAnnotationPresent(IgnoreBind.class)) continue; // Skip ignored fields
            field.setAccessible(true);
            try {
                Object control = field.get(controller);
                if (control instanceof Labeled labeled) {
                    String key = field.getName(); // fx:id assumed as key
                    labeled.textProperty().bind(Language.bind(key));
                } else if (control instanceof TextInputControl input) {
                    String key = field.getName();
                    input.promptTextProperty().bind(Language.bind(key + ".prompt"));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
