#!/bin/bash

echo "Setting up required Maven libraries."
mvn install:install-file -Dfile=./libs/org/spigotmc/server/1.15.2-R0.1-SNAPSHOT/server-1.15.2-R0.1-SNAPSHOT.jar -DgroupId=org.spigotmc.server -DartifactId=spigot-server -Dversion=1.15.2-R0.1-SNAPSHOT -Dpackaging=jar -DgeneratePom=true
read -n 1 -s -r -p "Press any key to continue"