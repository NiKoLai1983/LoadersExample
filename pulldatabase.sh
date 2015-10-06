#!/usr/bin/env bash

##############################################################################
# Script configuration:
PACKAGE_NAME=es.jpv.android.examples.loadersexample
DB_NAME=dummyitemsdata.db
ADB=~/Android/Sdk/platform-tools/adb31
DB_BROWSER=/usr/bin/sqlitebrowser
ABE_JAR=~/StudioProjects/android-backup-extractor/build/libs/abe-all.jar
DB_PATH=apps/${PACKAGE_NAME}/db
##############################################################################

# Retrieve data from phone, unpack data and open database file
echo Extracting DB from phone...
mkdir -p ./dbdump
cd ./dbdump
${ADB} backup -f backup.ab -noapk ${PACKAGE_NAME}
echo Unpacking Android backup...
java -jar ${ABE_JAR} unpack backup.ab backup.tar
echo Untaring app data...
tar -xf backup.tar
cd ..
echo Opening the DB in viewer...
${DB_BROWSER} ./dbdump/${DB_PATH}/${DB_NAME} &