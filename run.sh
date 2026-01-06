#!/bin/bash

# Function to handle cleanup on exit
cleanup() {
    echo "Stopping frontend and backend..."
    kill $BACKEND_PID $FRONTEND_PID
    exit
}

# Trap SIGINT (Ctrl+C) and SIGTERM
trap cleanup SIGINT SIGTERM

# Check and extract uploads if necessary
if [ ! -d "Backend/uploads" ]; then
    echo "Media assets directory not found. Extracting uploads.tar.gz..."
    tar -xzf Backend/uploads.tar.gz -C Backend/
elif [ -z "$(ls -A Backend/uploads)" ]; then
    echo "Media assets directory is empty. Extracting uploads.tar.gz..."
    tar -xzf Backend/uploads.tar.gz -C Backend/
fi

echo "Starting Backend..."
cd Backend
./mvnw spring-boot:run &
BACKEND_PID=$!
cd ..

echo "Starting Frontend..."
cd Frontend/vue-frontend
npm run dev &
FRONTEND_PID=$!
cd ../..

echo "Both services are starting..."
echo "Backend PID: $BACKEND_PID"
echo "Frontend PID: $FRONTEND_PID"

# Wait for both processes
wait $BACKEND_PID $FRONTEND_PID
