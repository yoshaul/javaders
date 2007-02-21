@echo off
SET CLASSPATH=.;C:\JAVA\Sun\AppServer\lib\j2ee.jar;C:\j2sdk1.4.2_06\lib\rt.jar

java -Djava.naming.factory.initial=com.sun.jndi.cosnaming.CNCtxFactory -Djava.naming.provider.url=iiop://localhost:3700 -Dorg.omg.CORBA.ORBInitialHost=iiop://localhost:3700 -Dorg.omg.CORBA.ORBInitialPort=3700 -classpath "%CLASSPATH%" game.GameMenu debug
@echo on