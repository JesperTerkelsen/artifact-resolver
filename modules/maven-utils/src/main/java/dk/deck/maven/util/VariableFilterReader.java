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

import java.io.BufferedReader;
import java.io.FilterReader;
import java.io.IOException;
import java.util.Map;

/**
 * A filter reader that replaces variables from a Map with variable names from a map.
 * 
 * The the values in the map cannot contain variable indicators, 
 * thus those must be resolved in the map before applying to the file
 * 
 * 
 * @author Jesper Terkelsen
 */
public class VariableFilterReader extends FilterReader {

    private Map<String, String> variables;

    public VariableFilterReader(BufferedReader in, Map<String, String> variables) {
        super(in);
        // TODO replace variables with variables, before using them
        this.variables = VariableUtil.replaceVariables(variables);
    }
    // This variable holds the current line.
    // If null and emitNewline is false, a newline must be fetched.
    private String curLine;
    // This is the index of the first unread character in curLine.
    // If at any time curLineIx == curLine.length, curLine is set to null.
    private int curLineIx;

    // This overridden method fills cbuf with characters read from in.
    @Override
    public int read(char cbuf[], int off, int len) throws IOException {
        // System.out.println("read() off:" + off + " len:" + len);
        // Fetch new line if necessary
        if (curLine == null) {
            getNextLine();
        }

        // Return characters from current line
        if (curLine != null) {


            int num = Math.min(len, Math.min(cbuf.length - off,
                    curLine.length() - curLineIx));
            // Copy characters from curLine to cbuf
            for (int i = 0; i < num; i++) {
                cbuf[off++] = curLine.charAt(curLineIx++);
            }

            // No more characters in curLine
            if (curLineIx == curLine.length()) {
                curLine = null;

                // Is there room for the newline?
                if (num < len && off < cbuf.length) {
                    cbuf[off++] = '\n';
                    num++;
                }
            }

            // Return number of character read
            // System.out.println("read " + num);
            return num;
        } else if (len > 0) {
            // No more characters left in input reader
            return -1;
        } else {
            // Client did not ask for any characters
            return 0;
        }
    }

    // Get next line
    private void getNextLine() throws IOException {
        curLine = ((BufferedReader) in).readLine();
        if (curLine != null) {
            curLineIx = 0;
            // System.out.println("Reading line: " + curLine);
            // Replace variables

            curLine = VariableUtil.replaceVariables(curLine, variables);

            return;
        }
        return;
    }



    @Override
    public boolean ready() throws IOException {
        return curLine != null || in.ready();
    }

    @Override
    public boolean markSupported() {
        return false;
    }
}
