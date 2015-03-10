/**
 * Copyright 2014 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package playn.robovm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.robovm.apple.coregraphics.CGAffineTransform;
import org.robovm.apple.coregraphics.CGBitmapContext;
import org.robovm.apple.coregraphics.CGBlendMode;
import org.robovm.apple.coregraphics.CGImage;
import org.robovm.apple.coregraphics.CGInterpolationQuality;
import org.robovm.apple.coregraphics.CGLineCap;
import org.robovm.apple.coregraphics.CGLineJoin;
import org.robovm.apple.coregraphics.CGMutablePath;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGTextEncoding;
import org.robovm.rt.bro.ptr.IntPtr;

import playn.core.Canvas;
import playn.core.Gradient;
import playn.core.Path;
import playn.core.Pattern;
import playn.core.TextLayout;
import playn.core.gl.AbstractCanvasGL;

/**
 * Implements {@link Canvas}.
 */
public class RoboCanvas extends AbstractCanvasGL<CGBitmapContext> {

  private final int texWidth, texHeight;

  private float strokeWidth = 1;
  private int strokeColor = 0xFF000000;
  private int fillColor = 0xFF000000;
  private CGBitmapContext bctx;
  private final RoboGLContext ctx;

  private LinkedList<RoboCanvasState> states = new LinkedList<RoboCanvasState>();

  public RoboCanvas(RoboGLContext ctx, float width, float height, boolean interpolate) {
    super(width, height);
    // if our size is invalid, we'll fail below at CGBitmapContext, so fail here more usefully
    if (width <= 0 || height <= 0) throw new IllegalArgumentException(
      "Invalid size " + width + "x" + height);
    states.addFirst(new RoboCanvasState());

    this.ctx = ctx;

    // create our raw image data
    texWidth = ctx.scale.scaledCeil(width);
    texHeight = ctx.scale.scaledCeil(height);

    // create the bitmap context via which we'll render into it
    bctx = RoboGraphics.createCGBitmap(texWidth, texHeight);
    if (!interpolate) {
      bctx.setInterpolationQuality(CGInterpolationQuality.None);
    }

    // clear the canvas before we scale our bitmap context to avoid artifacts
    bctx.clearRect(new CGRect(0, 0, texWidth, texHeight));

    // CG coordinate system is OpenGL-style (0,0 in lower left); so we flip it
    bctx.translateCTM(0, ctx.scale.scaled(height));
    bctx.scaleCTM(ctx.scale.factor, -ctx.scale.factor);
  }

  public IntPtr data() {
    return bctx.getData();
  }

  public int texWidth() {
    return texWidth;
  }

  public int texHeight() {
    return texHeight;
  }

  public CGImage cgImage() {
    // TODO: make sure the image created by this call doesn't require any manual resource
    // releasing, other than being eventually garbage collected
    return bctx.toImage();
  }

  public void dispose() {
    if (bctx != null) {
      bctx.dispose();
      bctx = null;
    }
  }

  @Override
  public Canvas clear() {
    bctx.clearRect(new CGRect(0, 0, texWidth, texHeight));
    isDirty = true;
    return this;
  }

  @Override
  public Canvas clearRect(float x, float y, float width, float height) {
    bctx.clearRect(new CGRect(x, y, width, height));
    isDirty = true;
    return this;
  }

  @Override
  public Canvas clip(Path clipPath) {
    bctx.addPath(((RoboPath) clipPath).cgPath);
    bctx.clip();
    return this;
  }

  @Override
  public Canvas clipRect(float x, float y, float width, float height) {
    bctx.clipToRect(new CGRect(x, y, width, height));
    return this;
  }

  @Override
  public Path createPath() {
    return new RoboPath();
  }

  @Override
  public Canvas drawLine(float x0, float y0, float x1, float y1) {
    bctx.beginPath();
    bctx.moveToPoint(x0, y0);
    bctx.addLineToPoint(x1, y1);
    bctx.strokePath();
    isDirty = true;
    return this;
  }

  @Override
  public Canvas drawPoint(float x, float y) {
    save();
    setStrokeWidth(0.5f);
    strokeRect(x + 0.25f, y + 0.25f, 0.5f, 0.5f);
    restore();
    return this;
  }

  @Override
  public Canvas drawText(String text, float x, float y) {
    bctx.saveGState();
    bctx.translateCTM(x, y + RoboGraphics.defaultFont.ctFont.getDescent());
    bctx.scaleCTM(1, -1);
    bctx.selectFont(RoboGraphics.defaultFont.iosName(), RoboGraphics.defaultFont.size(),
                    CGTextEncoding.MacRoman);
    bctx.showTextAtPoint(0, 0, text);
    bctx.restoreGState();
    isDirty = true;
    return this;
  }

  @Override
  public Canvas fillCircle(float x, float y, float radius) {
    RoboGradient gradient = currentState().gradient;
    if (gradient == null) {
      bctx.fillEllipseInRect(new CGRect(x-radius, y-radius, 2*radius, 2*radius));
    } else {
      CGMutablePath cgPath = CGMutablePath.createMutable();
      cgPath.addArc(null, x, y, radius, 0, 2*Math.PI, false);
      bctx.addPath(cgPath);
      bctx.clip();
      gradient.fill(bctx);
    }
    isDirty = true;
    return this;
  }

  @Override
  public Canvas fillPath(Path path) {
    bctx.addPath(((RoboPath) path).cgPath);
    RoboGradient gradient = currentState().gradient;
    if (gradient == null) {
      bctx.fillPath();
    } else {
      bctx.clip();
      gradient.fill(bctx);
    }
    isDirty = true;
    return this;
  }

  @Override
  public Canvas fillRect(float x, float y, float width, float height) {
    RoboGradient gradient = currentState().gradient;
    if (gradient == null) {
      bctx.fillRect(new CGRect(x, y, width, height));
    } else {
      bctx.saveGState();
      bctx.clipToRect(new CGRect(x, y, width, height));
      gradient.fill(bctx);
      bctx.restoreGState();
    }
    isDirty = true;
    return this;
  }

  @Override
  public Canvas fillRoundRect(float x, float y, float width, float height, float radius) {
    addRoundRectPath(x, y, width, height, radius);
    RoboGradient gradient = currentState().gradient;
    if (gradient == null) {
      bctx.fillPath();
    } else {
      bctx.clip();
      gradient.fill(bctx);
    }
    isDirty = true;
    return this;
  }

  @Override
  public Canvas fillText(TextLayout layout, float x, float y) {
    RoboGradient gradient = currentState().gradient;
    RoboTextLayout ilayout = (RoboTextLayout) layout;
    if (gradient == null) {
      ilayout.fill(bctx, x, y, fillColor);

    } else {
      // draw our text into a fresh context so we can use it as a mask for the gradient
      CGBitmapContext maskContext = RoboGraphics.createCGBitmap(texWidth, texHeight);
      maskContext.clearRect(new CGRect(0, 0, texWidth, texHeight));
      // scale the context based on our scale factor
      maskContext.scaleCTM(ctx.scale.factor, ctx.scale.factor);
      // fill the text into this temp context in white for use as a mask
      setFillColor(maskContext, 0xFFFFFFFF);
      ilayout.fill(maskContext, 0, 0, fillColor);

      // now fill the gradient, using our temp context as a mask
      bctx.saveGState();
      bctx.clipToMask(new CGRect(x, y, width, height), maskContext.toImage());
      gradient.fill(bctx);
      bctx.restoreGState();

      // finally free the temp context
      maskContext.dispose();
    }

    isDirty = true;
    return this;
  }

  @Override
  public Canvas restore() {
    states.removeFirst();
    bctx.restoreGState();
    return this;
  }

  @Override
  public Canvas rotate(float radians) {
    bctx.rotateCTM(radians);
    return this;
  }

  @Override
  public Canvas save() {
    states.addFirst(new RoboCanvasState(currentState()));
    bctx.saveGState();
    return this;
  }

  @Override
  public Canvas scale(float x, float y) {
    bctx.scaleCTM(x, y);
    return this;
  }

  @Override
  public Canvas setAlpha(float alpha) {
    bctx.setAlpha(alpha);
    return this;
  }

  @Override
  public Canvas setCompositeOperation(Composite composite) {
    bctx.setBlendMode(compToBlend.get(composite));
    return this;
  }

  @Override
  public Canvas setFillColor(int color) {
    this.fillColor = color;
    currentState().gradient = null;
    setFillColor(bctx, color);
    return this;
  }

  @Override
  public Canvas setFillGradient(Gradient gradient) {
    currentState().gradient = (RoboGradient) gradient;
    return this;
  }

  @Override
  public Canvas setFillPattern(Pattern pattern) {
    currentState().gradient = null;
    // TODO: this anchors the fill pattern in the lower left; sigh
    bctx.setFillColor(((RoboPattern) pattern).colorWithPattern);
    return this;
  }

  @Override
  public Canvas setLineCap(LineCap cap) {
    bctx.setLineCap(decodeCap.get(cap));
    return this;
  }

  @Override
  public Canvas setLineJoin(LineJoin join) {
    bctx.setLineJoin(decodeJoin.get(join));
    return this;
  }

  @Override
  public Canvas setMiterLimit(float miter) {
    bctx.setMiterLimit(miter);
    return this;
  }

  @Override
  public Canvas setStrokeColor(int color) {
    this.strokeColor = color;
    setStrokeColor(bctx, color);
    return this;
  }

  @Override
  public Canvas setStrokeWidth(float strokeWidth) {
    this.strokeWidth = strokeWidth;
    bctx.setLineWidth(strokeWidth);
    return this;
  }

  @Override
  public Canvas strokeCircle(float x, float y, float radius) {
    bctx.strokeEllipseInRect(new CGRect(x-radius, y-radius, 2*radius, 2*radius));
    isDirty = true;
    return this;
  }

  @Override
  public Canvas strokePath(Path path) {
    bctx.addPath(((RoboPath) path).cgPath);
    bctx.strokePath();
    isDirty = true;
    return this;
  }

  @Override
  public Canvas strokeRect(float x, float y, float width, float height) {
    bctx.strokeRect(new CGRect(x, y, width, height));
    isDirty = true;
    return this;
  }

  @Override
  public Canvas strokeRoundRect(float x, float y, float width, float height, float radius) {
    addRoundRectPath(x, y, width, height, radius);
    bctx.strokePath();
    isDirty = true;
    return this;
  }

  @Override
  public Canvas strokeText(TextLayout layout, float x, float y) {
    ((RoboTextLayout) layout).stroke(bctx, x, y, strokeWidth, strokeColor);
    isDirty = true;
    return this;
  }

  @Override
  public Canvas transform(float m11, float m12, float m21, float m22, float dx, float dy) {
    bctx.concatCTM(new CGAffineTransform(m11, m12, m21, m22, dx, dy));
    return this;
  }

  @Override
  public Canvas translate(float x, float y) {
    bctx.translateCTM(x, y);
    return this;
  }

  @Override
  protected void finalize() {
    dispose(); // meh
  }

  @Override
  protected CGBitmapContext gc() {
    return bctx;
  }

  private void addRoundRectPath(float x, float y, float width, float height, float radius) {
    float midx = x + width/2, midy = y + height/2, maxx = x + width, maxy = y + height;
    bctx.beginPath();
    bctx.moveToPoint(x, midy);
    bctx.addArcToPoint(x, y, midx, y, radius);
    bctx.addArcToPoint(maxx, y, maxx, midy, radius);
    bctx.addArcToPoint(maxx, maxy, midx, maxy, radius);
    bctx.addArcToPoint(x, maxy, x, midy, radius);
    bctx.closePath();
  }

  private RoboCanvasState currentState() {
    return states.peek();
  }

  static void setStrokeColor(CGBitmapContext bctx, int color) {
    float blue = (color & 0xFF) / 255f;
    color >>= 8;
    float green = (color & 0xFF) / 255f;
    color >>= 8;
    float red = (color & 0xFF) / 255f;
    color >>= 8;
    float alpha = (color & 0xFF) / 255f;
    bctx.setRGBStrokeColor(red, green, blue, alpha);
  }

  static void setFillColor(CGBitmapContext bctx, int color) {
    float blue = (color & 0xFF) / 255f;
    color >>= 8;
    float green = (color & 0xFF) / 255f;
    color >>= 8;
    float red = (color & 0xFF) / 255f;
    color >>= 8;
    float alpha = (color & 0xFF) / 255f;
    bctx.setRGBFillColor(red, green, blue, alpha);
  }

  private static Map<Composite,CGBlendMode> compToBlend = new HashMap<Composite,CGBlendMode>();
  static {
    compToBlend.put(Composite.SRC, CGBlendMode.Copy);
    compToBlend.put(Composite.DST_ATOP, CGBlendMode.DestinationAtop);
    compToBlend.put(Composite.SRC_OVER, CGBlendMode.Normal);
    compToBlend.put(Composite.DST_OVER, CGBlendMode.DestinationOver);
    compToBlend.put(Composite.SRC_IN, CGBlendMode.SourceIn);
    compToBlend.put(Composite.DST_IN, CGBlendMode.DestinationIn);
    compToBlend.put(Composite.SRC_OUT, CGBlendMode.SourceOut);
    compToBlend.put(Composite.DST_OUT, CGBlendMode.DestinationOut);
    compToBlend.put(Composite.SRC_ATOP, CGBlendMode.SourceAtop);
    compToBlend.put(Composite.XOR, CGBlendMode.XOR);
    compToBlend.put(Composite.MULTIPLY, CGBlendMode.Multiply);
  }

  private static Map<LineCap,CGLineCap> decodeCap = new HashMap<LineCap,CGLineCap>();
  static {
    decodeCap.put(LineCap.BUTT, CGLineCap.Butt);
    decodeCap.put(LineCap.ROUND, CGLineCap.Round);
    decodeCap.put(LineCap.SQUARE, CGLineCap.Square);
  }

  private static Map<LineJoin,CGLineJoin> decodeJoin = new HashMap<LineJoin,CGLineJoin>();
  static {
    decodeJoin.put(LineJoin.BEVEL, CGLineJoin.Bevel);
    decodeJoin.put(LineJoin.MITER, CGLineJoin.Miter);
    decodeJoin.put(LineJoin.ROUND, CGLineJoin.Round);
  }
}
