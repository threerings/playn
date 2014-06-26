/**
 * Copyright 2014 The PlayN Authors
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
 */
package playn.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 *
 * @author devnewton
 */
public class JavaFilesystemAssets extends JavaAssets {

    private final File[] directories;

    public JavaFilesystemAssets(JavaPlatform platform, File... dirs) {
        super(platform);
        directories = Arrays.copyOf(dirs, dirs.length);
    }

    @Override
    protected URL requireResource(String path) throws FileNotFoundException {
        try {
            for (File dir : directories) {

                File f = new File(dir, path).getCanonicalFile();
                if (f.exists()) {
                    return f.toURI().toURL();
                }
            }
            File f = new File(path);
            if (f.exists()) {
                return f.toURI().toURL();
            }
            throw new FileNotFoundException("Cannot find file " + path);
        } catch (IOException ex) {
            throw new FileNotFoundException(ex.getMessage());
        }
    }

}
