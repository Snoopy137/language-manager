package io.github.snoopy137.languagemanager.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to indicate that a field in a controller should be excluded
 * from automatic binding in the `Language` class.
 * <p>
 * Fields marked with this annotation will be ignored during the automatic
 * binding process in the `autoBind()` method of the `Language` class. This is
 * useful when you want to explicitly prevent certain fields from being bound to
 * language keys in the resource bundle, for example, fields that should not be
 * localized.
 * </p>
 * <p>
 * The annotation should be applied to fields in a JavaFX controller class that
 * are annotated with `@FXML` but should not be automatically bound to language
 * translations.
 * </p>
 *
 * @author alan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IgnoreBind {
}
