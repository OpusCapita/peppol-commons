#!/usr/bin/env bash
GRADLE_PROPERTIES=gradle.properties
export GRADLE_PROPERTIES
echo "Gradle Properties should exist at $GRADLE_PROPERTIES"

if [ ! -f "$GRADLE_PROPERTIES" ]; then
    echo "Gradle Properties does not exist"

    echo "Creating Gradle Properties file..."
    touch $GRADLE_PROPERTIES

    while read -r line; do
        echo "version=$line" >> $GRADLE_PROPERTIES
    done < "VERSION"

    echo $GPG_PRIVATE_KEY | base64 --decode > secret.pgp

    echo "signing.keyId=$SIGNING_KEY_ID" >> $GRADLE_PROPERTIES
    echo "signing.password=$SIGNING_PASSWORD" >> $GRADLE_PROPERTIES
    echo "signing.secretKeyRingFile=secret.pgp" >> $GRADLE_PROPERTIES
    echo "nexusUsername=$NEXUS_USERNAME" >> $GRADLE_PROPERTIES
    echo "nexusPassword=$NEXUS_PASSWORD" >> $GRADLE_PROPERTIES
fi
