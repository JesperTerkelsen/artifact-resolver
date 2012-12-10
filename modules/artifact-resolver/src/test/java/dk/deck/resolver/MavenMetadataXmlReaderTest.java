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

import dk.deck.resolver.MavenMetadataXmlReader;
import dk.deck.resolver.model.Version;
import java.util.List;
import dk.deck.resolver.model.Versioning;
import dk.deck.resolver.model.Metadata;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import dk.deck.resolver.model.Snapshot;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jesper Terkelsen
 */
public class MavenMetadataXmlReaderTest {
    
    public MavenMetadataXmlReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getEnvironment method, of class MavenMetadataXmlReader.
     */
    @Test
    public void testGetMetadata1() throws Exception {
        System.out.println("testGetMetadata1");
        String testData ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<metadata>\n"
                + "  <groupId>com.infopaq.env</groupId>\n"
                + "  <artifactId>environment</artifactId>\n"
                + "  <versioning>\n"
                + "    <latest>1.2.0-SNAPSHOT</latest>\n"
                + "    <versions>\n"
                + "      <version>1.1.0-SNAPSHOT</version>\n"
                + "      <version>1.2.0-SNAPSHOT</version>\n"
                + "    </versions>\n"
                + "    <lastUpdated>20110808120607</lastUpdated>\n"
                + "  </versioning>"
                + "</metadata>\n";
        InputStream xml = new ByteArrayInputStream(testData.getBytes());
        MavenMetadataXmlReader instance = new MavenMetadataXmlReader();
        Metadata expResult = null;
        Metadata result = instance.getMetadata(xml);
        assertEquals("environment", result.getArtifactId());
        assertEquals("com.infopaq.env", result.getGroupId());
        assertEquals(null, result.getVersion());
                
        Versioning versioning = result.getVersioning();
        assertEquals("20110808120607",versioning.getLastUpdated());
        assertEquals("1.2.0-SNAPSHOT",versioning.getLatest());
        assertEquals(null,versioning.getRelease());
        List<Version> versions = versioning.getVersions();
        assertEquals(2, versions.size());
        Version version0 = versions.get(0);
        assertEquals("1.1.0-SNAPSHOT", version0.getVersion());        
        Version version1 = versions.get(1);
        assertEquals("1.2.0-SNAPSHOT", version1.getVersion());
        
    }
    
    @Test
    public void testGetMetadata2() throws Exception {
        System.out.println("testGetMetadata2");
        String testData ="<metadata>"
                + "    <groupId>com.infopaq.env</groupId>"
                + "    <artifactId>environment</artifactId>"
                + "    <version>1.2.0-SNAPSHOT</version>"
                + "    <versioning>"
                + "        <snapshot>"
                + "            <timestamp>20110808.120607</timestamp>"
                + "            <buildNumber>62</buildNumber>"
                + "        </snapshot>"
                + "        <lastUpdated>20110808120607</lastUpdated>"
                + "    </versioning>"
                + "</metadata>\n";
        InputStream xml = new ByteArrayInputStream(testData.getBytes());
        MavenMetadataXmlReader instance = new MavenMetadataXmlReader();
        Metadata expResult = null;
        Metadata result = instance.getMetadata(xml);
        assertEquals("environment", result.getArtifactId());
        assertEquals("com.infopaq.env", result.getGroupId());
        assertEquals("1.2.0-SNAPSHOT", result.getVersion());
                
        Versioning versioning = result.getVersioning();
        assertEquals("20110808120607",versioning.getLastUpdated());
        assertEquals(null,versioning.getLatest());
        assertEquals(null,versioning.getRelease());
        
        Snapshot snapshot = versioning.getSnapshot();
        assertEquals("62",snapshot.getBuildNumber());
        assertEquals("20110808.120607",snapshot.getTimestamp());
    }    
}
