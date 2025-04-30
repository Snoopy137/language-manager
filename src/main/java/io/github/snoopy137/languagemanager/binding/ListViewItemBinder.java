package io.github.snoopy137.languagemanager.binding;

import io.github.snoopy137.languagemanager.utils.Language;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import lombok.extern.slf4j.Slf4j;

/**
 * Binds specific {@link ListView} items to language keys.
 * <p>
 * This binder applies translation to specific string elements in the ListView's
 * items list. It assumes the items are Strings and binds only the first item to
 * the provided language key.
 * </p>
 *
 * @author alan
 * @since 1.1.0
 */
@Slf4j
public class ListViewItemBinder implements ControlBinder {

    /**
     * Returns {@code true} if the control is a {@link ListView}.
     *
     * @param control the control to check
     * @return {@code true} if the control is a ListView, otherwise
     * {@code false}
     */
    @Override
    public boolean supports(Object control) {
        return control instanceof ListView;
    }

    /**
     * Binds the first item in the {@link ListView} to the specified language
     * key. This method assumes the items are Strings.
     *
     * @param control the control to bind
     * @param key the language key used to retrieve localized text from the
     * resource bundle
     */
    @Override
    public void bind(Object control, String key) {
        if (control instanceof ListView<?> listView) {
            ObservableList<?> items = listView.getItems();
            if (!items.isEmpty() && items.get(0) instanceof String) {
                @SuppressWarnings("unchecked")
                ListView<String> stringListView = (ListView<String>) listView;
                for (int i = 0; i < listView.getItems().size(); i++) {
                    final int index = i;
                    String original = stringListView.getItems().get(i);
                    Language.bundleProperty().addListener((obs, oldVal, newVal) -> {
                        stringListView.getItems().set(index, newVal.getString(key + "." + index)); // example: bind just the first item
                    });
                    stringListView.getItems().set(index, Language.get(key + "." + index, original));
                }
                log.debug("Bound first ListView item to key '{}'", key);
            } else {
                log.warn("ListView items must be Strings to support language binding. Skipping binding for key '{}'", key);
            }
        }
    }
}
