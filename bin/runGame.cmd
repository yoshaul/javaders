@echo off
SET CLASSPATH=.;G:\!!Project_Disk\all_game_files\j2ee.jar;C:\Java\jdk1.5.0\jre\lib\rt.jar

java -Djava.naming.factory.initial=com.sun.jndi.cosnaming.CNCtxFactory -Djava.naming.provider.url=iiop://localhost:3700 -Dorg.omg.CORBA.ORBInitialHost=iiop://localhost:3700 -Dorg.omg.CORBA.ORBInitialPort=3700 -classpath "%CLASSPATH%" game.GameMenu
@echo on