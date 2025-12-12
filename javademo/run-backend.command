#!/bin/bash
cd "$(dirname "$0")/backend" || exit 1
echo "Starting Spring Boot backend..."
mvn spring-boot:run
echo "Backend stopped. Press Enter to close."
read -r _
