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
package dk.deck.maven.model;

import dk.deck.maven.model.MavenVersion;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
public class MavenVersionTest {
    
    public MavenVersionTest() {
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
     * Test of parse method, of class MavenVersion.
     */
    @Test
    public void testParse1() throws Exception {
        System.out.println("parse");
        String version = "1";
        MavenVersion expResult = new MavenVersion("1", null, null, false);
        MavenVersion result = MavenVersion.parse(version);
        assertEquals(expResult, result);        
    }
    
    /**
     * Test of parse method, of class MavenVersion.
     */
    @Test
    public void testParse102() throws Exception {
        System.out.println("parse");
        String version = "1.0.2";
        MavenVersion expResult = new MavenVersion("1", "0", "2", false);
        MavenVersion result = MavenVersion.parse(version);
        assertEquals(expResult, result);        
    }    

    /**
     * Test of parse method, of class MavenVersion.
     */
    @Test
    public void testParse102SNAPSHOT() throws Exception {
        System.out.println("parse");
        String version = "1.0.2-SNAPSHOT";
        MavenVersion expResult = new MavenVersion("1", "0", "2", true);
        System.out.println("Snapshot tostring " + expResult.toString());
        
        MavenVersion result = MavenVersion.parse(version);
        assertEquals(expResult, result);        
    }    
    
    /**
     * Test of parse method, of class MavenVersion.
     */
    @Test
    public void testParse11m1() throws Exception {
        System.out.println("parse");
        String version = "1.1-m1";
        MavenVersion expResult = new MavenVersion("1", "1-m1", null, false);
        System.out.println("Snapshot tostring " + expResult.toString());
        
        MavenVersion result = MavenVersion.parse(version);
        assertEquals(expResult, result);        
    }       
    
    /**
     * Test of toString method, of class MavenVersion.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        MavenVersion instance = new MavenVersion("1", "0", "8", false);
        String expResult = "1.0.8";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testComparator() throws ParseException{
        System.out.println("comparator");
        List<MavenVersion> list = new ArrayList<MavenVersion>();
        list.add(MavenVersion.parse("1.3.5-SNAPSHOT"));
        list.add(MavenVersion.parse("1.1.10"));
        list.add(MavenVersion.parse("1.1.6"));
        list.add(MavenVersion.parse("1.3.5"));
        list.add(MavenVersion.parse("1.4.3-SNAPSHOT"));
        list.add(MavenVersion.parse("1.2.5"));
        list.add(MavenVersion.parse("1.2.5-SNAPSHOT"));
        list.add(MavenVersion.parse("1.4.3"));
        list.add(MavenVersion.parse("1.0.16"));
        list.add(MavenVersion.parse("1.2.1-SNAPSHOT"));
        list.add(MavenVersion.parse("1.1.34"));
        list.add(MavenVersion.parse("1.5.0-SNAPSHOT"));
        list.add(MavenVersion.parse("1.1.1"));
        list.add(MavenVersion.parse("1.1.1-feature-branch4"));
        list.add(MavenVersion.parse("1.1.15"));
        list.add(MavenVersion.parse("1.1.15-feature-branch1"));
        list.add(MavenVersion.parse("1.1.15-feature-branch2"));
        list.add(MavenVersion.parse("1.1.15-feature-branch3"));
        list.add(MavenVersion.parse("1.2.9-SNAPSHOT"));
        list.add(MavenVersion.parse("1.3.4"));
        list.add(MavenVersion.parse("1.1.22"));
        list.add(MavenVersion.parse("1.1.0"));
        list.add(MavenVersion.parse("1.2.10-SNAPSHOT"));
        list.add(MavenVersion.parse("1.2.9"));
        list.add(MavenVersion.parse("1.2.10"));
        list.add(MavenVersion.parse("1.3.0"));
        list.add(MavenVersion.parse("1.2.4-SNAPSHOT"));
        list.add(MavenVersion.parse("1.2.4"));
        list.add(MavenVersion.parse("1.4.4"));
        list.add(MavenVersion.parse("1.3.4-SNAPSHOT"));
        list.add(MavenVersion.parse("1.3.0-SNAPSHOT"));
        list.add(MavenVersion.parse("1.2.0-SNAPSHOT"));
        list.add(MavenVersion.parse("1.4.5"));
        list.add(MavenVersion.parse("1.4.0-SNAPSHOT"));
        list.add(MavenVersion.parse("1.1.4"));
        list.add(MavenVersion.parse("1.4.0"));
        list.add(MavenVersion.parse("1.2.8"));
        list.add(MavenVersion.parse("1.2.8-SNAPSHOT"));
        list.add(MavenVersion.parse("1.0.17"));
        list.add(MavenVersion.parse("1.4.1"));
        list.add(MavenVersion.parse("1.4.1-SNAPSHOT"));
        list.add(MavenVersion.parse("1.1.24"));
        list.add(MavenVersion.parse("1.3.1-SNAPSHOT"));
        list.add(MavenVersion.parse("1.3.1"));
        list.add(MavenVersion.parse("1.2.11"));
        list.add(MavenVersion.parse("1.2.11-SNAPSHOT"));
        list.add(MavenVersion.parse("1.3.2"));
        list.add(MavenVersion.parse("1.3.2-SNAPSHOT"));
        list.add(MavenVersion.parse("1.5.2-SNAPSHOT"));
        list.add(MavenVersion.parse("1.2.3"));
        list.add(MavenVersion.parse("1.2.7-SNAPSHOT"));
        list.add(MavenVersion.parse("1.1.13"));
        list.add(MavenVersion.parse("1.2.7"));
        list.add(MavenVersion.parse("1.0.18"));
        list.add(MavenVersion.parse("1.3.6-SNAPSHOT"));
        list.add(MavenVersion.parse("1.1.20"));
        list.add(MavenVersion.parse("1.1.11"));
        list.add(MavenVersion.parse("1.1.18"));
        list.add(MavenVersion.parse("1.4.2"));
        list.add(MavenVersion.parse("1.2.12-SNAPSHOT"));
        list.add(MavenVersion.parse("1.2.2-SNAPSHOT"));
        list.add(MavenVersion.parse("1.2.6"));
        list.add(MavenVersion.parse("1.3.3"));
        list.add(MavenVersion.parse("1.5.1-SNAPSHOT"));
        list.add(MavenVersion.parse("1.3.3-SNAPSHOT"));
        list.add(MavenVersion.parse("1.2.6-SNAPSHOT"));
        list.add(MavenVersion.parse("1.1.2"));
        list.add(MavenVersion.parse("1.0.19"));
        Collections.sort(list, MavenVersion.DEFAULT_COMPARATOR);
        System.out.println("Result:");
        for (MavenVersion mavenVersion : list) {
            System.out.println(mavenVersion.toString());
        }
    }
}
