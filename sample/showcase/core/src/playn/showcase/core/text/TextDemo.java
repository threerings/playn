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
package playn.showcase.core.text;

import playn.core.CanvasImage;
import playn.core.CanvasLayer;
import playn.core.Layer;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Font;
import playn.core.TextFormat;
import playn.core.TextLayout;
import static playn.core.PlayN.*;

import playn.showcase.core.Demo;

public class TextDemo extends Demo {
  private GroupLayer base;

  @Override
  public String name() {
    return "Text";
  }

  @Override
  public void init() {
    base = graphics().createGroupLayer();
    graphics().rootLayer().add(base);

    // draw a soothing flat background
    CanvasImage bgtile = graphics().createImage(64, 64);
    bgtile.canvas().setFillColor(0xFFCCCCCC);
    bgtile.canvas().fillRect(0, 0, 64, 64);
    bgtile.canvas().setStrokeColor(0xFFFFFFFF);
    bgtile.canvas().strokeRect(0, 0, 64, 64);

    ImageLayer bg = graphics().createImageLayer(bgtile);
    bg.setRepeatX(true);
    bg.setRepeatY(true);
    bg.setWidth(graphics().width());
    bg.setHeight(graphics().height());
    base.add(bg);

    // add some text to said soothing background
    final float MARGIN = 10;
    float xpos = MARGIN, maxYPos = 0;
    for (String name : new String[] { "Helvetica", "Times" }) {
      float ypos = MARGIN, maxWidth = 0;
      for (Font.Style style : Font.Style.values()) {
        for (float size : new float[] { 12f, 24f, 32f }) {
          Font font = graphics().createFont(name, style, size);
          TextFormat format = new TextFormat().withFont(font);
          TextLayout layout = graphics().layoutText("Hello PlayN World", format);
          Layer layer = createTextLayer(layout);
          layer.setTranslation(xpos, ypos);
          base.add(layer);
          ypos += layout.height();
          maxWidth = Math.max(maxWidth, layout.width());
          maxYPos = Math.max(ypos, maxYPos);
        }
      }
      xpos += (maxWidth + MARGIN);
    }

    // also add some wrapped text
    xpos = MARGIN;
    float ypos = maxYPos + MARGIN;
    Font font = graphics().createFont("Courier", Font.Style.PLAIN, 16);
    String text = "Text can also be wrapped at a specified width.\n\n" +
      "And wrapped manually at newlines.\nLike this.";
    TextLayout layout = graphics().layoutText(
      text, new TextFormat().withFont(font).withWrapWidth(200).
                             withEffect(TextFormat.Effect.shadow(0x33000000, -2, -2)).
                             withTextColor(0xFF660000));
    Layer layer = createTextLayer(layout);
    layer.setTranslation(xpos, ypos);
    base.add(layer);
    xpos += layout.width() + MARGIN;
    ypos += MARGIN;

    text = "Wrapped text can be center-justified, if so desired.";
    layout = graphics().layoutText(
      text, new TextFormat().withFont(font).withWrapping(200, TextFormat.Alignment.CENTER).
                             withEffect(TextFormat.Effect.shadow(0x33000000, 2, 2)).
                             withTextColor(0xFF006600));
    layer = createTextLayer(layout);
    layer.setTranslation(xpos, ypos);
    base.add(layer);
    xpos += layout.width() + MARGIN;
    ypos += MARGIN;

    text = "Or it can be flush to the right, if that's how you like to justify yourself.";
    layout = graphics().layoutText(
      text, new TextFormat().withFont(font).withWrapping(200, TextFormat.Alignment.RIGHT).
                             withEffect(TextFormat.Effect.outline(0xFFFFFFFF)).
                             withTextColor(0xFF000066));
    layer = createTextLayer(layout);
    layer.setTranslation(xpos, ypos);
    base.add(layer);
    xpos += layout.width() + MARGIN;
    ypos += MARGIN;
  }

  @Override
  public void shutdown() {
    base.destroy();
    base = null;
  }

  protected Layer createTextLayer(TextLayout layout) {
    CanvasLayer layer = graphics().createCanvasLayer(
      (int)Math.ceil(layout.width()), (int)Math.ceil(layout.height()));
    layer.canvas().drawText(layout, 0, 0);
    return layer;
  }
}
