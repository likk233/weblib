#!/bin/bash
cd "$(dirname "$0")/frontend" || exit 1
if [ ! -d node_modules ]; then
  echo "Installing frontend dependencies..."
  npm install
fi
echo "Starting Vue frontend..."
npm run dev
echo "Frontend stopped. Press Enter to close."
read -r _
Unexpected error: Could not write JSON: could not initialize proxy [com.example.courseapp.model.Student#1001] - no Session