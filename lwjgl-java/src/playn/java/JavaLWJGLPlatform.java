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

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import playn.core.Game;
import playn.core.PlayN;
import playn.core.TouchImpl;
import playn.core.TouchStub;

import java.io.File;

public class JavaLWJGLPlatform extends JavaPlatform {

  private static JavaLWJGLPlatform testInstance;

  /**
   * Registers the Java platform with a default configuration.
   */
  public static JavaLWJGLPlatform register() {
    return register(new Config());
  }

  /**
   * Registers the Java platform with the specified configuration.
   */
  public static JavaLWJGLPlatform register(Config config) {
    // guard against multiple-registration (only in headless mode because this can happen when
    // running tests in Maven; in non-headless mode, we want to fail rather than silently ignore
    // erroneous repeated registration)
    if (config.headless && testInstance != null) {
      return testInstance;
    }
    JavaLWJGLPlatform instance = new JavaLWJGLPlatform(config);
    if (config.headless) {
      testInstance = instance;
    }
    PlayN.setPlatform(instance);
    return instance;
  }

  public JavaLWJGLPlatform(Config config) {
    super(config);
    if (!config.headless) {
      setTitle(config.appName);
    }
  }

  @Override
  public void run(final Game game) {
    if (!config().headless) {
      try {
        Display.create();
      } catch (LWJGLException e) {
        throw new RuntimeException(e);
      }
    }
    init(game);

    boolean wasActive = Display.isActive();
    while (!Display.isCloseRequested()) {
      // Notify the app if lose or regain focus (treat said as pause/resume).
      boolean newActive = Display.isActive();
      if (wasActive != newActive) {
        if (wasActive)
          onPause();
        else
          onResume();
        wasActive = newActive;
      }
      processFrame(game);
      Display.update();
      // Sleep until it's time for the next frame.
      Display.sync(60);
    }

    shutdown();
  }

  protected JavaGraphics createGraphics(Config config) {
    return new JavaLWJGLGraphics(this, config);
  }

  protected TouchImpl createTouch(Config config) {
    if (config.emulateTouch) {
      return new JavaLWJGLEmulatedTouch();
    } else {
      return new TouchStub();
    }
  }

  protected JavaMouse createMouse() {
    return new JavaLWJGLMouse(this);
  }

  protected JavaKeyboard createKeyboard() {
    return new JavaLWJGLKeyboard();
  }

  /**
   * Sets the title of the window.
   *
   * @param title the window title
   */
  public void setTitle(String title) {
    Display.setTitle(title);
  }

  protected void unpackNatives() {
    // avoid native library unpacking if we're running in Java Web Start
    if (isInJavaWebStart())
      return;

    SharedLibraryExtractor extractor = new SharedLibraryExtractor();
    File nativesDir = null;
    try {
      nativesDir = extractor.extractLibrary("lwjgl", null).getParentFile();
    } catch (Throwable ex) {
      throw new RuntimeException("Unable to extract LWJGL native libraries.", ex);
    }
    System.setProperty("org.lwjgl.librarypath", nativesDir.getAbsolutePath());
  }
}
