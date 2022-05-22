At first, common and service modules should be built.
Module `common` can be built by following the installation 
instructions in `commmon/README.md`. Then it is necessary to
follow instructions from `service/README.md` for building
`service` module. After that the next command should 
be run to build `client` module:
```shell
mvn clean install
```

### CLI
Initially, you need to run server following guide from
`service/README.md`. It is necessary to run the next command:
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
