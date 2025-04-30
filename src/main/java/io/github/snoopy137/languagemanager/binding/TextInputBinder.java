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
import javafx.scene.control.TextInputControl;
import lombok.extern.slf4j.Slf4j;

/**
 * Binds the {@code promptTextProperty} of {@link TextInputControl} controls
 * (e.g., TextField, TextArea, PasswordField) to language keys.
 * <p>
 * This binder appends {@code ".prompt"} to the provided key to locate the
 * prompt message in the resource bundle. If the key is not found, the current
 * prompt text is used as default.
 * </p>
 *
 * @author alan
 * @since 1.1.0
 */
@Slf4j
public class TextInputBinder implements ControlBinder {

    /**
     * Returns {@code true} if the control is a {@link TextInputControl}.
     *
     * @param control the control to check
     * @return {@code true} if the control is a TextInputControl instance,
     * otherwise {@code false}
     */
    @Override
    public boolean supports(Object control) {
        return control instanceof TextInputControl;
    }

    /**
     * Binds the {@code promptTextProperty} of the {@link TextInputControl} to
     * the language key.
     * <p>
     * The key is modified by appending {@code ".prompt"} to fetch the
     * appropriate entry from the language bundle. If the key is not found, the
     * existing prompt text is retained.
     *
     * @param control the control to bind
     * @param key the language key used to retrieve localized text from the
     * resource bundle
     */
    @Override
    public void bind(Object control, String key) {
        TextInputControl input = (TextInputControl) control;
        String promptKey = key + ".prompt";
        log.debug("Binding promptText property of TextInputControl to key '{}'", promptKey);
        input.promptTextProperty().bind(Language.bind(promptKey, input.getPromptText()));
    }
}
