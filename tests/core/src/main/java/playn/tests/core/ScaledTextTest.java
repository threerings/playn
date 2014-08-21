/**
 * Copyright 2012 The PlayN Authors
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
import playn.core.util.TextBlock;

public class ScaledTextTest extends Test {

  protected final TextFormat infoFormat;

  public ScaledTextTest(Platform platform) {
    super(platform);
    infoFormat = new TextFormat().
            withFont(platform.graphics().createFont("Helvetica", Font.Style.PLAIN, 12));
  }

  @Override
  public String getName() {
    return "ScaledTextTest";
  }

  @Override
  public String getDescription() {
    return "Tests that text rendering to scaled Canvas works properly.";
  }

  @Override
  public void init() {
    String text = "The quick brown fox jumped over the lazy dog.";
    TextFormat format = new TextFormat().
      withFont(platform.graphics().createFont("Helvetica", Font.Style.PLAIN, 18));
    TextBlock block = new TextBlock(platform, platform.graphics().layoutText(text, format, new TextWrap(100)));

    float x = 5;
    for (float scale : new float[] { 1f, 2f, 3f }) {
      float swidth = block.bounds.width() * scale, sheight = block.bounds.height() * scale;
      CanvasImage image = platform.graphics().createImage(swidth, sheight);
      image.canvas().setStrokeColor(0xFFFFCCCC).strokeRect(0, 0, swidth-0.5f, sheight-0.5f);
      image.canvas().scale(scale, scale);
      image.canvas().setFillColor(0xFF000000);
      block.fill(image.canvas(), TextBlock.Align.RIGHT, 0, 0);
      platform.graphics().rootLayer().addAt(platform.graphics().createImageLayer(image), x, 5);
      addInfo(image, x + swidth/2, sheight + 10);
      x += swidth + 5;
    }
  }

  protected void addInfo (CanvasImage image, float cx, float y) {
    TextLayout ilayout = platform.graphics().layoutText(image.width() + "x" + image.height(), infoFormat);
    CanvasImage iimage = platform.graphics().createImage(ilayout.width(), ilayout.height());
    iimage.canvas().setFillColor(0xFF000000).fillText(ilayout, 0, 0);
    platform.graphics().rootLayer().addAt(platform.graphics().createImageLayer(iimage), cx - iimage.width()/2, y);
  }
}
