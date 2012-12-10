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
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import dk.deck.resolver.model.Artifact;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * A artifact resolver that uses a local repository instead of remotes
 *
 *
 * @author Jesper Terkelsen
 */
public class ArtifactResolverLocal extends AbstractArtifactResolver {

    private static final Log log = LogFactory.getLog(ArtifactResolverRemote.class);
    private String localRepository;

    public ArtifactResolverLocal(String localRepository) {
        this.localRepository = localRepository;
    }

    @Override
    public int ensureExistance(File workdir, List<Artifact> artifacts) throws IOException, SAXException, ParseException {
        int countDownloaded = 0;
        for (Artifact artifact : artifacts) {
            boolean downloaded = ensureExistance(workdir, artifact);
            if (downloaded){
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
            copyArtifact(workdir, artifact);
            return true;
        } else {
            String md5 = getMd5(localFile);
            String remoteMd5 = getLocalArtifactMd5(artifact);
            if (!md5.toLowerCase().equals(remoteMd5.toLowerCase())) {
                log.info("File " + localFile.getName() + " needs updates re-downloading");
                localFile.delete();
                copyArtifact(workdir, artifact);
                return true;
            } else {
                log.info("File " + localFile.getName() + " exists locally, skipping");
                return false;
            }
        }
    }

    private void copyArtifact(File workdir, Artifact artifact) throws IOException {
        String path = localRepository + "/" + getArtifactItemPath(artifact);
        File localFile = new File(path);
        if (!localFile.exists()) {
            throw new IllegalStateException("File " + path + " does not exists ");
        }
        File output = new File(workdir.getAbsolutePath() + "/" + artifact.getFileName());
        FileUtils.copyFile(localFile, output);

    }

    private String getLocalArtifactMd5(Artifact artifact) throws IOException {
        String path = localRepository + "/" + getArtifactItemPath(artifact);
        File localFile = new File(path);
        if (!localFile.exists()) {
            throw new IllegalStateException("File " + path + " does not exists ");
        }
        return getMd5(localFile);
    }

    @Override
    public List<MavenVersion> getVersionsAvalible(Artifact artifact) throws IOException, DownloadException, SAXException, ParseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
