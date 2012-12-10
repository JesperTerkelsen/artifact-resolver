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

import dk.deck.resolver.ArtifactResolverLocal;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import dk.deck.resolver.model.Artifact;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Jesper Terkelsen
 */
@Ignore
public class ArtifactResolverLocalTest {
    
    public ArtifactResolverLocalTest() {
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
     * Test of ensureExistance method, of class ArtifactResolverLocal.
     */
    @Test
    public void testEnsureExistance() throws Exception {
        System.out.println("ensureExistance");
        File workdir = new File("target/resolver-local");
        workdir.mkdirs();        
        String groupId = "junit";
        String artifactId = "junit";
        String version = "4.11";
        String packaging = "jar";
        // String classifier = "test";
        // Artifact artifact = new Artifact(groupId, artifactId, version, packaging, classifier);
        Artifact artifact = new Artifact(groupId, artifactId, version, packaging);
        List<Artifact> artifacts = new ArrayList<Artifact>();
        artifacts.add(artifact);
        
        ArtifactResolverLocal instance = new ArtifactResolverLocal(System.getProperty("user.home") + "/.m2/repository");
        instance.ensureExistance(workdir, artifacts);
    }
}
