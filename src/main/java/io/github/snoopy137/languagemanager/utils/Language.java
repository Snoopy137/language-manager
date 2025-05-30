package io.github.snoopy137.languagemanager.utils;

import io.github.snoopy137.languagemanager.annotations.IgnoreBind;
import io.github.snoopy137.languagemanager.binding.CheckBoxBinder;
import io.github.snoopy137.languagemanager.binding.ChoiceBoxBinder;
import io.github.snoopy137.languagemanager.binding.ComboBoxBinder;
import io.github.snoopy137.languagemanager.binding.ContextMenuBinder;
import io.github.snoopy137.languagemanager.binding.ControlBinder;
import io.github.snoopy137.languagemanager.binding.HyperlinkBinder;
import io.github.snoopy137.languagemanager.binding.LabeledBinder;
import io.github.snoopy137.languagemanager.binding.ListViewItemBinder;
import io.github.snoopy137.languagemanager.binding.MenuBinder;
import io.github.snoopy137.languagemanager.binding.MenuItemBinder;
import io.github.snoopy137.languagemanager.binding.RadioButtonBinder;
import io.github.snoopy137.languagemanager.binding.TabBinder;
import io.github.snoopy137.languagemanager.binding.TextInputBinder;
import io.github.snoopy137.languagemanager.binding.TitledPaneBinder;
import io.github.snoopy137.languagemanager.binding.TooltipBinder;
import io.github.snoopy137.languagemanager.binding.TreeItemBinder;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to manage language settings and automatic UI binding for JavaFX
 * applications. This class allows setting the locale, dynamically switching
 * languages, and binding UI elements to corresponding language keys in the
 * resource bundle.
 *
 * @author alan
 */
@Slf4j
public class Language {

    private static final ObjectProperty<ResourceBundle> bundleProperty = new SimpleObjectProperty<>();
    private static String baseName = "language";

    private static final List<ControlBinder> BINDERS = List.of(
            new LabeledBinder(),
            new TextInputBinder(),
            new MenuItemBinder(),
            new TabBinder(),
            new TooltipBinder(),
            new TitledPaneBinder(),
            new TreeItemBinder(),
            new ContextMenuBinder(),
            new CheckBoxBinder(),
            new RadioButtonBinder(),
            new HyperlinkBinder(),
            new MenuBinder(),
            new ListViewItemBinder(),
            new ComboBoxBinder(),
            new ChoiceBoxBinder()
    );

    /**
     * Gets the current resource bundle being used for language translations.
     *
     * @return the current resource bundle.
     */
    public static ResourceBundle getBundle() {
        ResourceBundle bundle = bundleProperty.get();
        if (bundle == null) {
            try {
                bundle = ResourceBundle.getBundle(baseName, Locale.getDefault());
                bundleProperty.set(bundle);
            } catch (MissingResourceException e) {
                log.warn("""
            Failed to load the properties file.
            Make sure a valid resource bundle exists in 'src/main/resources'.
            Returning null. UI may not display localized text.""");
            }
        }
        return bundle;
    }

    /**
     * Sets a custom base name for the resource bundle. Must be called before
     * setting the locale.
     *
     * @param name the new base name (e.g., "messages")
     */
    public static void setBaseName(String name) {
        baseName = name;
        // Clear the cached bundle so it reloads
        bundleProperty.set(null);
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
                return optional;  // Returning the original text property itself in case of missing translation
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
            ResourceBundle newBundle = ResourceBundle.getBundle(baseName, locale);
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
                if (control == null) {
                    log.warn("null controls cannot be bind, initialize your control before autobinding");
                    continue;
                }

                // Default key: field name
                String key = field.getName();

                // Use custom key from @Bind if present
                if (isManualBind) {
                    var bindAnnotation = field.getAnnotation(io.github.snoopy137.languagemanager.annotations.Bind.class);
                    if (!bindAnnotation.value().isEmpty()) {
                        key = bindAnnotation.value();
                    }
                }

                boolean bound = false;
                for (ControlBinder binder : BINDERS) {
                    if (binder.supports(control)) {
                        binder.bind(control, key);
                        bound = true;
                        break;
                    }
                }
                if (!bound) {
                    log.debug("No binder found for control type: {}", control.getClass().getName());
                }

            } catch (IllegalAccessException e) {
                log.error("Failed to bind control '{}'", field.getName(), e);
            }
        }
    }

    /**
     * Retrieves the translated string for the specified key from the current
     * resource bundle. If the key does not exist or the bundle is not loaded,
     * the provided fallback string will be returned instead.
     * <p>
     * This method is useful when an immediate, non-observable translation is
     * required (e.g., for static content or initial values).
     *
     * @param key the key to look up in the resource bundle.
     * @param fallback the fallback string to return if the key is missing or
     * the bundle is not available.
     * @return the translated string if available, or the fallback value
     * otherwise.
     */
    public static String get(String key, String fallback) {
        ResourceBundle bundle = bundleProperty.get();
        if (bundle != null && bundle.containsKey(key)) {
            log.debug("Found key '{}' in bundle", key);
            return bundle.getString(key);
        } else {
            log.warn("Missing key '{}' in resource bundle", key);
            return fallback;
        }
    }

    /**
     * Automatically binds a single UI control to the corresponding value in the
     * current resource bundle.
     * <p>
     * This method is useful when you create controls dynamically at runtime
     * (instead of declaring them as fields in a controller) and still want to
     * automatically bind their text or promptText properties.
     * </p>
     *
     * <p>
     * The key used for the binding will be the control's {@code id} property.
     * Therefore, make sure the control has its {@code id} set appropriately.
     * </p>
     *
     * @param control the UI control to bind (e.g., {@code Label},
     * {@code Button}, {@code TextField}, etc.).
     */
    public static void autoBindField(Object control, String key) {
        if (control == null) {
            log.warn("Control is null, skipping auto-bind");
            return;
        }

        if (key.isBlank() || key == null) {
            log.error("no key was provided, binding cannot be complete");
            return;
        }

        boolean bound = false;
        for (ControlBinder binder : BINDERS) {
            if (binder.supports(control)) {
                binder.bind(control, key);
                bound = true;
                log.debug("Successfully auto-bound control '{}' with key '{}'", control.getClass().getName(), key);
                break;
            }
        }

        if (!bound) {
            log.debug("No binder found for control type: {}", control.getClass().getName());
        }
    }
}
