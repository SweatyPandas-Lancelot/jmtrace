# jmtrace
A simple tool traces jvm memory accesses based on asm.

## Requirements
JDK 1.8+ / OpenJDK 1.8+
Gradle 6.7.1 （optional）

## Build
We use `gradle` to build the project and use `gradlew` to help you get the correct version of gradle. You can change target version in `gradle/wrapper/gradle-wrapper.properties`.

> cd ~/Your_Dir/jmtrace
> ./gradlew fatjar

You can find jmtrace-version.jar in `build/libs`

## Usage
> ./jmtrace [options] -jar jarfile [args...] //used for jarfile
> or
> ./jmtrace [options] class [args...] //used for single class file

## Output
- Read / Write signal
- Thread id
- 64-bit object id
- Field identification

> R(ead)|W(rite) threadId ObjectId <ObjectType.fieldName|ClassName.staticFiledName|ArrayType[index]>

## Example
```shell
$ jmtrace -jar something.jar "hello world"
R 1032 b026324c6904b2a9 cn.edu.nju.ics.Foo.someField
W 1031 e7df7cd2ca07f4f1 java.lang.Object[0]
W 1031 e7df7cd2ca07f4f2 java.lang.Object[1]
```