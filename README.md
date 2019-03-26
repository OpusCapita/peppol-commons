# peppol-commons [![CircleCI](https://circleci.com/gh/OpusCapita/peppol-commons.svg?style=svg)](https://circleci.com/gh/OpusCapita/peppol-commons)

Common services for the Peppol services running on Andariel Platform.

Common services include;
- Container Message
- Authorization Infra
- Message Queue Client
- ServiceNow Integration
- Storage Implementations
- EventingService Client
- Commons Rest Endpoints
- Common Error Handler

This project is being published as a library to [Maven Central Repository](https://search.maven.org/search?q=g:com.opuscapita.peppol%20AND%20a:peppol-commons&core=gav)

### Publishing
Every changes pushed to master branch will trigger a new release and auto-publish to maven central. 

For a manuel publish, first you need to populate a gradle.properties file. (note that this file is ignored by git, please DO NOT push it).

```$xslt
# sample gradle.properties file
version=<VERSION_TO_PUBLISH>
signing.keyId=<GPG_KEY_SHORT_ID>
signing.password=<GPG_KEY_PASSWORD>
signing.secretKeyRingFile=<PATH_TO_YOUR_GPG_SECRET_KEY>
nexusUsername=<YOUR_SONATYPE_USERNAME>
nexusPassword=<YOUR_SONATYPE_PASSWORD>
```
After you created gradle.properties file, run the following commands:

```$xslt
# simple clean and build
./gradlew clean
./gradlew build

# sign and upload artifacts to
# sonatype staging repo: https://oss.sonatype.org
./gradlew uploadArchives

# close and release the new version of library
# it will be available in ~10m in Maven Central Repo: http://repo1.maven.org/maven2/com/opuscapita/peppol/peppol-commons/
# and it will be available in ~2h in Maven Central Search: https://search.maven.org/
./gradlew closeAndReleaseRepository
```
If you want to test your changes locally, you can publish to your local repository and depend it from your service. To publish to your local maven repository (`~/m2`) execute:
```
./gradlew publishToMavenLocal
```


### Versioning
The version number has 3 parts: `[major].[minor].[patch]`. 

During circleci build process, the latest git tag is fetched. The patch version is increased by one and pushed back to git as a new tag.

To update major version, you need to create a new git tag manually, and also release it from your local..again manually.
