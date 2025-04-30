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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lombok.extern.slf4j.Slf4j;

/**
 * Binds the {@code valueProperty} of {@link TreeItem} controls to language
 * keys.
 * <p>
 * This binder checks whether a given control is an instance of {@code TreeItem}
 * and binds its value (and the value of its children) to the language bundle
 * using the provided key and an index.
 * </p>
 *
 * @author alan
 * @since 1.1.0
 */
@Slf4j
public class TreeItemBinder implements ControlBinder {

    /**
     * Returns {@code true} if the control is a {@link TreeItem}.
     *
     * @param control the control to check
     * @return {@code true} if the control is a TreeItem, otherwise
     * {@code false}
     */
    @Override
    public boolean supports(Object control) {
        return control instanceof TreeView;
    }

    /**
     * Binds the {@code valueProperty} of the {@link TreeItem} (and its
     * children) to the language key.
     *
     * @param control the control to bind
     * @param key the language key used to retrieve localized text from the
     * resource bundle
     */
    @Override
    public void bind(Object control, String key) {
        if (control instanceof TreeView<?> treeView) {
            bindTreeItem(treeView.getRoot(), key, 0);
            for (int i = 1; i < treeView.getRoot().getChildren().size() - 1; i++) {
                bindTreeItem(treeView.getRoot().getChildren().get(i), key, i);
            }
        }
    }

    /**
     * Recursively binds a TreeItem (and its children) to language keys based on
     * their index.
     *
     * @param treeItem the TreeItem to bind
     * @param key the language key prefix
     * @param index the index of the current TreeItem in the hierarchy
     */
    private void bindTreeItem(TreeItem<?> treeItem, String key, int index) {
        if (treeItem.getValue() instanceof String) {
            String itemKey = key + "." + index;
            log.debug("Binding TreeItem with key '{}'", itemKey);

            // Use StringBinding to bind the TreeItem's value property
            ObservableValue binding = Language.bind(itemKey, (String) treeItem.getValue());

            // Bind the valueProperty of the TreeItem to the StringBinding
            treeItem.valueProperty().bind(binding);

            // Bind the children of this TreeItem
            for (int i = 0; i < treeItem.getChildren().size(); i++) {
                log.debug("Binding TreeItem with key '{}'", itemKey);
                bindTreeItem(treeItem.getChildren().get(i), key + "." + index, i); // Use current index to form child keys
            }
        } else {
            log.warn("TreeItem value must be a String to support language binding. Skipping binding for key '{}'", key);
        }
    }
}
