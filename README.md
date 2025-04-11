# language-manager

**Language Manager** is a JavaFX library that enables **dynamic language switching at runtime**, allowing you to update the application language without needing to refresh the scene.

## ✨ Features

- 🌍 Support for multiple languages using standard `.properties` files.
- 🔄 Change language on the fly without reloading scenes.
- 🔗 Automatic binding for `Label`, `Button`, `TextField`, and other controls using `@FXML` ids.
- ⚙️ Custom annotations to ignore specific fields from auto-binding.
- 📦 Lightweight and easy to integrate.

## 📦 Installation

Add the library to your project using Maven or Gradle (instructions coming soon once it's published).

## 📁 File Structure

Place your resource bundles in the `src/main/resources` folder:

src/
└── main/
    └── resources/
        ├── language.properties         # Default (English)
        ├── language_es.properties      # Spanish
        └── language_fr.properties      # French

## 🚀 Usage

### 1. Bind Your Controls

In your FXML controller:

```java
@FXML
private Label greeting;

public void initialize() {
    Language.autoBind(this); // Automatically binds the 'greeting' Label
}
```

### 2. Change Language Dynamically

```java
Language.setLocale(Locale.forLanguageTag("es")); // Switch to Spanish
```

### 3. Fallback Handling

If the desired language file doesn't exist, the system falls back to the default language.properties. You can customize or log this behavior in your own Language.getBundle() method.

### 4. Ignore Specific Fields

Use the @IgnoreBind annotation to skip certain controls from being automatically bound.
```java
@FXML @IgnoreBind
private Label customLabel;
```

### 📸 Demo

![Language Switching Demo](demo/demo.gif)

🔧 Under the Hood

Language Manager uses ResourceBundle, SimpleObjectProperty, and Bindings to keep text in sync with the selected locale — all while avoiding the need to reinitialize scenes.

📄 License

MIT License
