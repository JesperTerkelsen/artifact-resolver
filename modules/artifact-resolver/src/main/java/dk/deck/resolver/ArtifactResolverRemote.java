/*
 * Copyright 2012 Jesper Terkelsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 */
package dk.deck.resolver;

import dk.deck.maven.model.MavenVersion;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dk.deck.resolver.model.Artifact;
import dk.deck.resolver.model.Metadata;
import dk.deck.resolver.model.Snapshot;
import dk.deck.resolver.model.Version;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

/**
 * Lightweight client that can download artifacts from a maven repository.
 *
 * We do not want to embed maven, because the arcitecture requers a local
 * repository, we only want to download directly
 *
 * Todo add md5 and sha1 checks
 *
 * @author Jesper Terkelsen
 */
public class ArtifactResolverRemote extends AbstractArtifactResolver {

    private static final Log log = LogFactory.getLog(ArtifactResolverRemote.class);
    private final List<String> repositories;
    private final DefaultHttpClient httpclient;
    public static final int MAX_HTTP_THREADS = 10;

    public ArtifactResolverRemote(List<String> repositories, String username, String password) {
        this.repositories = Collections.unmodifiableList(repositories);
        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        connectionManager.setMaxTotal(MAX_HTTP_THREADS);
        connectionManager.setDefaultMaxPerRoute(MAX_HTTP_THREADS);
        httpclient = new DefaultHttpClient(connectionManager);

        List<String> authpref = new ArrayList<String>();
        authpref.add(AuthPolicy.BASIC);
        authpref.add(AuthPolicy.DIGEST);
        httpclient.getParams().setParameter(AuthPNames.PROXY_AUTH_PREF, authpref);
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
        httpclient.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);

    }

    @Override
    public int ensureExistance(File workdir, List<Artifact> artifacts) throws IOException, SAXException, ParseException {
        int countDownloaded = 0;
        for (Artifact artifact : artifacts) {
            boolean downloaded = ensureExistance(workdir, artifact);
            if (downloaded) {
                countDownloaded++;
            }
        }
        return countDownloaded;
    }

    @Override
    public boolean ensureExistance(File workdir, Artifact artifact) throws IOException, SAXException, ParseException {
        // Check if file exists
        String fileName = artifact.getFileName();
        File localFile = new File(workdir + "/" + fileName);
        if (!localFile.exists()) {
            log.info("File " + localFile.getName() + " does not exists locally downloading");
            downloadArtifact(workdir, artifact);
            return true;
        } else {
            String md5 = getMd5(localFile);
            String remoteMd5 = downloadArtifactMd5(artifact);
            if (!md5.toLowerCase().equals(remoteMd5.toLowerCase())) {
                log.info("File " + localFile.getName() + " needs updates re-downloading");
                localFile.delete();
                downloadArtifact(workdir, artifact);
                return true;
            } else {
                log.info("File " + localFile.getName() + " exists locally, skipping");
                return false;
            }
        }
    }

    public File downloadArtifact(File workdir, Artifact artifact) throws IOException, SAXException, ParseException {
        if (!workdir.exists()) {
            throw new IllegalArgumentException("Workdir does not exist");
        }
        DownloadException lastFault = null;
        for (String repo : repositories) {
            try {
                return downloadArtifact(workdir, repo, artifact);
            } catch (DownloadException ex) {
                // Cannot find artifact in this repo
                // This might now be a problem if it is in another repo
                lastFault = ex;
            }
        }
        throw new IllegalStateException("Cannot find artifact " + artifact + " in repositories " + repositories, lastFault);
    }

    public String downloadArtifactMd5(Artifact artifact) throws IOException, SAXException, ParseException {
        DownloadException lastFault = null;
        for (String repo : repositories) {
            try {
                return downloadArtifactMd5(repo, artifact);
            } catch (DownloadException ex) {
                // Cannot find artifact in this repo
                // This might now be a problem if it is in another repo
                lastFault = ex;
            }
        }
        throw new IllegalStateException("Cannot find artifact " + artifact + " in repositories " + repositories, lastFault);
    }

    public String downloadArtifactMd5(String repo, Artifact artifact) throws IOException, DownloadException, SAXException, ParseException {
        // Check that the requested version exists, then get the version meta data
        MavenVersion version = verifyVersionInRepo(repo, artifact);
        if (version.isSnapshot()) {
            Metadata snapshotMetaData = getSnapshptMetaData(repo, artifact);
            Snapshot snapshot = snapshotMetaData.getVersioning().getSnapshot();
            if (snapshot != null) {
                String buildNumber = snapshot.getBuildNumber();
                String timestamp = snapshot.getTimestamp();
                String md5 = getSnapshotMd5(repo, artifact, timestamp, buildNumber, version);
                return md5;
            } else {
                // Try directly on the version
                String md5 = getArtifactMd5(repo, artifact);
                return md5;
            }
        } else {
            String md5 = getArtifactMd5(repo, artifact);
            return md5;
        }
    }

    public File downloadArtifact(File workdir, String repo, Artifact artifact) throws IOException, DownloadException, SAXException, ParseException {
        // Check that the requested version exists, then get the version meta data
        MavenVersion version = verifyVersionInRepo(repo, artifact);
        if (version.isSnapshot()) {
            Metadata snapshotMetaData = getSnapshptMetaData(repo, artifact);
            Snapshot snapshot = snapshotMetaData.getVersioning().getSnapshot();
            if (snapshot != null) {
                String buildNumber = snapshot.getBuildNumber();
                String timestamp = snapshot.getTimestamp();
                String md5 = getSnapshotMd5(repo, artifact, timestamp, buildNumber, version);
                File output = getSnapshot(workdir, repo, artifact, timestamp, buildNumber, version);
                if (!verifyMd5(md5, output)) {
                    throw new DownloadException("Md5 does not match for " + artifact);
                }
                return output;
            } else {
                // Try directly on the version
                String md5 = getArtifactMd5(repo, artifact);
                File output = getArtifact(workdir, repo, artifact);
                if (!verifyMd5(md5, output)) {
                    throw new DownloadException("Md5 does not match for " + artifact);
                }
                return output;
            }
        } else {
            String md5 = getArtifactMd5(repo, artifact);
            File output = getArtifact(workdir, repo, artifact);
            if (!verifyMd5(md5, output)) {
                throw new DownloadException("Md5 does not match for " + artifact);
            }
            return output;
        }
    }

    private MavenVersion verifyVersionInRepo(String repo, Artifact artifact) throws IOException, DownloadException, SAXException, ParseException {
        Metadata artifactMetaData = getArtifactMetaData(repo, artifact);

        if (!artifact.getGroupId().equals(artifactMetaData.getGroupId())) {
            throw new DownloadException("Got groupId " + artifactMetaData.getGroupId() + " which does not match " + artifact.getGroupId());
        }
        if (!artifact.getArtifactId().equals(artifactMetaData.getArtifactId())) {
            throw new DownloadException("Got artifactId " + artifactMetaData.getArtifactId() + " which does not match " + artifact.getArtifactId());
        }

        boolean foundVersion = false;
        // Check that version exists
        List<Version> versions = artifactMetaData.getVersioning().getVersions();
        for (Version version : versions) {
            if (version.getVersion().equals(artifact.getVersion())) {
                foundVersion = true;
            }
        }
        if (!foundVersion) {
            throw new DownloadException("Version " + artifact.getVersion() + " not found on server");
        }


        // Check that the requested version exists, then get the version meta data
        MavenVersion version = MavenVersion.parse(artifact.getVersion());
        return version;
    }

    @Override
    public List<MavenVersion> getVersionsAvalible(Artifact artifact) throws IOException, DownloadException, SAXException, ParseException {
        Set<MavenVersion> result = new HashSet<MavenVersion>();
        for (String repo : repositories) {
            Metadata artifactMetaData = getArtifactMetaData(repo, artifact);

            if (!artifact.getGroupId().equals(artifactMetaData.getGroupId())) {
                throw new DownloadException("Got groupId " + artifactMetaData.getGroupId() + " which does not match " + artifact.getGroupId());
            }
            if (!artifact.getArtifactId().equals(artifactMetaData.getArtifactId())) {
                throw new DownloadException("Got artifactId " + artifactMetaData.getArtifactId() + " which does not match " + artifact.getArtifactId());
            }
            List<Version> versions = artifactMetaData.getVersioning().getVersions();

            for (Version version : versions) {
                result.add(MavenVersion.parse(version.getVersion()));
            }
        }
        return new ArrayList<MavenVersion>(result);
    }

    private File getSnapshot(File workdir, String repo, Artifact artifact, String timestamp, String buildNumber, MavenVersion version) throws IOException, DownloadException {
        String artifactItemPath = getSnapshotItemPath(artifact, version, timestamp, buildNumber);
        String getUrl = repo + "/" + artifactItemPath;
        return getFile(getUrl, artifact.getFileName(), workdir, repo, artifact);
    }

    private String getSnapshotMd5(String repo, Artifact artifact, String timestamp, String buildNumber, MavenVersion version) throws IOException, DownloadException {
        String artifactItemPath = getSnapshotItemPath(artifact, version, timestamp, buildNumber);
        String getUrl = repo + "/" + artifactItemPath + ".md5";
        String md5 = getContent(getUrl, repo, artifact).trim();
        return md5;
    }

    private String getArtifactMd5(String repo, Artifact artifact) throws IOException, DownloadException {
        String artifactItemPath = getArtifactItemPath(artifact);
        String getUrl = repo + "/" + artifactItemPath + ".md5";
        String md5 = getContent(getUrl, repo, artifact).trim();
        return md5;
    }

    private File getArtifact(File workdir, String repo, Artifact artifact) throws IOException, DownloadException {
        String artifactItemPath = getArtifactItemPath(artifact);
        String getUrl = repo + "/" + artifactItemPath;
        return getFile(getUrl, artifact.getFileName(), workdir, repo, artifact);
    }

    private String getSnapshotItemPath(Artifact artifact, MavenVersion version, String timestamp, String buildNumber) {
        String artifactPath = artifact.getGroupId().replaceAll(Pattern.quote("."), Matcher.quoteReplacement("/")) + "/" + artifact.getArtifactId();
        String artifactVersionPath = artifactPath + "/" + artifact.getVersion();

        String snapshotVersion = version.toSnapshotString(timestamp, buildNumber);
        String artifactFilename = artifact.getArtifactId() + "-" + snapshotVersion + "." + artifact.getPackaging();
        if (!artifact.getClassifier().isEmpty()) {
            artifactFilename = artifact.getArtifactId() + "-" + snapshotVersion + "-" + artifact.getClassifier() + "." + artifact.getPackaging();
        }

        String artifactItemPath = artifactVersionPath + "/" + artifactFilename;
        return artifactItemPath;
    }

    private String getContent(String getUrl, String repo, Artifact artifact) throws IOException, DownloadException {
        log.debug("Downloading: " + getUrl);
        HttpGet httpget = new HttpGet(getUrl);
        HttpResponse response = httpclient.execute(httpget);
        int status = response.getStatusLine().getStatusCode();
        if (status != 200) {
            EntityUtils.consume(response.getEntity());
            throw new DownloadException("MD5 for artifact " + artifact + " not in repo " + repo + ": status code is " + status);
        }
        return IOUtils.toString(response.getEntity().getContent());
    }

    private File getFile(String getUrl, String fileName, File workdir, String repo, Artifact artifact) throws IOException, DownloadException {
        log.info("Downloading: " + getUrl);
        HttpGet httpget = new HttpGet(getUrl);
        HttpResponse response = httpclient.execute(httpget);
        int status = response.getStatusLine().getStatusCode();
        if (status != 200) {
            EntityUtils.consume(response.getEntity());
            throw new DownloadException("Artifact " + artifact + " not in repo " + repo + ": status code is " + status);
        }
        log.debug("Size: " + fileName + " " + response.getEntity().getContentLength());
        File output = new File(workdir + "/" + fileName);
        FileOutputStream out = new FileOutputStream(output);
        IOUtils.copy(response.getEntity().getContent(), out);
        out.flush();
        out.close();
        EntityUtils.consume(response.getEntity());
        return output;
    }

    private Metadata getSnapshptMetaData(String repo, Artifact artifact) throws IOException, DownloadException, SAXException {
        String artifactPath = artifact.getGroupId().replaceAll(Pattern.quote("."), Matcher.quoteReplacement("/")) + "/" + artifact.getArtifactId();
        String artifactMetaData = artifactPath + "/" + artifact.getVersion() + "/maven-metadata.xml";
        String getUrl = repo + "/" + artifactMetaData;

        return getMetadata(repo, artifact, getUrl);
    }

    private Metadata getArtifactMetaData(String repo, Artifact artifact) throws IOException, DownloadException, SAXException {
        String artifactPath = artifact.getGroupId().replaceAll(Pattern.quote("."), Matcher.quoteReplacement("/")) + "/" + artifact.getArtifactId();
        String artifactMetaData = artifactPath + "/maven-metadata.xml";
        String getUrl = repo + "/" + artifactMetaData;

        return getMetadata(repo, artifact, getUrl);
    }

    private Metadata getMetadata(String repo, Artifact artifact, String getUrl) throws IOException, DownloadException, SAXException {
        log.debug("Downloading: " + getUrl);
        HttpGet httpget = new HttpGet(getUrl);
        HttpResponse response = httpclient.execute(httpget);
        int status = response.getStatusLine().getStatusCode();
        if (status != 200) {
            EntityUtils.consume(response.getEntity());
            throw new DownloadException("Artifact " + artifact + " not in repo " + repo + ": status code is " + status);
        }

        String contentType = response.getEntity().getContentType().getValue();
        // Artifactory returns "application/xml" github returns "text/plain; charset=utf-8" 
        if (!contentType.equals("application/xml") && !contentType.startsWith("text/plain")) {
            EntityUtils.consume(response.getEntity());
            throw new DownloadException("Artifact " + artifact + " not in repo " + repo + ": content type is " + contentType);
        }
        MavenMetadataXmlReader reader = new MavenMetadataXmlReader();
        return (Metadata) reader.getMetadata(response.getEntity().getContent());
    }
}
