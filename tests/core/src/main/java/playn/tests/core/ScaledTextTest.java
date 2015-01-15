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
import playn.scene.*;

public class ScaledTextTest extends Test {

  public ScaledTextTest (TestsGame game) {
    super(game, "ScaledTextTest", "Tests that text rendering to scaled Canvas works properly.");
  }

  @Override public void init() {
    String text = "The quick brown fox jumped over the lazy dog.";
    TextFormat format = new TextFormat(new Font("Helvetica", 18));
    TextBlock block = new TextBlock(game.graphics.layoutText(text, format, new TextWrap(100)));

    float x = 5;
    for (float scale : new float[] { 1f, 2f, 3f }) {
      float swidth = block.bounds.width() * scale, sheight = block.bounds.height() * scale;
      Canvas canvas = game.graphics.createCanvas(swidth, sheight);
      canvas.setStrokeColor(0xFFFFCCCC).strokeRect(0, 0, swidth-0.5f, sheight-0.5f);
      canvas.scale(scale, scale);
      canvas.setFillColor(0xFF000000);
      block.fill(canvas, TextBlock.Align.RIGHT, 0, 0);
      game.rootLayer.addAt(new ImageLayer(game.graphics, canvas.image), x, 5);
      addInfo(canvas, x + swidth/2, sheight + 10);
      x += swidth + 5;
    }
  }

  protected void addInfo (Canvas canvas, float cx, float y) {
    TextFormat infoFormat = new TextFormat(new Font("Helvetica", 12));
    TextLayout ilayout = game.graphics.layoutText(canvas.width + "x" + canvas.height, infoFormat);
    Canvas iimage = game.graphics.createCanvas(ilayout.size);
    iimage.setFillColor(0xFF000000).fillText(ilayout, 0, 0);
    game.rootLayer.addAt(new ImageLayer(game.graphics, iimage.image), cx - iimage.width/2, y);
  }
}
