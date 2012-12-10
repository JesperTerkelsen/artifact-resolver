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

import dk.deck.resolver.ArtifactResolverRemote;
import dk.deck.maven.model.MavenVersion;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import dk.deck.resolver.model.Artifact;
import org.apache.log4j.BasicConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author Jesper Terkelsen
 */
@Ignore("Integration test, using external resources")
public class ArtifactResolverRemoteTest {

    public ArtifactResolverRemoteTest() {
    }
    
    String release = "https://github.com/JesperTerkelsen/deck-mvn-repo/raw/master/releases";
    String snapshots = "https://github.com/JesperTerkelsen/deck-mvn-repo/raw/master/snapshots";
    String[] repos = {release, snapshots};
    String username = "";
    String password = "";

    @BeforeClass
    public static void setUpClass(){
        BasicConfigurator.configure();
    }
    
    @AfterClass
    public static void tearDownClass(){
        BasicConfigurator.resetConfiguration();
    }
    
    @Before
    public void setUp() {
        
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of downloadArtifact method, of class ArtifactResolver.
     */
    @Test
    public void testDownloadSnapshotArtifactFromCoords() throws IOException, SAXException, ParseException {
        System.out.println("testDownloadSnapshotArtifactFromCoords");

        File workdir = new File("target/resolver");
        workdir.mkdirs();
        String coords = "dk.deck.remote-console:remote-console:jar";
        String version = "1.0.7-SNAPSHOT";
       
        Artifact artifact = Artifact.getArtifact(coords, version);

        ArtifactResolverRemote instance = new ArtifactResolverRemote(Arrays.asList(repos), username, password);
        File expResult = new File("target/resolver/remote-console-1.0.7-SNAPSHOT.jar");
        File result = instance.downloadArtifact(workdir, artifact);
        assertEquals(expResult, result);
    }    
    
    
    /**
     * Test of downloadArtifact method, of class ArtifactResolver.
     */
    @Test
    public void testDownloadSnapshotArtifact() throws IOException, SAXException, ParseException {
        System.out.println("testDownloadSnapshotArtifact");

        File workdir = new File("target/resolver");
        workdir.mkdirs();
        String groupId = "dk.deck.remote-console";
        String artifactId = "remote-console";
        String version = "1.0.7-SNAPSHOT";
        String packaging = "jar";
        String classifier = "";
        Artifact artifact = new Artifact(groupId, artifactId, version, packaging, classifier);

        ArtifactResolverRemote instance = new ArtifactResolverRemote(Arrays.asList(repos), username, password);
        File expResult = new File("target/resolver/remote-console-1.0.7-SNAPSHOT.jar");
        File result = instance.downloadArtifact(workdir, artifact);
        assertEquals(expResult, result);
    }

    @Test
    public void testDownloadReleaseArtifact() throws IOException, SAXException, ParseException {
        System.out.println("testDownloadReleaseArtifact");

        File workdir = new File("target/resolver");
        workdir.mkdirs();
        String groupId = "dk.deck.remote-console";
        String artifactId = "remote-console";
        String version = "1.0.6";
        String packaging = "jar";
        String classifier = "";
        Artifact artifact = new Artifact(groupId, artifactId, version, packaging, classifier);

        ArtifactResolverRemote instance = new ArtifactResolverRemote(Arrays.asList(repos), username, password);
        File expResult = new File("target/resolver/remote-console-1.0.6.jar");
        File result = instance.downloadArtifact(workdir, artifact);
        assertEquals(expResult, result);
    }

    /**
     * Test of getVersionsAvalible method, of class ArtifactResolverRemote.
     */
    @Test
    public void testGetVersionsAvalible() throws Exception {
        System.out.println("getVersionsAvalible");

        File workdir = new File("target/resolver");
        workdir.mkdirs();
        String groupId = "dk.deck.remote-console";
        String artifactId = "remote-console";
        String version = "1.0.6";
        String packaging = "jar";
        String classifier = "";
        Artifact artifact = new Artifact(groupId, artifactId, version, packaging, classifier);

        ArtifactResolverRemote instance = new ArtifactResolverRemote(Arrays.asList(repos), username, password);

        List<MavenVersion> versions = instance.getVersionsAvalible(artifact);
        Collections.sort(versions, MavenVersion.DEFAULT_COMPARATOR);
        for (MavenVersion mavenVersion : versions) {
            System.out.println(mavenVersion.toString());
        }

    }
}
