In order to build this module it is necessary first build
module `common` following the installation instructions in
`commmon/README.md`. After that the next command should be run
to build `service` module:
```shell
mvn clean install
```
Service starting could be done by using the command from 
service project directory:
```shell
java -jar ./target/service.jar
```
