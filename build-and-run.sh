#!/bin/bash

# Build and Run Script for Secure Coding Demo
# This script builds and runs the Spring Boot application

echo "================================================"
echo "  Secure Coding Demo - Build & Run Script"
echo "================================================"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null
then
    echo "❌ Java nije pronađen. Molimo instalirajte Java 17 ili noviji."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java verzija $JAVA_VERSION je preniska. Potrebna je Java 17+."
    exit 1
fi

echo "✅ Java verzija: $(java -version 2>&1 | head -n 1)"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null
then
    echo "⚠️  Maven nije pronađen. Pokušavam s Maven Wrapper-om..."
    if [ -f "./mvnw" ]; then
        MVN_CMD="./mvnw"
    else
        echo "❌ Maven Wrapper nije pronađen. Molimo instalirajte Maven."
        exit 1
    fi
else
    MVN_CMD="mvn"
    echo "✅ Maven verzija: $(mvn -version | head -n 1)"
fi

echo ""
echo "📦 Buildam projekt..."
echo ""

# Build the project
$MVN_CMD clean package -DskipTests

if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Build nije uspio!"
    exit 1
fi

echo ""
echo "✅ Build uspješan!"
echo ""
echo "🚀 Pokrećem aplikaciju..."
echo ""
echo "================================================"
echo "  Aplikacija će biti dostupna na:"
echo "  http://localhost:8080"
echo "================================================"
echo ""
echo "Pritisnite Ctrl+C za zaustavljanje aplikacije"
echo ""

# Run the application
$MVN_CMD spring-boot:run
