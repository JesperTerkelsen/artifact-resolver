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

import java.text.ParseException;
import java.util.Comparator;

/**
 * This is an entity that represent the installer version, it can be used to
 * mark the installed version on the server to be able to skip installing shared
 * libs, or service configuration, and thereby skipping backups
 *
 * This version is specified according to mavens versioning standards.
 *
 * This is a imutable value object
 *
 * @author Jesper Terkelsen
 */
public class MavenVersion {

    private static final String SNAPSHOT = "-snapshot";
    private final String majorVersion;
    private final String featureVersion;
    private final String bugfixVersion;
    private final boolean snapshot;

    public MavenVersion(String majorVersion, String featureVersion, String bugfixVersion, boolean snapshot) {
        if (majorVersion == null) {
            throw new IllegalArgumentException("Major version cannot be null");
        }
        this.majorVersion = majorVersion;
        this.featureVersion = featureVersion;
        this.bugfixVersion = bugfixVersion;
        this.snapshot = snapshot;
    }

    public String getBugfixVersion() {
        return bugfixVersion;
    }

    public String getFeatureVersion() {
        return featureVersion;
    }

    public String getMajorVersion() {
        return majorVersion;
    }

    public boolean isSnapshot() {
        return snapshot;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MavenVersion other = (MavenVersion) obj;
        if ((this.majorVersion == null) ? (other.majorVersion != null) : !this.majorVersion.equals(other.majorVersion)) {
            return false;
        }
        if ((this.featureVersion == null) ? (other.featureVersion != null) : !this.featureVersion.equals(other.featureVersion)) {
            return false;
        }
        if ((this.bugfixVersion == null) ? (other.bugfixVersion != null) : !this.bugfixVersion.equals(other.bugfixVersion)) {
            return false;
        }
        if (this.snapshot != other.snapshot) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.majorVersion != null ? this.majorVersion.hashCode() : 0);
        hash = 29 * hash + (this.featureVersion != null ? this.featureVersion.hashCode() : 0);
        hash = 29 * hash + (this.bugfixVersion != null ? this.bugfixVersion.hashCode() : 0);
        hash = 29 * hash + (this.snapshot ? 1 : 0);
        return hash;
    }

    public static MavenVersion parse(String version) throws ParseException {
        version = version.trim();
        if (version.isEmpty()) {
            throw new ParseException("Empty string", 0);
        }
        String[] dotSplit = version.split("\\.");
        if (dotSplit.length > 3) {
            throw new ParseException("A maven version cannot contain more than 2 dots (.)", -1);
        }
        if (dotSplit.length == 0) { // No dots
            dotSplit = new String[]{version};
        }
        boolean snapshot = false;
        String majorVersion = "";
        String featureVersion = null;
        String bugfixVersion = null;
        for (int i = 0; i < dotSplit.length; i++) {
            String string = dotSplit[i];
            // System.out.println(i + " " + string);
            if (i + 1 == dotSplit.length) {
                // We need to check for snapshot on the last split, - is allowed before this
                if (string.toLowerCase().endsWith(SNAPSHOT)) {
                    snapshot = true;
                    string = string.substring(0, string.length() - SNAPSHOT.length());
                }
            }
            if (i == 0) {
                majorVersion = string;
            }
            if (i == 1) {
                featureVersion = string;
            }
            if (i == 2) {
                bugfixVersion = string;
            }
        }
        return new MavenVersion(majorVersion, featureVersion, bugfixVersion, snapshot);
    }

    @Override
    public String toString() {
        String result = majorVersion.toString();
        if (featureVersion != null) {
            result = result + "." + featureVersion.toString();
        }
        if (bugfixVersion != null) {
            result = result + "." + bugfixVersion.toString();
        }
        if (snapshot == true) {
            result = result + "-SNAPSHOT";
        }
        return result;
    }

    public String toSnapshotString(String timestamp, String buildNumber) {
        String result = majorVersion.toString();
        if (featureVersion != null) {
            result = result + "." + featureVersion.toString();
        }
        if (bugfixVersion != null) {
            result = result + "." + bugfixVersion.toString();
        }
        if (snapshot == true) {
            result = result + "-" + timestamp + "-" + buildNumber;
        }
        return result;
    }
    public static Comparator<MavenVersion> DEFAULT_COMPARATOR = new Comparator<MavenVersion>() {

        @Override
        public int compare(MavenVersion t1, MavenVersion t2) {
            int result = 0;
            int compare = compareTo(t1.majorVersion, t2.majorVersion);
            if (compare != 0) {
                result = compare;
            } else if (t1.featureVersion != null) {
                compare = compareTo(t1.featureVersion, t2.featureVersion);
                if (compare != 0) {
                    result = compare;
                } else if (t1.bugfixVersion != null) {
                    compare = compareTo(t1.bugfixVersion, t2.bugfixVersion);
                    if (compare != 0) {
                        result = compare;
                    } else {
                        result = Boolean.valueOf(!t1.snapshot).compareTo(Boolean.valueOf(t2.snapshot));
                    }
                }
            }
            return result;
        }

        /**
         * Tries to compare the strings as integers, if possible, otherwise we
         * revert to ascii sort.
         */
        private int compareTo(String s1, String s2) {
            try {
                Integer i1 = Integer.parseInt(s1);
                Integer i2 = Integer.parseInt(s2);
                return i1.compareTo(i2);
            } catch (NumberFormatException ex){
                return s1.compareTo(s2);
            }
        }
    };
}
