@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.1"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d D:\DIY\Java\apiss
call gradlew.bat compileJava --no-configuration-cache
