@echo off
REM Build and Run Script for Secure Coding Demo (Windows)
REM This script builds and runs the Spring Boot application

echo ================================================
echo   Secure Coding Demo - Build ^& Run Script
echo ================================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo [X] Java nije pronaden. Molimo instalirajte Java 17 ili noviji.
    pause
    exit /b 1
)

echo [OK] Java instaliran
java -version
echo.

REM Check if Maven is installed
mvn -version >nul 2>&1
if errorlevel 1 (
    echo [!] Maven nije pronaden. Pokusavam s Maven Wrapper-om...
    if exist mvnw.cmd (
        set MVN_CMD=mvnw.cmd
    ) else (
        echo [X] Maven Wrapper nije pronaden. Molimo instalirajte Maven.
        pause
        exit /b 1
    )
) else (
    set MVN_CMD=mvn
    echo [OK] Maven instaliran
    mvn -version | findstr /C:"Apache Maven"
)

echo.
echo [*] Buildam projekt...
echo.

REM Build the project
call %MVN_CMD% clean package -DskipTests

if errorlevel 1 (
    echo.
    echo [X] Build nije uspio!
    pause
    exit /b 1
)

echo.
echo [OK] Build uspjesan!
echo.
echo [*] Pokrecem aplikaciju...
echo.
echo ================================================
echo   Aplikacija ce biti dostupna na:
echo   http://localhost:8080
echo ================================================
echo.
echo Pritisnite Ctrl+C za zaustavljanje aplikacije
echo.

REM Run the application
call %MVN_CMD% spring-boot:run
