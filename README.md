# Book Recommendation System IA

This is a prototype Java Swing application implementing a book recommendation system with login, search, recommendations, bookmarks, and admin features.

## Running

1. **Download the FlatLaf dependency** (jar files are ignored by git):

```
mkdir -p lib
curl -L -o lib/flatlaf-3.2.jar https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2/flatlaf-3.2.jar
```

2. **Compile and run**:

```
javac -cp lib/flatlaf-3.2.jar -d out $(find src -name "*.java")
java -cp out:lib/flatlaf-3.2.jar com.ibcs.Main
```
