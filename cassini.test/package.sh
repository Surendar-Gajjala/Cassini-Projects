echo ""
echo "Begin packaging..."
rm -rf target/cassini.test.build
mkdir -p target/cassini.test.build/db/cassini.platform/src/main
mkdir -p target/cassini.test.build/db/cassini.test/src/main

echo "Copying platform schema files..."
cp -R ../cassini.platform/src/main/db target/cassini.test.build/db/cassini.platform/src/main

echo "Copying TEST schema files..."
cp -R src/main/db target/cassini.test.build/db/cassini.test/src/main

echo "Copying webapp files..."
mkdir target/cassini.test.build/webapp

cp -R target/cassini.test target/cassini.test.build/webapp

echo "Creating the archive file..."
cd target
DATE=`date +%Y-%m-%d`
tar -czf cassini.test.$DATE.tar.gz cassini.test.build
cd ..

echo "Finished packaging!"
echo ""
