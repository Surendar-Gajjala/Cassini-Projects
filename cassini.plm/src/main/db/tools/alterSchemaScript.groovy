#!/usr/bin/env groovy

def schemaNames = ["cassini_plm","emm"];

def psql = this.args[0];

def host = this.args[1];

if (host == null) {
    System.getProperty("db.host");
}

if (host == null) {
    host = "localhost";
}


for(int i = 0; i < schemaNames.size();i++){
    def schemaName = schemaNames[i];

    String contents = new File('alter_statements.sql').getText('UTF-8')
    contents = contents.replace('$schemaName', schemaName);

    def tmpSchemaFile = new File('schema.tmp.sql')
    tmpSchemaFile << contents;

    String cmd = sprintf("%s -h %s -p 5432 -Ucassinisys -t -P pager=off -f schema.tmp.sql cassiniapps", psql, host);
    def process = new ProcessBuilder(cmd.split(" ")).redirectErrorStream(true).start()
    process.inputStream.eachLine { println it }

    tmpSchemaFile.delete();

    if (process.exitValue() != 0) {
        System.exit(process.exitValue());
    }

}
