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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dk.deck.resolver.model.Artifact;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Jesper Terkelsen
 */
public abstract class AbstractArtifactResolver implements ArtifactResolver {
    
    
    protected String getArtifactItemPath(Artifact artifact){
        String artifactPath = artifact.getGroupId().replaceAll(Pattern.quote("."), Matcher.quoteReplacement("/")) + "/" + artifact.getArtifactId();
        String artifactVersionPath = artifactPath + "/" + artifact.getVersion();
        String artifactFilename = artifact.getFileName();
        String artifactItemPath = artifactVersionPath + "/" + artifactFilename;        
        return artifactItemPath;
    }
    
    protected String getMd5(File file) throws IOException {
        String md5Hex = DigestUtils.md5Hex(new FileInputStream(file));
        return md5Hex;
    }
    
    protected boolean verifyMd5(String md5, File file) throws IOException {
        String md5Hex = DigestUtils.md5Hex(new FileInputStream(file));
        if (!md5Hex.toLowerCase().equals(md5.toLowerCase())) {
            return false;
        } else {
            return true;
        }
    }
}
