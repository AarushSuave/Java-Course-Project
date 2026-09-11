@echo off
title RescueNet - Disaster Response Management System

echo ====================================================================
echo        RescueNet: Disaster Response Management System
echo           Second Year Engineering Java OOP Project
echo ====================================================================
echo.

where javac >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: JDK javac was not found in your system PATH.
    echo Please install JDK 17 or JDK 21 and ensure Java is in PATH.
    echo.
    pause
    exit /b 1
)

if not exist bin mkdir bin

echo [1/2] Compiling RescueNet Java source files...
javac -d bin src/exception/*.java src/model/*.java src/service/*.java src/gui/*.java src/test/*.java src/Main.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed! Check error messages above.
    echo.
    pause
    exit /b 1
)

echo [2/2] Compilation successful.
echo.
echo Launching RescueNet GUI...
echo Default Credentials: admin / admin123
echo.

java -cp bin Main
