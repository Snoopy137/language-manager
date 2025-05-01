[![](https://img.shields.io/maven-central/v/io.github.snoopy137/language-manager)](https://central.sonatype.com/artifact/io.github.snoopy137/language-manager)
![License](https://img.shields.io/github/license/snoopy137/language-manager)

# language-manager
**Language Manager** is a JavaFX library that enables **dynamic language switching at runtime**, allowing you to update the application language without needing to refresh the scene.

### 📑 Table of Contents

1. [Features](#features)
2. [Installation](#installation)
3. [File Structure](#file-structure)
4. [Usage](#usage)
   - [1. FXML-Based Auto Binding](#1-fxml-based-auto-binding)
   - [2. Programmatic Binding (No FXML Required)](#2-programmatic-binding-no-fxml-required)
   - [3. Custom Binding Key (Optional)](#3-custom-binding-key-optional)
   - [4. Manual Binding Without Annotations](#4-manual-binding-without-annotations)
   - [5. Change Language Dynamically](#5-change-language-dynamically)
5. [Language Properties](#language-properties)
   - [1. Base Name Customization](#1-base-name-customization)
   - [2. Key Structure for Complex Controls](#2-key-structure-for-complex-controls)
6. [Fallback Handling](#fallback-handling)
7. [Demo](#demo)
8. [License](#license)
9. [Contributing](#contributing)
10. [Issues](#issues)
11. [Versioning](#versioning)

<a id="features" name="features"></a>
## ✨ Features

🌍 Support for multiple languages using standard `.properties` files.

🔄 Change language dynamically at runtime — no need to reload the scene.

🔗 Automatic binding for JavaFX controls such as `Label`, `Button`, `TextField`, `CheckBox`, `RadioButton`, `ChoiceBox`, `ComboBox`, `MenuItem`, and more.

🧠 Programmatic binding for controls without `@FXML` IDs — perfect for dynamically created interfaces.

⚙️ Custom annotations to ignore or customize specific field bindings.

🧩 Support for `TreeItem` and `Tab` bindings as well.

📦 Lightweight, non-intrusive, and easy to integrate into any JavaFX project.
<a id="installation" name="installation"></a>
## 📦 Installation

Add the library to your project using Maven or Gradle

### Maven
Add the following to your `pom.xml`:
```xml
<dependency>
    <groupId>io.github.snoopy137</groupId>
    <artifactId>language-manager</artifactId>
    <version>1.1.1</version>
</dependency>
```
### Gradle
Add this to your `build.gradle`:
```groovy
dependencies {
    implementation 'io.github.snoopy137:language-manager:1.1.1'
}
```
<a id="file-structure" name="file-structure"></a>
## 📁 File Structure

Place your resource bundles in the `src/main/resources` folder:

```text
src/
└── main/
    └── resources/
        ├── language.properties         # Default (English)
        ├── language_es.properties      # Spanish
        └── language_fr.properties      # French
```
For specific key patterns used by controls like ChoiceBox, ComboBox, etc., see the Key Structure section.
<a id="usage" name="usage"></a>
## 🚀 Usage
<a id="1-fxml-based-auto-binding" name="1-fxml-based-auto-binding"></a>
   ### 1. FXML-Based Auto Binding

   If you're using FXML, simply annotate your controller fields and call Language.autoBind(this) to automatically bind controls based on their @FXML IDs.

   ```java
   @FXML
   private Label greeting;

   @FXML
   private Button submitButton;

   public void initialize() {
       Language.autoBind(this); // Binds all supported @FXML controls automatically
   }
   ```
   ℹ️ Supported controls include Label, Button, TextField, TextArea, CheckBox, MenuItem, Tab, Tooltip, and more.

   🚫 To exclude a specific field from being auto-bound, use the @IgnoreBind annotation:
   ```java
   @FXML @IgnoreBind
   private Label doNotTranslate;
   ```
<a id="2-programmatic-binding-no-fxml-required" name="2-programmatic-binding-no-fxml-required"></a>
   ### 2. Programmatic Binding (No FXML Required)

   If you're not using FXML or want to create and bind controls dynamically, you can use the @Bind annotation without @FXML. Just make sure to initialize your controls    before calling Language.autoBind(this).

   ```java
   @Bind
   private Label dynamicLabel;

   public void initialize() {
       dynamicLabel = new Label();
       rootPane.setCenter(dynamicLabel);

       Language.autoBind(this); // Binds to key "dynamicLabel"
   }
   ```
<a id="3-custom-binding-key-optional" name="3-custom-binding-key-optional"></a>
   ### 3. Custom Binding Key (Optional)

   Whether you're using FXML or not, you can override the default binding key (which is normally the field name) by providing a custom value to the @Bind annotation.

   ✅ With FXML

   ```java
   @FXML
   @Bind("custom.key")
   private Label greeting; // Binds to the key "custom.key" instead of the field name.
   ```
   ✅ Without FXML
   ```java
   @Bind("custom.key")
   private Label dynamicLabel;

   public void initialize() {
       dynamicLabel = new Label();
       Language.autoBind(this); // Binds to the key "custom.key" instead of the field name.
   }
   ```
   This is useful for mapping controls to translation keys that don’t match their field names or follow a naming convention.
<a id="4-manual-binding-without-annotations" name="4-manual-binding-without-annotations"></a>
   ### 4. Manual Binding Without Annotations

   If you're creating controls programmatically (not declared as fields), you can bind them using Language.autoBindField(...).
   In this case, you must provide the translation key explicitly, as there's no field name to derive it from.

   ```java
   private void initialize() {
       Label dynamicLabel = new Label();
       Language.autoBindField(dynamicLabel, "dynamic.label");
   }
   ```

   This is ideal for dynamically created controls that aren't declared as fields or when you want precise manual control over the key used.
<a id="5-change-language-dynamically" name="5-change-language-dynamically"></a>
   ### 5. Change Language Dynamically

   Switch the application's active language at runtime using:

   ```java
   Language.setLocale(Locale.forLanguageTag("es")); // Switch to Spanish
   ```
   🌍 This will automatically update all bound controls with the translated values from the corresponding language_es.properties file.

   You can switch to any language as long as a corresponding .properties file is available (e.g., language_fr.properties for French).

💡 If a key is missing in the selected language file, the fallback mechanism (see next section) will handle it gracefully.
<a id="language-properties" name="language-properties"></a>
##  🗂️ Language Properties
   # 1. Base Name Customization
   By default, the Language Manager looks for a language.properties file. You can customize the base name using:
   ```java
   Language.setBaseName("mybundle"); // Looks for mybundle.properties, mybundle_es.properties, etc.
   ```
   This lets you organize language files however you'd like.
   # 2. Key Structure for Complex Controls
   Some controls (like ChoiceBox, ComboBox, TabPane, etc.) require a specific key format in the .properties files for their child items or tooltips to be localized       properly.
   Example structure:
   ```properties
   # ChoiceBox
   choiceBox=Choose an option
   choiceBox.0=Option1
   choiceBox.1=Option2
   
   # ComboBox
   comboBox=Select an item
   comboBox.0=Option 1
   comboBox.1=Option 2
   
   # TabPane
   tab1=Tab 1
   tab2=Tab 2
   tab1.tooltip=Tab tooltip 1
   tab2.tooltip=Tab tooltip 2
   
   # ListView
   listView.0=First item
   listView.1=Second item
   listView.2=Third item
   
   # TreeView
   treeView.0=Parent Node
   treeView.0.0=Child Node 1
   treeView.0.1=Child Node 2
   ```
   Controller Example:
   ```java
   @FXML private ChoiceBox<String> choiceBox;
   @FXML private ComboBox<String> comboBox;
   @FXML private ListView<String> listView;
   @FXML private TreeView<String> treeView;
   @FXML private TabPane tabPane;
   @FXML private Tab tab1, tab2;
   
   @FXML
   private void initialize() {
       choiceBox.getItems().addAll("Choice 1", "Choice 2");
       comboBox.getItems().addAll("Combo 1", "Combo 2");
   
       listView.getItems().addAll("Item 1", "Item 2", "Item 3");
   
       TreeItem<String> root = new TreeItem<>("Root");
       root.getChildren().addAll(
           new TreeItem<>("Child 1"),
           new TreeItem<>("Child 2")
       );
       treeView.setRoot(root);
   }
   ```
<a id="fallback-handling" name="fallback-handling"></a>
### Fallback Handling

If a specific language file (e.g., language_es.properties) doesn't exist, the system automatically falls back to the default language.properties.
Additionally, if a bound key is missing in the current resource bundle:

The original textProperty() (or equivalent property) of the control is preserved — it won't be overwritten with a blank or placeholder.

A warning will be logged to help you track missing translations:
```java
Missing key 'submitButton' in resource bundle
```
<a id="demo" name="demo"></a>
### 📸 Demo

![Language Switching Demo](demo/demo.gif)

🔧 Under the Hood

Language Manager leverages ResourceBundle, SimpleObjectProperty, and Bindings to keep text in sync with the selected locale — all while avoiding the need to reinitialize scenes.
<a id="license" name="license"></a>
## 📄 License

MIT License

📚 [View Javadocs](https://snoopy137.github.io/language-manager/)
<a id="contributing" name="contributing"></a>
## 🤝 Contributing

We welcome contributions! Please fork the repository and submit a pull request with your changes.
<a id="issues" name="issues"></a>
## 🐞 Issues

If you encounter any bugs or have feature requests, please open an issue in the [GitHub Issues](https://github.com/snoopy137/language-manager/issues) section.

<a id="versioning" name="versioning"></a>
## 🔖 Versioning

We follow [Semantic Versioning](https://semver.org/) for our releases. You can check out the release notes for each version on the [Releases Page](https://github.com/snoopy137/language-manager/releases).

