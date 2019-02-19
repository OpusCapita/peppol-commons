#!/usr/bin/env bash
GRADLE_PROPERTIES=gradle.properties
export GRADLE_PROPERTIES
echo "Gradle Properties should exist at $GRADLE_PROPERTIES"

if [ ! -f "$GRADLE_PROPERTIES" ]; then
    echo "Gradle Properties does not exist"

    echo "Creating Gradle Properties file..."
    touch $GRADLE_PROPERTIES

    echo "Reading version and adding it to properties"
    # get major version info from version file
    IFS='.' read -r -a raw_major_version <<< "$(cat "VERSION")"
    # get minor version info from git tags
    IFS='.' read -r -a raw_minor_version <<< "$(git describe --tags --abbrev=0)"

    min_version=${raw_minor_version[2]}
    final_version="${raw_major_version[0]}.${raw_major_version[1]}.$(($min_version + 1))"
    echo "version=$final_version" >> $GRADLE_PROPERTIES

    echo "Tagging new version to github"
    git config --global user.email "$GIT_EMAIL"
    git config --global user.name "$GIT_NAME"
    git tag "$final_version"
    git push origin "$final_version"

    echo $GPG_PRIVATE_KEY | base64 --decode > secret.pgp

    echo "signing.keyId=$SIGNING_KEY_ID" >> $GRADLE_PROPERTIES
    echo "signing.password=$SIGNING_PASSWORD" >> $GRADLE_PROPERTIES
    echo "signing.secretKeyRingFile=secret.pgp" >> $GRADLE_PROPERTIES
    echo "nexusUsername=$NEXUS_USERNAME" >> $GRADLE_PROPERTIES
    echo "nexusPassword=$NEXUS_PASSWORD" >> $GRADLE_PROPERTIES
fi
