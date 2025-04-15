package io.github.snoopy137.languagemanager.utils;

import io.github.snoopy137.languagemanager.annotations.IgnoreBind;
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
 * @author alan Utility class to manage language settings and automatic UI
 * binding for JavaFX applications. This class allows setting the locale,
 * dynamically switching languages, and binding UI elements to corresponding
 * language keys in the resource bundle.
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
        try {
            bundleProperty = new SimpleObjectProperty<>(ResourceBundle.getBundle("language", Locale.getDefault()));
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
     * (such as {@code Label} and {@code TextInputControl}) in the provided
     * controller object to corresponding values in the current resource bundle.
     *
     * <p>
     * This method supports two usage modes:</p>
     * <ul>
     * <li><strong>FXML-based:</strong> Fields annotated with {@code @FXML} will
     * be automatically bound using the field name as the key.</li>
     * <li><strong>Code-based:</strong> Fields annotated with {@code @Bind} can
     * be bound programmatically in non-FXML contexts. The key is derived from
     * the field name.</li>
     * </ul>
     *
     * <p>
     * Fields annotated with {@code @IgnoreBind} will be ignored during the
     * binding process.</p>
     *
     * @param controller the controller or object containing UI controls whose
     * properties will be bound to the resource bundle.
     */
    public static void autoBind(Object controller) {
        log.debug("Obtaining declared fields from controller: {}", controller.getClass().getName());
        var fields = controller.getClass().getDeclaredFields();
        log.debug("Obtained {} declared fields", fields.length);

        for (var field : fields) {
            boolean isFxml = field.isAnnotationPresent(FXML.class);
            boolean isManualBind = field.isAnnotationPresent(io.github.snoopy137.languagemanager.annotations.Bind.class);

            if (!isFxml && !isManualBind) continue;
            if (field.isAnnotationPresent(IgnoreBind.class)) continue;

            field.setAccessible(true);
            try {
                Object control = field.get(controller);
                if (control == null) continue;

                // Default key: field name
                String key = field.getName();

                // Use custom key from @Bind if present
                if (isManualBind) {
                    var bindAnnotation = field.getAnnotation(io.github.snoopy137.languagemanager.annotations.Bind.class);
                    if (!bindAnnotation.value().isEmpty()) {
                        key = bindAnnotation.value();
                    }
                }

                if (control instanceof Labeled labeled) {
                    log.debug("Binding text property of labeled control '{}'", key);
                    labeled.textProperty().bind(Language.bind(key, labeled.getText()));
                } else if (control instanceof TextInputControl input) {
                    log.debug("Binding promptText property of TextInputControl '{}'", key + ".prompt");
                    input.promptTextProperty().bind(Language.bind(key + ".prompt", input.getText()));
                }

            } catch (IllegalAccessException e) {
                log.error("Failed to bind control '{}'", field.getName(), e);
            }
        }
    }
}
