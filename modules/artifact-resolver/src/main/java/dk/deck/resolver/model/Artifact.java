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
package dk.deck.resolver.model;

/**
 * Small entiry that represents an artifact
 *
 * @author Jesper Terkelsen
 */
public class Artifact {

    private String groupId;
    private String artifactId;
    private String version;
    private String packaging;
    private String classifier;

    public Artifact() {
    }

    public Artifact(String groupId, String artifactId, String version, String packaging) {
        this(groupId, artifactId, version, packaging, "");
    }
    
    public Artifact(String groupId, String artifactId, String version, String packaging, String classifier) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.packaging = packaging;
        this.classifier = classifier;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileName(){
        String artifactFilename = getArtifactId() + "-" + getVersion() + "." + getPackaging();
        if (!getClassifier().isEmpty()) {
            artifactFilename = getArtifactId() + "-" + getVersion() + "-" + getClassifier() + "." + getPackaging();
        }
        return artifactFilename;        
    }
    /**
     * Parse a coords string with a version to get the Artifact
     * 
     * @param coords Format groupid:artifactid:packaging or groupid:artifactid:classifier:packaging
     * @param version The version
     * @return a Artifact entiry with the coordinates 
     */
    public static Artifact getArtifact(String coords, String version) {
        String[] values = coords.split(":");
        if (values.length == 3) {
            return new Artifact(values[0], values[1], version, values[2], "");
        } else if (values.length == 4) {
            return new Artifact(values[0], values[1], version, values[3], values[2]);
        } else {
            throw new IllegalArgumentException("Malformed property value for coords " + coords);
        }
    }    
    
    @Override
    public String toString() {
        return groupId+":"+artifactId+":"+classifier+":"+version+":"+packaging;
    }
    
}
