/**
 * Copyright 2010 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package playn.flash;

import com.google.gwt.canvas.dom.client.CssColor;

import flash.display.StageAlign;
import flash.display.StageScaleMode;
import flash.display.Sprite;

import playn.core.Asserts;
import playn.core.CanvasImage;
import playn.core.CanvasLayer;
import playn.core.Font;
import playn.core.PlayN;
import playn.core.Gradient;
import playn.core.Graphics;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImmediateLayer;
import playn.core.ImageLayer;
import playn.core.Path;
import playn.core.Pattern;
import playn.core.SurfaceLayer;
import playn.core.TextFormat;
import playn.core.TextLayout;

import java.util.HashMap;
import java.util.Map;

class FlashGraphics implements Graphics {

  private final Map<Font,FlashFontMetrics> fontMetrics = new HashMap<Font,FlashFontMetrics>();
    private FlashCanvasLayer.CanvasElement dummyCanvas;
    private FlashCanvasLayer.Context2d dummyCtx;

    static CssColor cssColor(int color) {
    return CssColor.make(cssColorString(color));
  }

  static String cssColorString(int color) {
    double a = ((color >> 24) & 0xff) / 255.0;
    int r = (color >> 16) & 0xff;
    int g = (color >> 8) & 0xff;
    int b = (color >> 0) & 0xff;
    return "rgba(" + r + "," + g + "," + b + "," + a + ")";
  }

    FlashFontMetrics getFontMetrics(Font font) {
        FlashFontMetrics metrics = fontMetrics.get(font);
        if (metrics == null) {

            metrics = new FlashFontMetrics(12, 10);
            fontMetrics.put(font, metrics);
        }
        return metrics;
    }

    protected FlashGraphics() {
    rootLayer = FlashGroupLayer.getRoot();
    setSize (screenWidth(), screenHeight());
    Sprite.getRootSprite().getStage().setScaleMode(StageScaleMode.NO_SCALE);
    Sprite.getRootSprite().getStage().setStageAlign(StageAlign.TOP_LEFT);
    PlayN.log().info("Graphics System Initialized: Dimensions ("
        + screenWidth() + " x " + screenHeight() + ")");
    dummyCanvas = FlashCanvasLayer.CanvasElement.create();
    dummyCtx = dummyCanvas.getContext();
  }

  FlashGroupLayer rootLayer;
  @Override
  public GroupLayer rootLayer() {
    return rootLayer;
  }

  @Override @Deprecated
  public CanvasLayer createCanvasLayer(int width, int height) {
    return new FlashCanvasLayer(width, height);
  }

  @Override
  public GroupLayer createGroupLayer() {
    return new FlashGroupLayer();
  }

  @Override
  public SurfaceLayer createSurfaceLayer(int width, int height) {
    return new FlashSurfaceLayer(width, height);
  }

  @Override
  public ImmediateLayer.Clipped createImmediateLayer(
      int width, int height, ImmediateLayer.Renderer renderer) {
    throw new UnsupportedOperationException("Immediate layer not supported by Flash");
  }

  @Override
  public ImmediateLayer createImmediateLayer(ImmediateLayer.Renderer renderer) {
    throw new UnsupportedOperationException("Immediate layer not supported by Flash");
  }

  @Override
  public ImageLayer createImageLayer(Image image) {
    return new FlashImageLayer(image);
  }

  @Override
  public CanvasImage createImage(int w, int h) {
    FlashCanvas surface = new FlashCanvas(w, h, FlashCanvasLayer.CanvasElement.create(w, h).getContext());
    return new FlashCanvasImage(surface);
  }

  @Override
  public Gradient createLinearGradient(float x0, float y0, float x1, float y1,
      int[] colors, float[] positions) {
    Asserts.checkArgument(colors.length == positions.length);
    return new FlashGradient();
  }

  @Override
  public Path createPath() {
    return new FlashPath();
  }

  @Override
  public Pattern createPattern(Image img) {
    Asserts.checkArgument(img instanceof FlashImage);
    return new FlashPattern(img);
  }

  @Override
  public Gradient createRadialGradient(float x, float y, float r, int[] colors,
      float[] positions) {
    Asserts.checkArgument(colors.length == positions.length);

    return new FlashGradient();
  }

  @Override
  public Font createFont(String name, Font.Style style, float size) {
    return new FlashFont(name, style, size);
  }

  @Override
  public TextLayout layoutText(String text, TextFormat format) {
    return new FlashTextLayout(dummyCtx, text, format);
  }

  @Override
  public int screenHeight() {
    return Sprite.getRootSprite().getStage().getStageHeight();
  }

  @Override
  public int screenWidth() {
    return Sprite.getRootSprite().getStage().getStageWidth();
  }

  @Override
  public int width() {
    return screenWidth();
  }

  @Override
  public int height() {
    return screenHeight();
  }

  @Override
  public void setSize(int width, int height) {
    PlayN.log().info("Setting size " + width + "x" + height);
//    Sprite.getRootSprite().setWidth(width);
//    Sprite.getRootSprite().setHeight(height);
  }

  public void updateLayers() {
    rootLayer.update();
  }

  /* (non-Javadoc)
   * @see playn.core.Graphics#createImageLayer()
   */
  @Override
  public ImageLayer createImageLayer() {
    return new FlashImageLayer();
  }
}
