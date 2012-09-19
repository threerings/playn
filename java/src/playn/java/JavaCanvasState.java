/**
 * Copyright 2010 The PlayN Authors
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

import playn.core.Canvas;
import playn.core.Canvas.Composite;
import playn.core.Canvas.LineCap;
import playn.core.Canvas.LineJoin;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;

class JavaCanvasState {

  public interface Clipper {
    void setClip(Graphics2D gfx);
  }

  private static Clipper NOCLIP = new Clipper() {
    @Override
    public void setClip(Graphics2D gfx) {
      gfx.setClip(null);
    }
  };

  int fillColor;
  int strokeColor;
  JavaGradient fillGradient;
  JavaPattern fillPattern;
  AffineTransform transform;
  float strokeWidth;
  LineCap lineCap;
  LineJoin lineJoin;
  float miterLimit;
  Clipper clipper;
  Composite composite;
  float alpha;

  JavaCanvasState() {
    this(0xff000000, 0xffffffff, null, null, new AffineTransform(), 1.0f, LineCap.SQUARE,
         LineJoin.MITER, 10.0f, NOCLIP, Composite.SRC_OVER, 1);
 }

  JavaCanvasState(JavaCanvasState toCopy) {
    this(toCopy.fillColor, toCopy.strokeColor, toCopy.fillGradient, toCopy.fillPattern,
         toCopy.transform, toCopy.strokeWidth, toCopy.lineCap, toCopy.lineJoin, toCopy.miterLimit,
         toCopy.clipper, toCopy.composite, toCopy.alpha);
  }

  JavaCanvasState(int fillColor, int strokeColor, JavaGradient fillGradient, JavaPattern fillPattern,
      AffineTransform transform, float strokeWidth, LineCap lineCap, LineJoin lineJoin,
      float miterLimit, Clipper clipper, Composite composite, float alpha) {
    this.fillColor = fillColor;
    this.strokeColor = strokeColor;
    this.fillGradient = fillGradient;
    this.fillPattern = fillPattern;
    this.transform = transform;
    this.strokeWidth = strokeWidth;
    this.lineCap = lineCap;
    this.lineJoin = lineJoin;
    this.miterLimit = miterLimit;
    this.clipper = clipper;
    this.composite = composite;
    this.alpha = alpha;
  }

  // TODO: optimize this so we're not setting this stuff all the time.
  void prepareStroke(Graphics2D gfx) {
    gfx.setStroke(new BasicStroke(strokeWidth, convertLineCap(), convertLineJoin(), miterLimit));
    gfx.setColor(convertColor(strokeColor));
    clipper.setClip(gfx);
    gfx.setComposite(convertComposite(composite, alpha));
  }

  // TODO: optimize this so we're not setting this stuff all the time.
  void prepareFill(Graphics2D gfx) {
    if (fillGradient != null) {
      gfx.setPaint(fillGradient.paint);
    } else if (fillPattern != null) {
      TexturePaint paint = fillPattern.paint;
      if (paint != null) {
        gfx.setPaint(paint);
      }
    } else {
      gfx.setPaint(convertColor(fillColor));
    }
    clipper.setClip(gfx);
    gfx.setComposite(convertComposite(composite, alpha));
  }

  static Color convertColor(int color) {
    float a = (color >>> 24) / 255.0f;
    float r = ((color >>> 16) & 0xff) / 255.0f;
    float g = ((color >>> 8) & 0xff) / 255.0f;
    float b = (color & 0xff) / 255.0f;

    return new Color(r, g, b, a);
  }

  private java.awt.Composite convertComposite(Canvas.Composite composite, float alpha) {
    switch (composite) {
    case DST_ATOP: return AlphaComposite.DstAtop.derive(alpha);
    case DST_IN: return AlphaComposite.DstIn.derive(alpha);
    case DST_OUT: return AlphaComposite.DstOut.derive(alpha);
    case DST_OVER: return AlphaComposite.DstOver.derive(alpha);
    case SRC: return AlphaComposite.Src.derive(alpha);
    case SRC_ATOP: return AlphaComposite.SrcAtop.derive(alpha);
    case SRC_IN: return AlphaComposite.SrcIn.derive(alpha);
    case SRC_OUT: return AlphaComposite.SrcOut.derive(alpha);
    case SRC_OVER: return AlphaComposite.SrcOver.derive(alpha);
    case XOR: return AlphaComposite.Xor.derive(alpha);
    case MULTIPLY: return BlendComposite.Multiply.derive(alpha);
    default: return AlphaComposite.Src.derive(alpha);
    }
  }

  private int convertLineCap() {
    switch (lineCap) {
      case BUTT:
        return BasicStroke.CAP_BUTT;
      case ROUND:
        return BasicStroke.CAP_ROUND;
      case SQUARE:
        return BasicStroke.CAP_SQUARE;
    }
    return BasicStroke.CAP_SQUARE;
  }

  private int convertLineJoin() {
    switch (lineJoin) {
      case BEVEL:
        return BasicStroke.JOIN_BEVEL;
      case MITER:
        return BasicStroke.JOIN_MITER;
      case ROUND:
        return BasicStroke.JOIN_ROUND;
    }
    return BasicStroke.JOIN_MITER;
  }
}
