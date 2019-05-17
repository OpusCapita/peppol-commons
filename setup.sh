#!/usr/bin/env bash
GRADLE_PROPERTIES=gradle.properties
export GRADLE_PROPERTIES
echo "Gradle Properties should exist at $GRADLE_PROPERTIES"

if [ ! -f "$GRADLE_PROPERTIES" ]; then
    echo "Gradle Properties does not exist"

    echo "Creating Gradle Properties file..."
    touch $GRADLE_PROPERTIES


    echo "nexusUsername=$NEXUS_USERNAME" >> $GRADLE_PROPERTIES
    echo "nexusPassword=$NEXUS_PASSWORD" >> $GRADLE_PROPERTIES
fi
