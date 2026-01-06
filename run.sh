#!/bin/bash

# Function to handle cleanup on exit
cleanup() {
    echo ""
    echo "Stopping Prism services..."
    [ -n "$BACKEND_PID" ] && kill $BACKEND_PID 2>/dev/null
    [ -n "$FRONTEND_PID" ] && kill $FRONTEND_PID 2>/dev/null
    exit
}

# Trap SIGINT (Ctrl+C) and SIGTERM
trap cleanup SIGINT SIGTERM

echo "======================================================="
echo "   PRISM: Liquid Glass Media Management System Setup   "
echo "======================================================="

# 1. Dependency Checks
echo "[1/5] Checking system dependencies..."
command -v java >/dev/null 2>&1 || { echo >&2 "❌ Java 17+ is required but not installed. Aborting."; exit 1; }
command -v node >/dev/null 2>&1 || { echo >&2 "❌ Node.js is required but not installed. Aborting."; exit 1; }
command -v npm >/dev/null 2>&1 || { echo >&2 "❌ NPM is required but not installed. Aborting."; exit 1; }
echo "✅ Dependencies verified."

# 2. Database Extraction & Setup
echo "[2/5] Setting up database and assets..."
if command -v mysql >/dev/null 2>&1; then
    echo "Attempting to ensure database '_backend' exists..."
    # Attempting to create DB with the password found in application.yml
    mysql -u root -pebrim@CJ@lloh25 -e "CREATE DATABASE IF NOT EXISTS _backend;" 2>/dev/null
    if [ $? -eq 0 ]; then
        echo "✅ Database '_backend' is ready."
    else
        echo "⚠️  Could not auto-create database. Please ensure MySQL is running and check credentials in 'Backend/src/main/resources/application.yml'."
    fi
fi

# 3. Media Assets Extraction
if [ ! -d "Backend/uploads" ] || [ -z "$(ls -A Backend/uploads 2>/dev/null)" ]; then
    echo "[3/5] Extracting media assets from bundle..."
    mkdir -p Backend/uploads
    tar -xzf Backend/uploads.tar.gz -C Backend/
    echo "✅ Assets extracted."
else
    echo "[3/5] Media assets already present."
fi

# 4. Frontend Installation
if [ ! -d "Frontend/vue-frontend/node_modules" ]; then
    echo "[4/5] node_modules not found. Installing frontend dependencies..."
    echo "      This may take 1-2 minutes depending on your connection."
    cd Frontend/vue-frontend && npm install >/dev/null 2>&1
    cd ../..
    echo "✅ Frontend dependencies installed."
else
    echo "[4/5] Frontend dependencies already installed."
fi

# 5. Launch
echo "[5/5] Launching Prism Media System..."
chmod +x Backend/mvnw

cd Backend
./mvnw spring-boot:run &
BACKEND_PID=$!
cd ..

cd Frontend/vue-frontend
npm run dev &
FRONTEND_PID=$!
cd ../..

echo "======================================================="
echo "🚀 Prism is now starting!"
echo "Frontend URL:  http://127.0.0.1:5173"
echo "Backend URL:   http://127.0.0.1:8080"
echo "API Docs:      http://127.0.0.1:8080/swagger-ui/index.html"
echo "======================================================="
echo "Press Ctrl+C to stop both services."

# Wait for both processes
wait $BACKEND_PID $FRONTEND_PID
