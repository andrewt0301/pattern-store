At first, common and service modules should be built.
It can be done by using `mvn clean install` from a particular
directory. After that run `mvn clean install` from the 
directory of client.

### CLI
It is necessary to run the following command

```shell
java -cp "target/client-1.0-SNAPSHOT-jar-with-dependencies.jar" aakrasnov.diploma.client.ClientApplication stata --docId hexDocId
```

### Cache
Local client cache implementation contains an index file.
This file should be located in the directory with documents
which are cached. It is worth noticing that path to the 
cached document should be relative to the index file. 
Example of directory with index file and two cached 
documents: 
```
$prefix$
| -index.json
| +somePathDoc1
  | -cachedDocId1.json
| -cachedDocId2.json
```
