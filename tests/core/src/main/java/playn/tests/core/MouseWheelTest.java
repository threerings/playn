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
import playn.core.Mouse.WheelEvent;

public class MouseWheelTest extends Test
{
  private static final float HEIGHT = 300;
  private static final float WIDTH = 30;
  private static final float HWIDTH = WIDTH / 2;

  private ImageLayer il;

  public MouseWheelTest(Platform platform) {
    super(platform);
  }

  @Override public void init () {
    GroupLayer slider = platform.graphics().createGroupLayer();

    CanvasImage image = platform.graphics().createImage(WIDTH + 10, HEIGHT);
    image.canvas().setFillColor(0xff808080);
    image.canvas().fillRect(0, 0, WIDTH + 10, HEIGHT);
    ImageLayer bg = platform.graphics().createImageLayer(image);
    slider.add(bg);

    image = platform.graphics().createImage(WIDTH, HWIDTH);
    image.canvas().setFillColor(0xffffffff);
    image.canvas().fillRect(0, 0, WIDTH, HWIDTH);
    image.canvas().setStrokeColor(0xff000000);
    image.canvas().drawLine(0, HWIDTH / 2, WIDTH, HWIDTH / 2);
    image.canvas().setStrokeColor(0xffff0000);
    image.canvas().strokeRect(0, 0, WIDTH - 1, HWIDTH - 1);
    slider.add(il = platform.graphics().createImageLayer(image));
    il.setOrigin(0, HWIDTH / 2);
    il.setTranslation(0, HEIGHT / 2);
    il.setDepth(1);

    platform.graphics().rootLayer().add(slider);
    slider.setTranslation(25, 25);

    bg.addListener(new Mouse.LayerAdapter() {
      @Override public void onMouseWheelScroll (WheelEvent event) {
        float y = il.ty() + event.velocity();
        y = Math.max(0, Math.min(y, HEIGHT));
        il.setTranslation(0, y);
      }
    });
  }

  @Override public String getName () {
    return "MouseWheelTest";
  }

  @Override public String getDescription () {
    return "Tests mouse wheel movement on layers";
  }
}
