@ECHO off

echo Setting up required Maven libraries
echo NOTE: This script assumes you have Maven installed.
echo Maven 2.5 and later will work with this.
mvn install:install-file -Dfile=./libs/org/spigotmc/server/1.15.2-R0.1-SNAPSHOT/server-1.15.2-R0.1-SNAPSHOT.jar -DgroupId=org.spigotmc.server -DartifactId=spigot-server -Dversion=1.15.2-R0.1-SNAPSHOT -Dpackaging=jar -DgeneratePom=true
pause