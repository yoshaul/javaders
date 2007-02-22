@echo off
SET ANT_HOME=E:\DEVELOPMENT\apache-ant-1.6.1
SET J2EE_HOME=C:\JAVA\Sun\AppServer
SET JAVA_HOME=C:\Java\jdk1.5.0

SET CLASSPATH=.\;%JAVA_HOME%\lib;%J2EE_HOME%\lib\j2ee.jar;%CLASSPATH%

SET PATH=.\;%JAVA_HOME%\bin;%J2EE_HOME%\bin;%ANT_HOME%\bin;%PATH%;
@echo on

mkdir build
dir /s /b *.java > srcfiles.txt
javac -g -deprecation -d ./build @srcfiles.txt