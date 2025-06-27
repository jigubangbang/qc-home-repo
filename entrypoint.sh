#!/bin/sh
echo "Starting Community Service (port 8087)..."
java -jar /app/community-service.jar &
COMMUNITY_PID=$!
echo "Community Service started with PID: $COMMUNITY_PID"

echo "Starting Home Service (port 8088)..."
java -jar /app/home-service.jar &
HOME_PID=$!
echo "Home Service started with PID: $HOME_PID"

echo "Starting Quest Service (port 8089)..."
java -jar /app/quest-service.jar &
QUEST_PID=$!
echo "Quest Service started with PID: $QUEST_PID"

echo "All qc-home-repo services are running in the background. Waiting for them to finish..."

wait $COMMUNITY_PID
wait $HOME_PID
wait $QUEST_PID

echo "All services have terminated. Exiting container."
