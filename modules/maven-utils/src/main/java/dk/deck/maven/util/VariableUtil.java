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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Methods for working on variables 
 * 
 * @author Jesper Terkelsen
 */
public class VariableUtil {

    private static final Pattern VAR_PATTERN = Pattern.compile(".*(\\$\\{(.*)\\}).*");

    /**
     * Looking for variables in variables, and replacing them with values.
     * @param variables
     * @return 
     */
    public static Map<String, String> replaceVariables(Map<String, String> variables) {
        Map<String, String> workingVariables = new HashMap<String, String>();
        // Copy the values, so we wont modify the existing one
        for (Entry<String, String> entry : variables.entrySet()) {
            workingVariables.put(entry.getKey(), entry.getValue());
        }
        List<String> path = new ArrayList<String>();
        // Go through the original list and replace content in the copy
        for (Entry<String, String> entry : variables.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            replaceVariables(key, key, value, workingVariables, 0, path);
        }
        return workingVariables;
    }

    public static String replaceVariables(String originalKey, String key, String value, Map<String, String> variables, int level, List<String> path) {
        path.add(key);
        String result = value;
        List<String> vars = getVariables(result);
        if (!vars.isEmpty()) {
            for (String subKey : vars) {
                if (subKey.equals(originalKey)){
                    path.add(originalKey);
                    throw new IllegalArgumentException("Loop found in variables " + getPathTostring(path));
                }
                String subValue = variables.get(subKey);
                if (!getVariables(subValue).isEmpty()) {
                    subValue = replaceVariables(originalKey, subKey, subValue, variables, level + 1, path);
                }
                result = replaceVariable(result, subKey, subValue);
            }
        }
        variables.put(key, result);
        path.remove(path.size() -1);
        return result;
    }
    
    private static String getPathTostring(List<String> path){
        StringBuilder result = new StringBuilder();
        for (Iterator<String> it = path.iterator(); it.hasNext();) {
            String string = it.next();
            result.append(string);
            if (it.hasNext()){
                result.append("->");
            }
        }
        return result.toString();
    }

    /**
     * Replaces a single variable name with the replacement 
     * 
     * @param line
     * @param varName
     * @param replacement
     * @return 
     */
    public static String replaceVariable(String line, String varName, String replacement) {
        String result = "";
        String rest = line;
        boolean done = false;
        while (!done) {
            Matcher m = VAR_PATTERN.matcher(rest);
            if (m.matches()) {
                String var = m.group(2);
                // System.out.println("Match on line: " + rest + " for var: " + var);
                if (var.equals(varName) && replacement != null) {
                    result = replacement + rest.substring(m.end(1), rest.length()) + result;
                    rest = rest.substring(0, m.start(1));
                } else {
                    result = rest.substring(m.start(1), rest.length()) + result;
                    rest = rest.substring(0, m.start(1));
                }
            } else {
                result = rest + result;
                done = true;
            }
        }
        return result;
    }

    /**
     * Gets a list of variable names in a line
     * 
     * @param line The line to analyze
     * @return A list of reverse ordered variable names
     */
    public static List<String> getVariables(String line) {
        List<String> vars = new ArrayList<String>();
        if (line == null){
            return vars;
        }        
        String rest = line;
        boolean done = false;
        while (!done) {
            Matcher m = VAR_PATTERN.matcher(rest);
            if (m.matches()) {
                String var = m.group(2);
                // System.out.println("Match on line: " + rest + " for var: " + var);
                rest = rest.substring(0, m.start(1));
                vars.add(var);
            } else {
                done = true;
            }
        }
        return vars;
    }

    /**
     * Runs through a string once, finding variables and replacing them with the values if possible
     * 
     * @param line The line to replace variables on
     * @return The line with the replaced variables
     */
    public static String replaceVariables(String line, Map<String, String> variables) {
        String result = "";
        String rest = line;
        boolean done = false;
        while (!done) {
            Matcher m = VAR_PATTERN.matcher(rest);
            if (m.matches()) {
                String var = m.group(2);
                // System.out.println("Match on line: " + rest + " for var: " + var);
                String replaceMent = variables.get(var);
                if (replaceMent != null) {
                    result = replaceMent + rest.substring(m.end(1), rest.length()) + result;
                    rest = rest.substring(0, m.start(1));
                } else {
                    result = rest.substring(m.start(1), rest.length()) + result;
                    rest = rest.substring(0, m.start(1));
                }
            } else {
                result = rest + result;
                done = true;
            }
        }
        return result;
    }
}
