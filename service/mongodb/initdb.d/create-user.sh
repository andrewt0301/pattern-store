#!/bin/bash
mongo "$MONGO_INITDB_DATABASE" --eval "db.createUser({ roles: [{ role: 'dbOwner', db: '$MONGO_INITDB_DATABASE' }] })"