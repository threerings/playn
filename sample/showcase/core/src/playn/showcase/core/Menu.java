//
// $Id$

package playn.showcase.core;

import playn.core.CanvasLayer;
import playn.core.GroupLayer;
import playn.core.Keyboard;

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

    int width = graphics().width(), height = graphics().height();
    CanvasLayer bg = graphics().createCanvasLayer(width, height);
    bg.canvas().setFillColor(0xFF99CCFF);
    bg.canvas().fillRect(0, 0, width, height);

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
    bg.canvas().drawText("Press # key to run demo, ESC key to return to menu from demo", 25, ypos);

    layer.add(bg);
  }

  @Override
  public void shutdown() {
    layer.destroy();
    layer = null;
  }

  @Override
  public void update(float delta) {
  }

  @Override
  public void paint(float alpha) {
  }

  @Override
  public Keyboard.Listener keyboardListener() {
    return keyListener;
  }
}
