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
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;

/**
 * Binds the {@code textProperty} and {@code tooltip} of {@link Tab} controls to
 * language keys.
 * <p>
 * This binder checks whether a given control is an instance of {@code Tab} and
 * binds its text and tooltip to the language bundle using the provided key.
 *
 * <p>
 * The tooltip is bound using a key with the suffix {@code .tooltip}. If no
 * tooltip is initially present, one is created automatically.
 * </p>
 *
 * @author alan
 * @since 1.1.0
 */
public class TabBinder implements ControlBinder {

    /**
     * Returns {@code true} if the control is a {@link Tab}.
     *
     * @param control the control to check
     * @return {@code true} if the control is a Tab, otherwise {@code false}
     */
    @Override
    public boolean supports(Object control) {
        return control instanceof Tab;
    }

    /**
     * Binds the {@code textProperty} and {@code tooltip.textProperty} of the
     * {@link Tab} to the language key.
     *
     * @param control the control to bind
     * @param key the language key used to retrieve localized text from the
     * resource bundle
     */
    @Override
    public void bind(Object control, String key) {
        if (control instanceof Tab tab) {
            // Bind the text property
            tab.textProperty().bind(Language.bind(key, tab.getText()));

            // Bind the tooltip property, if it exists
            if (tab.getTooltip() != null) {
                tab.getTooltip().textProperty().bind(Language.bind(key + ".tooltip", tab.getTooltip().getText()));
            } else {
                // If there's no tooltip, create and bind it
                Tooltip tooltip = new Tooltip();
                tooltip.textProperty().bind(Language.bind(key + ".tooltip", ""));
                tab.setTooltip(tooltip);
            }
        }
    }

}
