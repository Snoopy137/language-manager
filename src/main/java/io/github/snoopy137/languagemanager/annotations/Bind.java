package io.github.snoopy137.languagemanager.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author alan Marks a UI field for language binding when not using FXML.
 * Allows specifying a custom key for the resource bundle.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Bind {

    /**
     * Optional key to override the default field name. If empty, the field name
     * is used as the key.
     */
    String value() default "";
}
