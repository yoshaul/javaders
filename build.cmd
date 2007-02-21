@echo off
SET ANT_HOME=C:\JAVA\apache-ant-1.6.1
SET J2EE_HOME=C:\JAVA\Sun\AppServer
SET JAVA_HOME=C:\j2sdk1.4.2_06

SET CLASSPATH=.\;%JAVA_HOME%\lib;%J2EE_HOME%\lib\j2ee.jar;%CLASSPATH%

SET PATH=.\;%JAVA_HOME%\bin;%J2EE_HOME%\bin;%ANT_HOME%\bin;%PATH%;
@echo on
ant