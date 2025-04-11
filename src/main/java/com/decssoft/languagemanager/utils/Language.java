package com.decssoft.languagemanager.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to manage language settings and automatic UI binding for JavaFX
 * applications. This class allows setting the locale, dynamically switching
 * languages, and binding UI elements to corresponding language keys in the
 * resource bundle.
 */
@Slf4j
public class Language {

    private static ObjectProperty<ResourceBundle> bundleProperty;

    /**
     * Gets the current resource bundle being used for language translations.
     *
     * @return the current resource bundle.
     */
    public static ResourceBundle getBundle() throws MissingResourceException {
        System.out.println("------------------- getting bundle ---------------------");
        try {
            bundleProperty = new SimpleObjectProperty<>(ResourceBundle.getBundle("language", Locale.CHINA));
            System.out.println(bundleProperty.get().getLocale());
            return bundleProperty.get();
        } catch (MissingResourceException e) {
            log.warn("""
            Failed to load the properties file.
            Make sure a valid resource bundle exists in 'src/main/resources'.
            Returning null. UI may not display localized text.""");
            return null;
        }
    }

    /**
     * Gets the property object for the current resource bundle.
     *
     * @return the object property holding the resource bundle.
     */
    public static ObjectProperty<ResourceBundle> bundleProperty() {
        return bundleProperty;
    }

    /**
     * Binds a given field (via its ID) to the appropriate value in the current
     * resource bundle. This method returns a `StringBinding` that will
     * automatically update whenever the resource bundle is changed. If the key
     * does not exist in the resource bundle, the key itself will be returned as
     * a fallback.
     *
     * @param key the field ID (or key) to look up in the resource bundle. This
     * is typically the `fx:id` of a UI element.
     * @return a `StringBinding` that can be used to bind to a UI element's text
     * property.
     */
    public static StringBinding bind(String key, String optional) {
        if (bundleProperty.get() == null) return Bindings.createStringBinding(() -> key);
        return Bindings.createStringBinding(() -> {
            ResourceBundle bundle = bundleProperty.get();
            if (bundle.containsKey(key)) {
                log.debug("Found key '{}' in bundle", key);
                return bundle.getString(key);
            } else {
                log.warn("Missing key '{}' in resource bundle", key);
                return optional;  // Returning the key itself in case of missing translation
            }
        }, bundleProperty);
    }

    /**
     * Sets the current locale and updates the resource bundle for the new
     * locale. This method loads a new resource bundle based on the provided
     * locale and updates the `bundleProperty` to reflect the new bundle. It
     * also logs the success or failure of setting the locale.
     *
     * @param locale the new `Locale` to set for the language, such as
     * `Locale.ENGLISH` or `Locale.forLanguageTag("es")`.
     */
    public static void setLocale(Locale locale) {
        try {
            log.info("Setting locale to: {}", locale);
            ResourceBundle newBundle = ResourceBundle.getBundle("language", locale);
            bundleProperty.set(newBundle);
            log.info("Locale set successfully");
        } catch (Exception e) {
            log.error("""
                      Failed to load resource bundle for locale: {}.
                      Make sure a valid resource bundle exists in 'src/main/resources'.""", locale);
        }
    }

    /**
     * Automatically binds the text or promptText properties of UI controls
     * (such as `Label` and `TextInputControl`) in the provided controller
     * object to corresponding values in the current resource bundle. The method
     * iterates through all declared fields in the controller, checks if they
     * are annotated with `@FXML`, and binds their properties to the
     * corresponding language keys from the resource bundle. Fields annotated
     * with `@IgnoreBind` are ignored during the binding process.
     *
     * @param controller the controller object that contains UI controls (such
     * as labels and text input fields) whose properties will be bound to the
     * resource bundle.
     */
    public static void autoBind(Object controller) {
        log.debug("Obtaining declared fields from controller: {}", controller.getClass().getName());
        var fields = controller.getClass().getDeclaredFields();
        log.debug("Obtained {} declared fields", fields.length);
        for (var field : fields) {
            if (!field.isAnnotationPresent(FXML.class)) continue;
            if (field.isAnnotationPresent(IgnoreBind.class)) continue; // Skip ignored fields
            field.setAccessible(true);
            try {
                Object control = field.get(controller);
                if (control instanceof Labeled labeled) {
                    String key = field.getName(); // fx:id assumed as key
                    log.debug("Binding text property of labeled control '{}'", key);
                    labeled.textProperty().bind(Language.bind(key, labeled.getText()));
                } else if (control instanceof TextInputControl input) {
                    String key = field.getName();
                    log.debug("Binding promptText property of TextInputControl '{}'", key);
                    input.promptTextProperty().bind(Language.bind(key + ".prompt", input.getText()));
                }
            } catch (IllegalAccessException e) {
                log.error("Failed to bind control '{}'", field.getName(), e);
            }
        }
    }
}
