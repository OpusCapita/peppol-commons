#!/usr/bin/env bash
GRADLE_PROPERTIES=gradle.properties
export GRADLE_PROPERTIES
echo "Gradle Properties should exist at $GRADLE_PROPERTIES"

if [ ! -f "$GRADLE_PROPERTIES" ]; then
    echo "Gradle Properties does not exist"

    echo "Creating Gradle Properties file..."
    touch $GRADLE_PROPERTIES

    echo "Reading version and adding it to properties"
    version_file=VERSION
    IFS='.' read -r -a raw_version <<< "$(cat "$version_file")"
    min_version=${raw_version[2]}
    final_version="${raw_version[0]}.${raw_version[1]}.$(($min_version + 1))"
    echo "$final_version" > $version_file
    echo "version=$final_version" >> $GRADLE_PROPERTIES

    echo "Pushing new version back to github"
    curl -s --header "Authorization: token $GIT_TOKEN" https://github.com/OpusCapita/peppol-commons
    git config --global user.name "$GIT_USER"
    git config --global user.email "$GIT_EMAIL"
    git add VERSION
    git commit -m "Version: $final_version"
    git push

    echo $GPG_PRIVATE_KEY | base64 --decode > secret.pgp

    echo "signing.keyId=$SIGNING_KEY_ID" >> $GRADLE_PROPERTIES
    echo "signing.password=$SIGNING_PASSWORD" >> $GRADLE_PROPERTIES
    echo "signing.secretKeyRingFile=secret.pgp" >> $GRADLE_PROPERTIES
    echo "nexusUsername=$NEXUS_USERNAME" >> $GRADLE_PROPERTIES
    echo "nexusPassword=$NEXUS_PASSWORD" >> $GRADLE_PROPERTIES
fi
