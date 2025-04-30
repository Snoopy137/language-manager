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

/**
 * Defines the contract for binding localized text to a specific type of JavaFX
 * control.
 * <p>
 * Implementations of this interface are responsible for determining if a given
 * control is supported and applying the appropriate binding logic using the
 * provided localization key.
 * </p>
 *
 * <p>
 * This interface allows Language Manager to be extended with support for
 * additional JavaFX UI controls beyond the default ones.</p>
 *
 * @author alan
 * @since 1.1.0
 */
public interface ControlBinder {

    /**
     * Checks whether this binder supports the given control type.
     *
     * @param control the UI control to evaluate
     * @return {@code true} if this binder can bind the given control,
     * {@code false} otherwise
     */
    boolean supports(Object control);

    /**
     * Applies localization binding to the given control using the specified
     * key.
     *
     * @param control the UI control to bind
     * @param key the localization key to bind to the control
     */
    void bind(Object control, String key);
}
