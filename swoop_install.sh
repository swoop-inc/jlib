#!/bin/sh
SNAPSHOT_OR_RELEASE=${1:-snapshot}
VERSION=${2:-0.2-SNAPSHOT}
mvn deploy:deploy-file -DgroupId=com.swoop -DartifactId=jlib -Dversion=$VERSION -DpomFile=pom.xml -Dpackaging=jar -Dfile=target/jlib-$VERSION.jar -Durl=dav:https://repository-swoop.forge.cloudbees.com/${SNAPSHOT_OR_RELEASE}/ -DrepositoryId=swoop-${SNAPSHOT_OR_RELEASE}
