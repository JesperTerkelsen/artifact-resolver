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
import org.xml.sax.SAXException;

/**
 *
 * @author Jesper Terkelsen
 */
public interface ArtifactResolver {

    int ensureExistance(File workdir, List<Artifact> artifacts) throws IOException, SAXException, ParseException;

    boolean ensureExistance(File workdir, Artifact artifact) throws IOException, SAXException, ParseException;

    List<MavenVersion> getVersionsAvalible(Artifact artifact) throws IOException, DownloadException, SAXException, ParseException;
    
}
