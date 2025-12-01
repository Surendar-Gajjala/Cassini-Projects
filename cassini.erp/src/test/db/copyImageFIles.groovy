import groovy.io.FileType

def root = "/Users/reddy/MyHome/CassiniSys/Customers/DPPL/Data/books/Dachepalli Books";

def rootDir = new File(root);
rootDir.eachFileRecurse (FileType.FILES) { file ->
    def name = file.name;
    if(name.endsWith(".jpg")) {
        new File(rootDir, name) << file.bytes;
    }
}