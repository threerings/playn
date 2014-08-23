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

package playn.tests.core;

import playn.core.*;
import playn.core.gl.GLShader;
import playn.core.gl.IndexedTrisShader;
import playn.core.util.TextBlock;

public abstract class Test {

  public static final int UPDATE_RATE = 25;

  final Platform platform;
  final TextFormat textFmt;

  public Test(Platform platform) {
    this.platform = platform;
    this.textFmt = new TextFormat().withFont(
            platform.graphics().createFont("Helvetica", Font.Style.PLAIN, 12));
  }

  public abstract String getName();

  public abstract String getDescription();

  public void init() {
  }

  public void update(int delta) {
  }

  public void paint(float alpha) {
  }

  public void dispose() {
  }

  public boolean usesPositionalInputs () {
    return false;
  }

  public boolean available () {
    return true;
  }

  protected float addTest(float lx, float ly, Layer.HasSize layer, String descrip) {
    return addTest(lx, ly, layer, descrip, layer.width());
  }

  protected float addTest(float lx, float ly, Layer.HasSize layer, String descrip, float twidth) {
    return addTest(lx, ly, layer, layer.width(), layer.height(), descrip, twidth);
  }

  protected float addTest(float lx, float ly, Layer layer, float lwidth, float lheight,
                         String descrip) {
    return addTest(lx, ly, layer, lwidth, lheight, descrip, lwidth);
  }

  protected float addTest(float lx, float ly, Layer layer, float lwidth, float lheight,
                          String descrip, float twidth) {
    platform.graphics().rootLayer().addAt(layer, lx + (twidth-lwidth)/2, ly);
    return addDescrip(descrip, lx, ly + lheight + 5, twidth);
  }

  protected float addDescrip(String descrip, float x, float y, float width) {
    ImageLayer layer = createDescripLayer(descrip, width);
    platform.graphics().rootLayer().addAt(layer, Math.round(x + (width - layer.width())/2), y);
    return y + layer.height();
  }

  protected ImageLayer createDescripLayer(String descrip, float width) {
    return platform.graphics().createImageLayer(wrapText(descrip, width, TextBlock.Align.CENTER));
  }

  protected CanvasImage wrapText(String text, float width, TextBlock.Align align) {
    TextLayout[] layouts = platform.graphics().layoutText(text, textFmt, new TextWrap(width));
    return new TextBlock(platform, layouts).toImage(align, 0xFF000000);
  }

  protected CanvasImage formatText (String text, boolean border) {
    return formatText(platform, textFmt, text, border);
  }

  protected static CanvasImage formatText (Platform platform, TextFormat format, String text, boolean border) {
    TextLayout layout = platform.graphics().layoutText(text, format);
    float margin = border ? 10 : 0;
    float width = layout.width()+2*margin, height = layout.height()+2*margin;
    CanvasImage image = platform.graphics().createImage(width, height);
    image.canvas().setStrokeColor(0xFF000000);
    image.canvas().setFillColor(0xFF000000);
    image.canvas().fillText(layout, margin, margin);
    if (border)
      image.canvas().strokeRect(0, 0, width-1, height-1);
    return image;
  }

  protected ImageLayer createButton (String text, final Runnable onClick) {
    CanvasImage image = formatText(text, true);
    ImageLayer button = platform.graphics().createImageLayer(image);
    button.addListener(new Pointer.Adapter() {
      @Override public void onPointerStart(Pointer.Event event) {
        onClick.run();
      }
    });
    return button;
  }

  protected float addButton (String text, Runnable onClick, float x, float y) {
    ImageLayer button = createButton(text, onClick);
    platform.graphics().rootLayer().addAt(button, x, y);
    return x + button.width() + 10;
  }

  protected GLShader createSepiaShader() {
    return (platform.graphics().ctx() == null) ? null : new IndexedTrisShader(platform.graphics().ctx()) {
      @Override protected String textureFragmentShader() {
        return "#ifdef GL_ES\n" +
          "precision highp float;\n" +
          "#endif\n" +

          "uniform sampler2D u_Texture;\n" +
          "varying vec2 v_TexCoord;\n" +
          "varying vec4 v_Color;\n" +

          "void main(void) {\n" +
          "  vec4 textureColor = texture2D(u_Texture, v_TexCoord);\n" +
          "  textureColor.rgb *= v_Color.rgb;\n" +
          "  float grey = dot(textureColor.rgb, vec3(0.299, 0.587, 0.114));\n" +
          "  gl_FragColor = vec4(grey * vec3(1.2, 1.0, 0.8), textureColor.a) * v_Color.a;\n" +
          "}";
      }
    };
  }
}
