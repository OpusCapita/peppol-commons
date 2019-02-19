#!/usr/bin/env bash
GRADLE_PROPERTIES=gradle.properties
export GRADLE_PROPERTIES
echo "Gradle Properties should exist at $GRADLE_PROPERTIES"

if [ ! -f "$GRADLE_PROPERTIES" ]; then
    echo "Gradle Properties does not exist"

    echo "Creating Gradle Properties file..."
    touch $GRADLE_PROPERTIES

    echo "Reading version, increasing and writing again.."
    version_file=VERSION
    IFS='.' read -r -a raw_version <<< "$(cat "$version_file")"
    min_version=${raw_version[2]}
    final_version="${raw_version[0]}.${raw_version[1]}.$(($min_version + 1))"
    echo "$final_version" > $version_file
    echo "version=$final_version" >> $GRADLE_PROPERTIES
    git commit $version_file -m "Version: $final_version"
    git status
    git push origin master

    echo $GPG_PRIVATE_KEY | base64 --decode > secret.pgp

    echo "signing.keyId=$SIGNING_KEY_ID" >> $GRADLE_PROPERTIES
    echo "signing.password=$SIGNING_PASSWORD" >> $GRADLE_PROPERTIES
    echo "signing.secretKeyRingFile=secret.pgp" >> $GRADLE_PROPERTIES
    echo "nexusUsername=$NEXUS_USERNAME" >> $GRADLE_PROPERTIES
    echo "nexusPassword=$NEXUS_PASSWORD" >> $GRADLE_PROPERTIES
fi
