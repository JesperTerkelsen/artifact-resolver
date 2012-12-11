/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.resolver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

/**
 *
 * @author jester
 */
public class TarUtils {

    public List<String> listArchive(File archive) throws FileNotFoundException, IOException {
        List<String> content = new ArrayList<String>();
        FileInputStream fileInputStream = new FileInputStream(archive);
        GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
        TarArchiveInputStream stream = new TarArchiveInputStream(gzipInputStream);
        ArchiveEntry entry = stream.getNextEntry();
        while (entry != null) {
            if (!entry.isDirectory()) {
                content.add(entry.getName());
            }
            entry = stream.getNextEntry();
        }
        return content;
    }

    public void unzipArchive(File sourceFile, File destDir) throws FileNotFoundException, IOException {
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
        TarArchiveInputStream stream = new TarArchiveInputStream(gzipInputStream);
        TarArchiveEntry entry = stream.getNextTarEntry();
        while (entry != null) {
            // System.out.println(entry.getName() + " " + entry.isDirectory());
            if (entry.isDirectory()){
                new File(destDir, entry.getName()).mkdirs();
            }
            if (entry.isFile()){
                File outFile = new File(destDir, entry.getName());
                // Make any missing directories
                outFile.getParentFile().mkdirs();
                FileOutputStream out = new FileOutputStream(outFile);
                long size = entry.getSize();
                byte[] buffer = new byte[1024];
                boolean done = false;
                while (!done){
                    int len = stream.read(buffer, 0, buffer.length);
                    if (len == -1){
                        done = true;
                    }
                    else {
                        out.write(buffer, 0, len);
                    }
                }
                out.flush();
                out.close();
            }

            // Read next
            entry = stream.getNextTarEntry();
        }
        stream.close();
    }
}
