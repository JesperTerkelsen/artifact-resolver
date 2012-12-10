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
package dk.deck.resolver.util;

import dk.deck.resolver.util.ZipUtils;
import java.io.File;
import java.util.List;
import org.junit.*;

/**
 *
 * @author Jesper Terkelsen
 */
@Ignore
public class ZipUtilsTest {
    
    public ZipUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    private File archive;
    
    @Before
    public void setUp() {
        // We use a provided war file, for this test.
        // This means that this test has to be run manually, bit this is better than comitting a zipped archive to resources
        archive = new File("/Users/jester/work/portal_frontend-1.2.4-SNAPSHOT.war");
    }
    
    @After
    public void tearDown() {
        archive = null;
    }

    /**
     * Test of listArchive method, of class ZipUtils.
     */
    @Test
    public void testListArchive() {
        System.out.println("listArchive");
        ZipUtils instance = new ZipUtils();
        List<String> result = instance.listArchive(archive);
        for (String string : result) {
            System.out.println(string);
        }
    }

    /**
     * Test of unzipArchive method, of class ZipUtils.
     */
    @Test
    public void testUnzipArchive() {
//        System.out.println("unzipArchive");
//        File archive = null;
//        File outputDir = null;
//        ZipUtils instance = new ZipUtils();
//        instance.unzipArchive(archive, outputDir);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}
