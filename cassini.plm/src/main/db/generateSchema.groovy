#!/usr/bin/env groovy

// Check the number of arguments and print usage
if (this.args.length < 3) {
    println();
    println("ERROR: Insufficient arguments");
    println("USAGE: generateSchema.groovy <psql_command(full path on windows)> <host_name> <schema_name>");
    println("EXAMPLE: generateSchema.groovy psql localhost cassini");
    println();
    return;
}

def psql = this.args[0];

def host = this.args[1];
if (host == null) {
    System.getProperty("db.host");
}

if (host == null) {
    host = "localhost";
}

def schemaName = this.args[2];
if (schemaName == null) {
    throw new RuntimeException("Schema name is required");
}

def pool = ['a'..'z',1..9].flatten()
//def pool = ['a'..'z'].flatten()
Random random = new Random(System.currentTimeMillis())
def randomChars = (0..6).collect { pool[random.nextInt(pool.size())] }
def randomString = randomChars.join()
//def randomString = "cassini"
//this.class.classLoader.rootLoader.addURL( new URL("file:///D:/myHome/dev/cassinisys.apps/cassini.plm/src/main/db/lib"))
this.class.classLoader.rootLoader.addURL(
        new URL("file:///./lib/*"))
import org.mindrot.jbcrypt.*

def hashPwd = BCrypt.hashpw(randomString, BCrypt.gensalt(10));
def hasCode = '\\\'' + hashPwd + '\\\'';

String contents = new File('schema.sql').getText('UTF-8')
contents = contents.replace('$hashPwd', hasCode).replace('$schemaName', schemaName);

def tmpSchemaFile = new File('schema.tmp.sql')
tmpSchemaFile << contents;

String cmd = sprintf("%s -h %s -p 5432 -Ucassinisys -t -P pager=off -f schema.tmp.sql cassiniapps", psql, host);
def process = new ProcessBuilder(cmd.split(" ")).redirectErrorStream(true).start()
process.inputStream.eachLine { println it }

tmpSchemaFile.delete();

if (process.exitValue() != 0) {
    System.exit(process.exitValue());
}

println("Administrator login password is: " +randomString);