cd ../cassini.platform
mvn clean install -DskipTests

cd ../cassini.plm
cd src/main/webapp
rm -rf app/assets/bower_components/cassini-platform
bower update cassini-platform

cd ../../..
mvn clean install -DskipTests
