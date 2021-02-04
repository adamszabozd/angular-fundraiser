#!/bin/bash

#UPDATE THESE LINES ACCORDING TO YOUR PROJECT STRUCTURE
PRIVATE_KEY_PATH=C:\Felhasználók\Asus\Letöltések\2020-sept-demo.pem
SITE_DOMAIN=kickstarter.progmasters.hu
SERVER_USER=ubuntu
PATH_OF_JAR_FILE=./target/angular-fundraiser-0.0.1-SNAPSHOT.jar
PATH_OF_FRONTEND_DIR=./angular-frontend/
PATH_OF_FRONTEND_BUILD_DIR=./angular-frontend/dist/angular-frontend/*

#Print all commands to console during execution
set -x
#Same but needed... set verbose mode...
set -v
#All or nothing, if anything fails, stop execution
set -e

cd ${PATH_OF_FRONTEND_DIR}
ng build --prod
cd ..
mvn clean package -DskipTests=true

# Kill running process on server
ssh -i ${PRIVATE_KEY_PATH} ${SERVER_USER}@${SITE_DOMAIN} "shutdown.sh"

# Delete all existing frontend files
ssh -i ${PRIVATE_KEY_PATH} ${SERVER_USER}@${SITE_DOMAIN} "sudo rm -rf ~/frontend/*"

# Copy frontend files
scp -i ${PRIVATE_KEY_PATH} -r ${PATH_OF_FRONTEND_BUILD_DIR} ${SERVER_USER}@${SITE_DOMAIN}:~/frontend

#Copy backend files
scp -i ${PRIVATE_KEY_PATH} -r ${PATH_OF_JAR_FILE} ${SERVER_USER}@${SITE_DOMAIN}:~/

#Start backend server
ssh -i ${PRIVATE_KEY_PATH} ${SERVER_USER}@${SITE_DOMAIN} "./startup.sh"

