/*
 * The MIT License
 *
 * Copyright 2025 alan.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.snoopy137.languagemanager.binding;

import io.github.snoopy137.languagemanager.utils.Language;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;
import lombok.extern.slf4j.Slf4j;

/**
 * A binder for {@link ChoiceBox} controls containing {@code String} items.
 * <p>
 * This binder updates the text of each item in the {@code ChoiceBox} by binding
 * its language key to the appropriate entry in the resource bundle.
 * <p>
 * The keys are expected to follow the format: {@code key.0}, {@code key.1},
 * etc., based on the item's index in the list.
 *
 * <p>
 * <strong>Example:</strong><br>
 * For a {@code ChoiceBox<String>} field named {@code genderBox} with 2 items,
 * the expected keys in the resource bundle are
 * {@code genderBox.0}, {@code genderBox.1}, etc.
 * </p>
 *
 * <p>
 * This binder only works if all items are {@code String} instances.
 * </p>
 *
 * @author alan
 * @since 1.1.0
 */
@Slf4j
public class ChoiceBoxBinder implements ControlBinder {

    /**
     * Checks if the given control is a {@code ChoiceBox<String>}.
     *
     * @param control the UI control to check
     * @return {@code true} if the control is a
     * {@code ChoiceBox<String>}, {@code false} otherwise
     */
    @Override
    public boolean supports(Object control) {
        return control instanceof ChoiceBox<?>
                && !((ChoiceBox<?>) control).getItems().isEmpty()
                && ((ChoiceBox<?>) control).getItems().get(0) instanceof String;
    }

    /**
     * Binds each item in the {@link ChoiceBox} to a corresponding language key
     * in the resource bundle.
     *
     * @param control the {@link ChoiceBox} to bind
     * @param key the base key used to look up each item (with index suffix)
     */
    @Override
    public void bind(Object control, String key) {
        if (control instanceof ChoiceBox<?> choiceBox) {
            @SuppressWarnings("unchecked")
            ChoiceBox<String> stringChoiceBox = (ChoiceBox<String>) choiceBox;
            ObservableValue binding = Language.bind(key, (String) choiceBox.getValue());
            choiceBox.valueProperty().bind(binding);
            for (int i = 0; i < stringChoiceBox.getItems().size(); i++) {
                final int index = i;
                String original = stringChoiceBox.getItems().get(index);
                String itemKey = key + "." + index;

                Language.bundleProperty().addListener((obs, oldVal, newVal) -> {
                    log.debug("Binding ChoiceBox item {} with key '{}'", index, itemKey);
                    stringChoiceBox.getItems().set(index, newVal.getString(itemKey));
                });

                stringChoiceBox.getItems().set(index, Language.get(itemKey, original));
            }
        }
    }
}
