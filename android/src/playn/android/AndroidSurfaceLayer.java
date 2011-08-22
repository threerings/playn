/**
 * Copyright 2011 The PlayN Authors
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
package playn.android;

import playn.core.Asserts;
import playn.core.InternalTransform;
import playn.core.Surface;
import playn.core.SurfaceLayer;
import playn.core.gl.GL20;
import android.util.Log;

class AndroidSurfaceLayer extends AndroidLayer implements SurfaceLayer {

  private AndroidSurface surface;

  private int fbuf, tex;
  private final int width, height;

  AndroidSurfaceLayer(AndroidGraphics gfx, int width, int height) {
    super(gfx);
    this.width = width;
    this.height = height;
    gfx.flush();

    AndroidGL20 gl20 = gfx.gl20;
    tex = gfx.createTexture(false, false);
    gl20.glTexImage2D(GL20.GL_TEXTURE_2D, 0, GL20.GL_RGBA, width, height, 0, GL20.GL_RGBA,
        GL20.GL_UNSIGNED_BYTE, null);

    int[] fbufBuffer = new int[1];
    gl20.glGenFramebuffers(1, fbufBuffer, 0);
    fbuf = fbufBuffer[0];
    Log.w("playn", "Fbuf " + fbuf);
    gfx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, fbuf);
    gl20.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0, GL20.GL_TEXTURE_2D,
        tex, 0);
    // gl20.glBindTexture(GL20.GL_TEXTURE_2D, 0);
    gfx.bindFramebuffer();

    surface = new AndroidSurface(gfx, fbuf, width, height);
    surface.clear();

  }

  @Override
  public void destroy() {
    super.destroy();
    gfx.destroyTexture(tex);
    gfx.gl20.glDeleteBuffers(1, new int[] {fbuf}, 0);

    tex = fbuf = 0;
    surface = null;
  }

  @Override
  public Surface surface() {
    return surface;
  }

  @Override
  public void paint(InternalTransform parentTransform, float parentAlpha) {
    if (!visible())
      return;

    // Draw this layer to the screen upside-down, because its contents are
    // flipped
    // (This happens because it uses the same vertex program as everything else,
    // which flips vertically to put the origin at the top-left).
    gfx.drawTexture(tex, width, height, localTransform(parentTransform), 0, height, width, -height,
        false, false, parentAlpha * alpha);
  }

  @Override
  public float width() {
    Asserts.checkNotNull(surface, "Surface must not be null");
    return surface.width();
  }

  @Override
  public float height() {
    Asserts.checkNotNull(surface, "Surface must not be null");
    return surface.height();
  }

  @Override
  public float scaledWidth() {
    return transform().scaleX() * width();
  }

  @Override
  public float scaledHeight() {
    return transform().scaleY() * height();
  }
}
