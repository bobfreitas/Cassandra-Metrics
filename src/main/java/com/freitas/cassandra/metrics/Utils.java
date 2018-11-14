/* Copyright 2015 Eric Evans <eevans@wikimedia.org> and Wikimedia Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.freitas.cassandra.metrics;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Utils {

    /** tools.jar search paths. */
    public static final File[] JDK_SEARCH = new File[] {
            new File(System.getProperty("java.home"), "../lib/tools.jar") };

    /**
     * Searches for the tools.jar file that ships with the JDK, and adds it to
     * the classpath.
     * 
     * @return true if tools.jar was found, and added to the classpath
     */
    public static boolean addToolsJar() {
        for (File jar : JDK_SEARCH) {
            if (jar.exists()) {
                addJar(jar);
                return true;
            }
        }
        return false;
    }

    /**
     * Add a file or directory to the classpath.
     * 
     * @param path
     *            path to jar file
     */
    @SuppressWarnings("deprecation")
    private static void addJar(File path) {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(classLoader(), new Object[] { path.toURL() });
        } catch (Throwable e) {
            throw new RuntimeException(String.format("Error adding %s to classpath", path), e);
        }
    }

    private static URLClassLoader classLoader() {
        return (URLClassLoader) ClassLoader.getSystemClassLoader();
    }

}
