/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.resolver.util;

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
public class PosixFileModeParserTest {
    
    public PosixFileModeParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of printPermissions method, of class PosixFileModeParser.
     */
    @Test
    public void testPrintPermissions() {
        System.out.println("printPermissions");
        PosixFileModeParser instance = new PosixFileModeParser(0100644);
        
        String result = instance.getPrintedPermissions();
        System.out.println(instance.getMode());
        System.out.println(Integer.toOctalString(instance.getMode()));
        System.out.println(Integer.toBinaryString(instance.getMode()));
        System.out.println(result);
        
        instance = new PosixFileModeParser(0100755);
        
        result = instance.getPrintedPermissions();
        System.out.println(instance.getMode());
        System.out.println(Integer.toBinaryString(instance.getMode()));
        System.out.println(Integer.toOctalString(instance.getMode()));
        System.out.println(result);
    }
}
