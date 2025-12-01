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

String contents = new File('schema.sql').getText('UTF-8')
contents = contents.replaceAll('\\$schemaName', schemaName);

def tmpSchemaFile = new File('schema.tmp.sql')
tmpSchemaFile << contents;

String cmd = sprintf("%s -h %s -p 5432 -Ucassinisys -t -P pager=off -f schema.tmp.sql cassiniapps", psql, host);
def process = new ProcessBuilder(cmd.split(" ")).redirectErrorStream(true).start()
process.inputStream.eachLine { println it }

tmpSchemaFile.delete();

if (process.exitValue() != 0) {
    System.exit(process.exitValue());
}