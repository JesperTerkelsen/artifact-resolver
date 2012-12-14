/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.resolver.util;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author jester
 */
// @Ignore("Integration test")
public class TarUtilsTest {
    
    public TarUtilsTest() {
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

//    /**
//     * Test of unzipArchive method, of class TarUtils.
//     */
//    @Test
//    public void testUnzipArchive1() throws Exception {
//        System.out.println("unzipArchive1");
//        File sourceFile = new File("/Users/jester/work/solrstaging-agent/modules/service/target/apache-solr-4.0.0.tgz");
//        
//        File destDir = new File("target");
//        TarUtils instance = new TarUtils();
//        instance.unzipArchive(sourceFile, destDir);      
//    }
    
    /**
     * Test of unzipArchive method, of class TarUtils.
     */
    @Test
    public void testUnzipArchive2() throws Exception {
        System.out.println("unzipArchive2");
        File sourceFile = new File("/Users/jester/Downloads/apache-tomcat-7.0.33.tar.gz");
        
        File destDir = new File("target");
        TarUtils instance = new TarUtils();
        instance.unzipArchive(sourceFile, destDir);      
    }    
}
