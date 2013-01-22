#artifact-resolver

Small java lib for downloading files from a public maven archives, wihout depending on maven to do so.

This is usefull inside installers that will rollout some released artifact to multible servers.

## Download and use
Use maven
```xml
<repository>
    <id>deck-repo</id>
    <url>https://github.com/JesperTerkelsen/deck-mvn-repo/raw/master/releases</url>
</repository>
<dependency>
    <artifactId>artifact-resolver</artifactId>
    <groupId>dk.deck.artifact-resolver</groupId>
    <version>1.0.0</version>
    <type>jar</type>
</dependency>
```

## How to use
```java
// Define credentials for your repositries
String username = "installer-user";
String password = "secret";
// Define the repositories
List<String> repositories = new ArrayList<String>();
repositories.add("http://repo.example.com/libs-release-local");
repositories.add("http://repo.example.com/libs-snapshot-local");
// Create a resolver for downloading artifacts
ArtifactResolver resolver = new ArtifactResolverRemote(repositories, username, password);
// Where to download artifacts to
File workdir = new File("target");
// Define the artifact you want to download, this is an example, use your own dependencies here
String groupId = "dk.deck.artifact-resolver";
String artifactId = "artifact-resolver";
String version = "1.0.0";
String type = "jar";
String classifier = null;
Artifact artifact = new Artifact(groupid, artifactId, version, type, classifier);
// Download the artifact to the workdir folder
resolver.ensureExistance(workdir, artifact);
```
