/**
 * Copyright 2011 The ForPlay Authors
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
package playn.showcase.core;

import playn.core.CanvasLayer;
import playn.core.GroupLayer;
import playn.core.Keyboard;
import playn.core.Platform;

import react.UnitSlot;

import tripleplay.ui.AxisLayout;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Interface;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.Style;
import tripleplay.ui.Styles;
import tripleplay.ui.Stylesheet;

import static playn.core.PlayN.*;

/**
 * A demo that displays a menu of the available demos.
 */
public class Menu extends Demo
{
  private final Keyboard.Listener keyListener = new Keyboard.Adapter() {
    public void onKeyDown(Keyboard.Event event) {
      // TODO: use proper key codes
      int keyCode = event.keyCode();
      int demoIndex = keyCode - '1';
      if (demoIndex >= 0 && demoIndex < showcase.demos.size()) {
        showcase.activateDemo(showcase.demos.get(demoIndex));
      }
    }
  };

  private final Showcase showcase;

  private Interface iface;
  private GroupLayer layer;

  public Menu (Showcase showcase) {
    this.showcase = showcase;
  }

  @Override
  public String name() {
    return "Menu";
  }

  @Override
  public void init() {
    layer = graphics().createGroupLayer();
    graphics().rootLayer().add(layer);

    // Flash does not support the text rendering needed to use the TriplePlay UI framework
    if (platformType() != Platform.Type.FLASH) {
      // create our UI manager and configure it to process pointer events
      iface = new Interface(null);
      pointer().setListener(iface.plistener);

      // define our root stylesheet
      Styles buttonStyles = Styles.none().
        add(Style.BACKGROUND.is(Background.solid(0xFFFFFFFF, 5))).
        addSelected(Style.BACKGROUND.is(Background.solid(0xFFCCCCCC, 6, 4, 4, 6)));
      Stylesheet rootSheet = Stylesheet.builder().
        add(Button.class, buttonStyles).
        create();

      // create our demo interface
      Root root = iface.createRoot(AxisLayout.vertical().gap(15), rootSheet);
      root.setSize(graphics().width(), graphics().height());
      root.addStyles(Styles.make(Style.BACKGROUND.is(Background.solid(0xFF99CCFF, 5))));
      layer.add(root.layer);

      Group buttons;
      root.add(new Label("PlayN Demos:"),
               buttons = new Group(AxisLayout.vertical().offStretch()),
               new Label("ESC key returns to menu from demo"));

      int key = 1;
      for (final Demo demo : showcase.demos) {
        Button button = new Button().setText(key++ + " - " + demo.name());
        buttons.add(button);
        button.clicked().connect(new UnitSlot() {
          public void onEmit() {
            showcase.activateDemo(demo);
          }
        });
      }

    } else {
      // display a solid background
      int width = graphics().width(), height = graphics().height();
      CanvasLayer bg = graphics().createCanvasLayer(width, height);
      bg.canvas().setFillColor(0xFF99CCFF);
      bg.canvas().fillRect(0, 0, width, height);
      layer.add(bg);

      // draw a primitive menu
      bg.canvas().setFillColor(0xFF000000);
      float ypos = 25;
      bg.canvas().drawText("PlayN Demos:", 25, ypos);
      ypos += 25;

      int key = 1;
      for (Demo demo : showcase.demos) {
        bg.canvas().drawText(key++ + " - " + demo.name(), 25, ypos);
        ypos += 25;
      }
      ypos += 25;
      bg.canvas().drawText("Press # key to run demo, ESC key returns to menu from demo", 25, ypos);
    }
  }

  @Override
  public void shutdown() {
    if (iface != null) {
      pointer().setListener(null);
      iface = null;
    }
    layer.destroy();
    layer = null;
  }

  @Override
  public void update(float delta) {
    if (iface != null) {
      iface.update(delta);
    }
  }

  @Override
  public void paint(float alpha) {
    if (iface != null) {
      iface.paint(alpha);
    }
  }

  @Override
  public Keyboard.Listener keyboardListener() {
    return keyListener;
  }
}
