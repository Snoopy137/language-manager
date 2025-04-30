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
import javafx.scene.control.ComboBox;
import lombok.extern.slf4j.Slf4j;

/**
 * Binds the items of {@link ComboBox} controls to language keys.
 * <p>
 * This binder checks whether a given control is an instance of {@code ComboBox}
 * and binds each item (assumed to be a {@code String}) to a language key using
 * the given prefix and an index.
 * </p>
 * <p>
 * For example, a ComboBox with three items and a prefix {@code gender} will
 * expect keys {@code gender.0}, {@code gender.1}, {@code gender.2}.
 * </p>
 *
 * @author alan
 * @since 1.1.0
 */
@Slf4j
public class ComboBoxBinder implements ControlBinder {

    /**
     * Returns {@code true} if the control is a {@link ComboBox}.
     *
     * @param control the control to check
     * @return {@code true} if the control is a ComboBox, otherwise
     * {@code false}
     */
    @Override
    public boolean supports(Object control) {
        return control instanceof ComboBox<?>;
    }

    /**
     * Binds each item in the {@link ComboBox} (assumed to be a {@code String})
     * to a language key.
     * <p>
     * Items will be bound using the provided prefix and an index. The original
     * values will be replaced with the translated ones.
     * </p>
     *
     * @param control the control to bind
     * @param key the language key used to retrieve localized text from the
     * resource bundle
     */
    @Override
    public void bind(Object control, String key) {
        if (control instanceof ComboBox<?> comboBox) {
            if (!comboBox.getItems().isEmpty() && comboBox.getItems().get(0) instanceof String) {
                @SuppressWarnings("unchecked")
                ComboBox<String> stringComboBox = (ComboBox<String>) comboBox;
                comboBox.promptTextProperty().bind(Language.bind(key, comboBox.getPromptText()));
                for (int i = 0; i < stringComboBox.getItems().size(); i++) {
                    final int index = i; // effectively final copy
                    String original = stringComboBox.getItems().get(i);
                    String itemKey = key + "." + i;

                    Language.bundleProperty().addListener((obs, oldVal, newVal) -> {
                        log.debug("Binding ComboBox item {} with key '{}'", index, itemKey);
                        stringComboBox.getItems().set(index, newVal.getString(itemKey));
                    });
                    stringComboBox.getItems().set(i, Language.get(itemKey, original));
                }
            } else {
                log.warn("ComboBox items must be Strings to support language binding. Skipping binding for key '{}'", key);
            }
        }
    }
}
