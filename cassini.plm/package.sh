echo ""
echo "Begin packaging..."
rm -rf target/cassini.plm.build
mkdir -p target/cassini.plm.build/db/cassini.platform/src/main
mkdir -p target/cassini.plm.build/db/cassini.plm/src/main

echo "Copying platform schema files..."
cp -R ../cassini.platform/src/main/db target/cassini.plm.build/db/cassini.platform/src/main

echo "Copying PLM schema files..."
cp -R src/main/db target/cassini.plm.build/db/cassini.plm/src/main

echo "Copying webapp files..."
mkdir target/cassini.plm.build/webapp

cp -R target/cassini.plm target/cassini.plm.build/webapp

echo "Creating the archive file..."
cd target
DATE=`date +%Y-%m-%d`
tar -czf cassini.plm.$DATE.tar.gz cassini.plm.build
cd ..

echo "Finished packaging!"
echo ""
