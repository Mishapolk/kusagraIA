# Book Recommendation System IA

This is a prototype Java Swing application implementing a book recommendation system with login, search, recommendations, bookmarks, and admin features.

## Running

1. **Download the FlatLaf and SQLite JDBC dependencies** (jar files are ignored by git):

```
mkdir -p lib
curl -L -o lib/flatlaf-3.2.jar https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2/flatlaf-3.2.jar
curl -L -o lib/sqlite-jdbc-3.45.3.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.3.0/sqlite-jdbc-3.45.3.0.jar
curl -L -o lib/slf4j-api-2.0.13.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.13/slf4j-api-2.0.13.jar
curl -L -o lib/slf4j-simple-2.0.13.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.13/slf4j-simple-2.0.13.jar
```

2. **Compile and run**:

### Linux/macOS
```
javac -cp lib/flatlaf-3.2.jar:lib/sqlite-jdbc-3.45.3.0.jar:lib/slf4j-api-2.0.13.jar:lib/slf4j-simple-2.0.13.jar -d out $(find src -name "*.java")
java -cp out:lib/flatlaf-3.2.jar:lib/sqlite-jdbc-3.45.3.0.jar:lib/slf4j-api-2.0.13.jar:lib/slf4j-simple-2.0.13.jar com.ibcs.Main
```

### Windows
```
dir /s /b src\*.java > sources.txt
javac -cp lib\flatlaf-3.2.jar;lib\sqlite-jdbc-3.45.3.0.jar;lib\slf4j-api-2.0.13.jar;lib\slf4j-simple-2.0.13.jar -d out @sources.txt
java -cp out;lib\flatlaf-3.2.jar;lib\sqlite-jdbc-3.45.3.0.jar;lib\slf4j-api-2.0.13.jar;lib\slf4j-simple-2.0.13.jar com.ibcs.Main
```
