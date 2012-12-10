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
package dk.deck.maven.util;

import dk.deck.maven.util.VariableUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class VariableUtilTest {
    
    public VariableUtilTest() {
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
     * Test of replaceVariables method, of class VariableUtil.
     */
    @Test
    public void testReplaceVariables_Map() {
        System.out.println("replaceVariables_Map");
        Map<String, String> variables = new HashMap<String, String>();
        variables.put("var1", "${var2} ${var3}");
        variables.put("var2", "!");
        // variables.put("var3", "!"); Missing on purpose
        variables.put("var4", "${var1}");
        Map expResult = new HashMap<String, String>();
        expResult.put("var1", "! ${var3}");
        expResult.put("var2", "!");
        expResult.put("var4", "! ${var3}");
        Map<String, String> result = VariableUtil.replaceVariables(variables);
        assertEquals(expResult, result);
        
//        for (Map.Entry<String, String> entry : result.entrySet()) {
//            System.out.println(entry.getKey() + "=" + entry.getValue());
//        }
    }

    /**
     * Test of replaceVariables method, of class VariableUtil.
     */
    @Test
    public void testReplaceVariables_Map_StackOverflow() {
        System.out.println("replaceVariables_Map_StackOverflow");
        Map<String, String> variables = new HashMap<String, String>();
        variables.put("var1", "${var2} ${var3}");
        variables.put("var2", "! ${var4}");
        // variables.put("var3", "!"); Missing on purpose
        variables.put("var4", "${var1}");
//        Map expResult = new HashMap<String, String>();
//        expResult.put("var1", "many");
//        expResult.put("var2", "!");
        try {
            Map<String, String> result = VariableUtil.replaceVariables(variables);
            // assertEquals(expResult, result);
            fail("Sould not reach here");
        } catch (IllegalArgumentException iae) {
            assertEquals("Loop found in variables var4->var1->var2->var4", iae.getMessage());
        }
    }

    /**
     * Test of replaceVariables method, of class VariableUtil.
     */
    @Test
    public void testReplaceVariables_String_Map() {
        System.out.println("replaceVariables");
        String line = "Test line with ${var1} variables ${var2}";
        Map<String, String> variables = new HashMap<String, String>();
        variables.put("var1", "many");
        variables.put("var2", "!");
        String expResult = "Test line with many variables !";
        String result = VariableUtil.replaceVariables(line, variables);
        // System.out.println(result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getVariables method, of class VariableUtil.
     */
    @Test
    public void testGetVariables() {
        System.out.println("getVariables");
        String line = "Test line with ${var1} variables ${var2}";
        List expResult = new ArrayList();
        expResult.add("var1");
        expResult.add("var2");
        List result = VariableUtil.getVariables(line);
        Collections.sort(expResult);
        Collections.sort(result);
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of replaceVariable method, of class VariableUtil.
     */
    @Test
    public void testReplaceVariable() {
        System.out.println("replaceVariable");
        String line = "This is a line containing ${var} and another variable ${var2}";
        String varName = "var";
        String replacement = "The variable";
        String expResult = "This is a line containing The variable and another variable ${var2}";
        String result = VariableUtil.replaceVariable(line, varName, replacement);
        // System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }
}
