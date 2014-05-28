#!/bin/bash

# Should fail if any steps in this script fail
set -e

dir=`mktemp -d -t "jlib-dependencies-XXXXX"`

echo
echo "Setting up Redis $REDIS_VERSION using temp folder: $dir"
echo

cd $dir
wget http://download.redis.io/releases/redis-$REDIS_VERSION.tar.gz
tar xzf redis-$REDIS_VERSION.tar.gz
cd redis-$REDIS_VERSION
make
make PREFIX=$SWOOP_BIN_PATH/.. install

echo
echo "Setting up MongoDB $MONGODB_VERSION using temp folder: $dir"
echo

cd $dir
wget http://downloads.mongodb.org/linux/mongodb-linux-x86_64-$MONGODB_VERSION.tgz
tar xzf mongodb-linux-x86_64-$MONGODB_VERSION.tgz
cd mongodb-linux-x86_64-$MONGODB_VERSION/bin
for executable in $(ls); do
  echo "linking `pwd`/$executable to $SWOOP_BIN_PATH/$executable"
  ln -s `pwd`/$executable $SWOOP_BIN_PATH/$executable
done

echo "redis-server used: $(which redis-server)"
echo "mongod used: $(which mongod)"
