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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import dk.deck.resolver.model.Artifact;
import org.xml.sax.SAXException;

/**
 *
 * @author Jesper Terkelsen
 */
public class ParallelArtifactResolver implements ArtifactResolver {
    private final ArtifactResolver resolver;
    private static int threadNumber = 0;
    private static final int WAIT_TIMEOUT = 3;
    private static final TimeUnit WAIT_UNIT = TimeUnit.HOURS;
    
    public ParallelArtifactResolver(ArtifactResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public int ensureExistance(File workdir, List<Artifact> artifacts) throws IOException, SAXException, ParseException {
        try {
            int downloadedCount = 0;
            List<SingleDownloader> loaders = new ArrayList<SingleDownloader>();
            int num = 0;
            // Use a max threads strategy instread
            ExecutorService executor = Executors.newFixedThreadPool(ArtifactResolverRemote.MAX_HTTP_THREADS, new ThreadFactory() {
                
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "Downloader-" + threadNumber++);
                    return t;
                }
            });

            for (Artifact artifact : artifacts) {
                SingleDownloader singleDownloader = new SingleDownloader(resolver, workdir, artifact);
                loaders.add(singleDownloader);
                executor.execute(singleDownloader);
                num++;
            }
            executor.shutdown();
            executor.awaitTermination(WAIT_TIMEOUT, WAIT_UNIT);
            for (SingleDownloader singlePreloader : loaders) {
                if (singlePreloader.isDownloaded()){
                    downloadedCount++;
                }
                Exception error = singlePreloader.getResult();
                if (error != null){
                    throw error;
                }
            }
            return downloadedCount;
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public boolean ensureExistance(File workdir, Artifact artifact) throws IOException, SAXException, ParseException {
        return resolver.ensureExistance(workdir, artifact);
    }

    @Override
    public List<MavenVersion> getVersionsAvalible(Artifact artifact) throws IOException, DownloadException, SAXException, ParseException {
        return resolver.getVersionsAvalible(artifact);
    }
    
    
    private static class SingleDownloader implements Runnable {
        private Exception result = null;
        private File workdir;
        private Artifact artifact;
        private ArtifactResolver resolver;
        private boolean downloaded;
        
        public SingleDownloader(ArtifactResolver resolver, File workdir, Artifact artifact) {
            this.workdir = workdir;
            this.artifact = artifact;
            this.resolver = resolver;
        }
        
        @Override
        public void run() {
            try {
                downloaded = resolver.ensureExistance(workdir, artifact);
            } catch (Exception ex) {
                result = ex;
            } finally{
            }
        }

        public boolean isDownloaded() {
            return downloaded;
        }
        
        public Exception getResult() {
            return result;
        }
    }     
    
}
