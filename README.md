# Snow Problem - Brandon Piper Edition

Hello!! This is my version of snow problem which is project #1 for computer science woohoo!!

Pretty please give me an A* :-)

## Compilation

Must be in the source folder

### Linux

```
javac *.java -d ./Compilation
cp -R ./levels ./Compilation/levels
cp -R ./resources ./Compilation/resources
```

### Windows

```
xcopy "./levels" "./Compilation/levels" /E /I
xcopy "./resources" "./Compilation/resources" /E /I
javac *.java -d ./Compilation
```

## Executing it

(make sure you're in the compilation folder)

`cd ./Compilation`

`java SnowProblem`