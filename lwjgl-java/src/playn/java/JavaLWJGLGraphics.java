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
import org.lwjgl.opengl.DisplayMode;
import playn.core.gl.GL20;

import java.util.Arrays;

public class JavaLWJGLGraphics extends JavaGraphics {

  public JavaLWJGLGraphics(JavaPlatform platform, JavaPlatform.Config config) {
    super(platform, config);
    if (!config.headless) {
      setDisplayMode(ctx.scale.scaledCeil(config.width), ctx.scale.scaledCeil(config.height),
              config.fullscreen);
    }
  }

  @Override
  protected JavaGLContext createJavaGLContext(JavaPlatform platform, float scaleFactor) {
    return new JavaGLContext(platform, new JavaLWJGL20(), scaleFactor);
  }

  @Override
  public int screenWidth() {
    return ctx.scale.invScaledFloor(Display.getDesktopDisplayMode().getWidth());
  }

  @Override
  public int screenHeight() {
    return ctx.scale.invScaledFloor(Display.getDesktopDisplayMode().getHeight());
  }

  protected void setDisplayMode(int width, int height, boolean fullscreen) {
    try {
      // check if current mode is suitable
      DisplayMode mode = Display.getDisplayMode();
      if (fullscreen == Display.isFullscreen() &&
              mode.getWidth() == width && mode.getHeight() == height)
        return;

      if (fullscreen) {
        // try and find a mode matching width and height
        DisplayMode matching = null;
        for (DisplayMode test : Display.getAvailableDisplayModes()) {
          if (test.getWidth() == width && test.getHeight() == height && test.isFullscreenCapable()) {
            matching = test;
          }
        }

        if (matching == null) {
          platform.log().info("Could not find a matching fullscreen mode, available: " +
                  Arrays.asList(Display.getAvailableDisplayModes()));
        } else {
          mode = matching;
        }

      } else {
        mode = new DisplayMode(width, height);
      }

      platform.log().debug("Updating display mode: " + mode + ", fullscreen: " + fullscreen);
      // TODO: fix crashes when fullscreen is toggled repeatedly
      if (fullscreen) {
        Display.setDisplayModeAndFullscreen(mode);
        // TODO: fix alt-tab, maybe add a key listener or something?
      } else {
        Display.setDisplayMode(mode);
      }

    } catch (LWJGLException ex) {
      throw new RuntimeException(ex);
    }
  }

  protected void init() {
    DisplayMode mode = Display.getDisplayMode();
    ctx.setSize(ctx.scale.invScaledFloor(mode.getWidth()),
            ctx.scale.invScaledFloor(mode.getHeight()));
    ctx.init();
  }

  @Override
  public void setSize(int width, int height, boolean fullscreen) {
    int swidth = ctx.scale.scaledCeil(width), sheight = ctx.scale.scaledCeil(height);
    setDisplayMode(swidth, sheight, fullscreen);
    super.setSize(width, height, fullscreen);
  }
}
