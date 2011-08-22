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

import java.util.ArrayList;
import java.util.List;

import playn.core.Asserts;
import playn.core.Image;
import playn.core.InternalTransform;
import playn.core.Pattern;
import playn.core.StockInternalTransform;
import playn.core.Surface;
import playn.core.gl.GL20;

class AndroidSurface implements Surface {

  private final AndroidGraphics gfx;
  private final int fbuf;
  private final int width;
  private final int height;
  private final List<InternalTransform> transformStack = new ArrayList<InternalTransform>();

  private int fillColor;
  private AndroidPattern fillPattern;

  AndroidSurface(AndroidGraphics gfx, int fbuf, int width, int height) {
    this.gfx = gfx;
    this.fbuf = fbuf;
    this.width = width;
    this.height = height;
    transformStack.add(new StockInternalTransform());
  }

  @Override
  public void clear() {
    gfx.bindFramebuffer(fbuf, width, height);

    gfx.gl20.glClearColor(0, 0, 0, 0);
    gfx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    // TODO(jonagill) Do we need another clear color call?
  }

  /*
   * None of these draw* calls rebind the screen's framebuffer for efficiency.
   * The screen's framebuffer is automatically restored in
   * AndroidGraphics.updateLayers() before any drawing to the screen occurs.
   */

  @Override
  public void drawImage(Image image, float x, float y) {
    drawImage(image, x, y, image.width(), image.height());
  }

  @Override
  public void drawImage(Image image, float x, float y, float dw, float dh) {
    gfx.bindFramebuffer(fbuf, width, height);

    Asserts.checkArgument(image instanceof AndroidImage);
    AndroidImage aimage = (AndroidImage) image;

    if (aimage.isReady()) {
      int tex = aimage.ensureTexture(gfx, false, false);
      if (tex != 0) {
        gfx.drawTexture(tex, image.width(), image.height(), topTransform(), x, y, dw, dh, false,
            false, 1);
      }
    }
    // gfx.bindFramebuffer();
  }

  @Override
  public void drawImage(Image image, float dx, float dy, float dw, float dh, float sx, float sy,
      float sw, float sh) {
    gfx.bindFramebuffer(fbuf, width, height);

    Asserts.checkArgument(image instanceof AndroidImage);
    AndroidImage aimage = (AndroidImage) image;

    if (aimage.isReady()) {
      int tex = aimage.ensureTexture(gfx, false, false);
      if (tex != 0) {
        gfx.drawTexture(tex, image.width(), image.height(), topTransform(), dx, dy, dw, dh, sx, sy,
            sw, sh, 1);
      }
    }
    // gfx.bindFramebuffer();
  }

  public void drawImageCentered(Image img, float x, float y) {
    drawImage(img, x - img.width() / 2, y - img.height() / 2);
  }

  @Override
  public void drawLine(float x0, float y0, float x1, float y1, float width) {
    gfx.bindFramebuffer(fbuf, this.width, this.height);

    float dx = x1 - x0, dy = y1 - y0;
    float len = (float) Math.sqrt(dx * dx + dy * dy);
    dx = dx * (width / 2) / len;
    dy = dy * (width / 2) / len;

    float[] pos = new float[8];
    pos[0] = x0 - dy;
    pos[1] = y0 + dx;
    pos[2] = x1 - dy;
    pos[3] = y1 + dx;
    pos[4] = x1 + dy;
    pos[5] = y1 - dx;
    pos[6] = x0 + dy;
    pos[7] = y0 - dx;
    gfx.fillPoly(topTransform(), pos, fillColor, 1);
    // gfx.bindFramebuffer();
  }

  @Override
  public void fillRect(float x, float y, float width, float height) {
    gfx.bindFramebuffer(fbuf, this.width, this.height);

    if (fillPattern != null) {
      AndroidImage image = fillPattern.image;
      int tex = image.ensureTexture(gfx, true, true);
      gfx.fillRect(topTransform(), x, y, width, height, image.width(), image.height(), tex, 1);
    } else {
      gfx.fillRect(topTransform(), x, y, width, height, fillColor, 1);
    }
    // gfx.bindFramebuffer();
  }

  @Override
  public int height() {
    return height;
  }

  @Override
  public void restore() {
    Asserts.checkState(transformStack.size() > 1, "Unbalanced save/restore");
    transformStack.remove(transformStack.size() - 1);
  }

  @Override
  public void rotate(float angle) {
    float sr = (float) Math.sin(angle);
    float cr = (float) Math.cos(angle);
    transform(cr, sr, -sr, cr, 0, 0);
  }

  @Override
  public void save() {
    transformStack.add((InternalTransform) new StockInternalTransform().set(topTransform()));
  }

  @Override
  public void scale(float sx, float sy) {
    topTransform().scale(sx, sy);
  }

  @Override
  public void setTransform(float m00, float m01, float m10, float m11, float tx, float ty) {
    topTransform().setTransform(m00, m01, m10, m11, tx, ty);
  }

  public void setFillColor(int color) {
    // TODO: Add it to the state stack.
    this.fillColor = color;
    this.fillPattern = null;
  }

  @Override
  public void setFillPattern(Pattern pattern) {
    // TODO: Add it to the state stack.
    Asserts.checkArgument(pattern instanceof AndroidPattern);
    this.fillPattern = (AndroidPattern) pattern;
  }

  @Override
  public void transform(float m00, float m01, float m10, float m11, float tx, float ty) {
    topTransform().concatenate(m00, m01, m10, m11, tx, ty, 0, 0);
  }

  @Override
  public void translate(float x, float y) {
    topTransform().translate(x, y);
  }

  @Override
  public int width() {
    return width;
  }

  private InternalTransform topTransform() {
    return transformStack.get(transformStack.size() - 1);
  }
}
