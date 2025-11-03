# 🧩 KotlinExecutor

KotlinExecutor is a **mock IDE-style text editor and Kotlin script executor** built using **Jetpack Compose for Desktop**.  
It provides a lightweight environment where you can write, highlight, and execute Kotlin code directly — no need for a full IDE.

---

## ✨ Features

### 📝 Text Editor
- Syntax highlighting for Kotlin **keywords** and **numbers**  
- Recursive tokenization system to separate code by delimiters  
- Smart cursor tracking with **auto-scrolling** horizontally and vertically  
- Styled and scalable text rendering with `AnnotatedString`  
- Transparent text overlay technique to maintain editable text + highlighted view  

### ⚙️ Kotlin Interpreter
- Runs Kotlin code snippets or full `.kts` scripts  
- Displays **live process output** and **error stream** in real-time  
- Shows the final process state (`Running`, `OK`, `Error(code)`)  
- Supports process control – **Kill process** button stops execution instantly  

---

## 🚀 How to Use

1. **Launch the app** — a text editor will appear.  
2. **Type your Kotlin code** in the editor on the left panel.  
3. Click **Run** to:
   - Save your code to `output.kts`
   - Execute it using the `kotlinc -script` command
   - See your program output and errors in the right panel.  
4. If you want to stop the currently running program, click **Kill process**.

---

## 🧠 Example

```kotlin
fun main() {
    println("Hello from KotlinExecutor!")
}
```

Output:

```
Hello from KotlinExecutor!
```

---

## 🛠️ Requirements

Before running, ensure you have:
- **JDK 17+**
- **Kotlin Compiler (`kotlinc`)** available in your system PATH
- (Optional) IntelliJ IDEA or Android Studio for easier development

---

## ▶️ Running the App

You can build and run the application using **Gradle Wrapper** (included in the project).

### On macOS / Linux:
```bash
./gradlew :composeApp:run
```

### On Windows:
```bash
.\gradlew.bat :composeApp:run
```

This will launch the Compose Desktop app.

---