## Build
In order to build this module it is necessary first build
module `common` following the installation instructions in
`commmon/README.md`. After that the next command should be run
to build `service` module:
```shell
mvn clean install
```
## Start with `docker-compose`
You are able to start application and MongoDn using 
`docker-compose` command. First, you need to create docker
network with following command:
```shell
docker network create ptst-network
```
In this case in `.env` file the value of variable `NETWORK_NAME`
should be equal to `ptst-network`.

This section is optional as it is configured in root project
directory, but you would likely want to change it. 
Next you need to create directories for MongoDb and specify
path to them in `.env` file (e.g. `MONGO_DATA_HOST_PATH`, 
`MONGO_LOG_HOST_PATH`, `MONGO_INITDB_SCRIPTS_HOST_PATH`). 
Examples of path could be found in `.env` file of root 
directory. In the `initdb.d` directory you need to 
create file `create-user.sh` with such content:
```shell
#!/bin/bash
mongo "$MONGO_INITDB_DATABASE" --eval "db.createUser({ roles: [{ role: 'dbOwner', db: '$MONGO_INITDB_DATABASE' }] })"
```
\
After that you should build service with next 
command from root directory:
```shell
# it is possible to move '.env' file and specify an absolute path
docker-compose --env-file ".env" build
```
Finally, you should run this command in order to start MongoDb
and service in detached mode:
```shell
docker-compose --env-file ".env" up -d
```
This command allows to stop container and remove it:
```shell
docker-compose down
```

## Start with cmd
In this case MongoDb server should be started.
Service starting could be done by using the command from 
service project directory:
```shell
java -jar ./target/service.jar
```
