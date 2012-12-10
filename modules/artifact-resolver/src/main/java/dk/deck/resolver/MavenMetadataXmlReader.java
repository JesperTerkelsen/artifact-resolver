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

import java.io.IOException;
import java.io.InputStream;
import dk.deck.resolver.model.Metadata;
import dk.deck.resolver.model.Snapshot;
import dk.deck.resolver.model.Version;
import dk.deck.resolver.model.Versioning;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * Reads the xml metadata
 *
 * @author Jesper Terkelsen
 */
public class MavenMetadataXmlReader {

    private Digester digester;

    public MavenMetadataXmlReader() {
        digester = new Digester();
        digester.setValidating(false);
        digester.addObjectCreate("metadata", Metadata.class);
        digester.addCallMethod("metadata/artifactId", "setArtifactId", 0);
        digester.addCallMethod("metadata/groupId", "setGroupId",0);
        digester.addCallMethod("metadata/version", "setVersion",0);
        

        digester.addObjectCreate("metadata/versioning", Versioning.class);
        digester.addCallMethod("metadata/versioning/latest", "setLatest", 0);
        digester.addCallMethod("metadata/versioning/release", "setRelease", 0);
        digester.addCallMethod("metadata/versioning/lastUpdated", "setLastUpdated", 0);
        digester.addSetNext("metadata/versioning", "setVersioning");
        
        digester.addObjectCreate("metadata/versioning/versions/version", Version.class);
        digester.addCallMethod("metadata/versioning/versions/version", "setVersion",0);
        digester.addSetNext("metadata/versioning/versions/version", "addVersion" );
        
        digester.addObjectCreate("metadata/versioning/snapshot", Snapshot.class);
        digester.addCallMethod("metadata/versioning/snapshot/timestamp", "setTimestamp", 0);
        digester.addCallMethod("metadata/versioning/snapshot/buildNumber", "setBuildNumber", 0);
        digester.addSetNext("metadata/versioning/snapshot", "setSnapshot");
        
    }

    public Metadata getMetadata(InputStream xml) throws IOException, SAXException {
        return (Metadata) digester.parse(xml);
    }

}
