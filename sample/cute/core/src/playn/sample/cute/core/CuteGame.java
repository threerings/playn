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
package playn.sample.cute.core;

import java.util.HashMap;
import java.util.Map;

import static playn.core.PlayN.*;
import playn.core.Key;

import playn.core.Game;
import playn.core.Json;
import playn.core.Keyboard;
import playn.core.Pointer;
import playn.core.Surface;
import playn.core.SurfaceLayer;
import playn.core.util.Callback;

public class CuteGame implements Game, Keyboard.Listener {

  private static final int NUM_STARS = 10;

  private static Map<Key, Integer> ADD_TILE_KEYS = new HashMap<Key, Integer>();
  static {
    int idx = 0;
    for (Key key : new Key[] {
        Key.K1, Key.K2, Key.K3, Key.K4, Key.K5, Key.K6, Key.K7, Key.K8,
        Key.W, Key.D, Key.S, Key.A, Key.T, Key.Y, Key.H, Key.N, Key.B, Key.V, Key.F, Key.R }) {
      ADD_TILE_KEYS.put(key, idx++);
    }
  }

  private SurfaceLayer gameLayer;

  private CuteWorld world;
  private CuteObject catGirl;
  private CuteObject[] stars;

  private boolean controlLeft, controlRight, controlUp, controlDown;
  private boolean controlJump;
  private float touchVectorX, touchVectorY;

  @Override
  public void init() {
    graphics().setSize(800, 600);

    gameLayer = graphics().createSurfaceLayer(graphics().width(), graphics().height());
    graphics().rootLayer().add(gameLayer);

    keyboard().setListener(this);
    pointer().setListener(new Pointer.Listener() {
      @Override
      public void onPointerEnd(Pointer.Event event) {
        touchVectorX = touchVectorY = 0;
      }
      @Override
      public void onPointerDrag(Pointer.Event event) {
        touchMove(event.x(), event.y());
      }
      @Override
      public void onPointerStart(Pointer.Event event) {
        touchMove(event.x(), event.y());
      }
    });

    // TODO(jgw): Until net is filled in everywhere, create a simple grass world.

    /*
    platform().net().get("/rpc?map", new Callback<String>() {
      @Override
      public void onSuccess(String json) {
        DataObject data = platform().parseData(json);
        world = new CuteWorld(plat, data);
        initStuff();
      }

      @Override
      public void onFailure(Throwable error) {
        platform().log("error loading map");
      }
    });
    */

    world = new CuteWorld(16, 16);

    // Grass.
    for (int y = 0; y < 16; ++y) {
      for (int x = 0; x < 16; ++x) {
        world.addTile(x, y, 2);
      }
    }

    // And a little house.
    for (int i = 0; i < 2; ++i) {
      world.addTile(4, 4, 7);
      world.addTile(5, 4, 7);
      world.addTile(6, 4, 7);
      world.addTile(4, 5, 7);
      world.addTile(5, 5, 7);
      world.addTile(6, 5, 7);
      world.addTile(4, 6, 7);
      world.addTile(5, 6, 3);
      world.addTile(6, 6, 7);
    }

    world.addTile(4, 4, 19);
    world.addTile(5, 4, 12);
    world.addTile(6, 4, 13);
    world.addTile(4, 5, 18);
    world.addTile(5, 5,  5);
    world.addTile(6, 5, 14);
    world.addTile(4, 6, 17);
    world.addTile(5, 6, 16);
    world.addTile(6, 6, 15);

    initStuff();
  }

  private void initStuff() {
    catGirl = new CuteObject(assetManager().getImage("images/character_cat_girl.png"));
    catGirl.setPos(2, 2, 1);
    catGirl.r = 0.3;
    world.addObject(catGirl);

    stars = new CuteObject[NUM_STARS];
    for (int i = 0; i < NUM_STARS; ++i) {
      stars[i] = new CuteObject(assetManager().getImage("images/star.png"));
      stars[i].setPos(Math.random() * world.worldWidth(), Math.random()
          * world.worldHeight(), 10);
      world.addObject(stars[i]);
    }
  }

  @Override
  public void onKeyDown(Keyboard.Event event) {
    Integer tileIdx = ADD_TILE_KEYS.get(event.key());
    if (tileIdx != null) {
      addTile((int) catGirl.x, (int) catGirl.y, tileIdx);
      return;
    }

    switch (event.key()) {
      case SPACE:
        controlJump = true;
        break;
      case ESCAPE:
        removeTopTile((int) catGirl.x, (int) catGirl.y);
        break;
      case LEFT:
        controlLeft = true;
        break;
      case UP:
        controlUp = true;
        break;
      case RIGHT:
        controlRight = true;
        break;
      case DOWN:
        controlDown = true;
        break;
    }
  }

  @Override
  public void onKeyTyped(Keyboard.TypedEvent event) {
    // nada
  }

  @Override
  public void onKeyUp(Keyboard.Event event) {
    switch (event.key()) {
      case LEFT:
        controlLeft = false;
        break;
      case UP:
        controlUp = false;
        break;
      case RIGHT:
        controlRight = false;
        break;
      case DOWN:
        controlDown = false;
        break;
    }
  }

  @Override
  public void update(float delta) {
    if (world == null) {
      return;
    }

    catGirl.setAcceleration(0, 0, 0);

    if (catGirl.isResting()) {
      // Keyboard control.
      if (controlLeft) {
        catGirl.ax = -1.0;
      }
      if (controlRight) {
        catGirl.ax = 1.0;
      }
      if (controlUp) {
        catGirl.ay = -1.0;
      }
      if (controlDown) {
        catGirl.ay = 1.0;
      }

      // Mouse Control.
      catGirl.ax += touchVectorX;
      catGirl.ay += touchVectorY;

      // Jump Control.
      if (controlJump) {
        catGirl.vz = 0.2;
        controlJump = false;
      }
    }

    world.updatePhysics(delta / 1000);
  }

  @Override
  public void paint(float alpha) {
    if (world == null) {
      return;
    }

    world.setViewOrigin(catGirl.x(alpha), catGirl.y(alpha), catGirl.z(alpha));

    Surface surface = gameLayer.surface();
    surface.clear();
    world.paint(surface, alpha);
  }

  private void touchMove(float x, float y) {
    float cx = graphics().screenWidth() / 2;
    float cy = graphics().screenHeight() / 2;

    touchVectorX = (x - cx) * 1.0f / cx;
    touchVectorY = (y - cy) * 1.0f / cy;
  }

  private void addTile(int x, int y, int type) {
    world.addTile(x, y, type);

    Json.Writer w = json().newWriter();
    w.object();
    w.key("op"); w.value("addTop");
    w.key("x"); w.value(x);
    w.key("y"); w.value(y);
    w.key("type"); w.value(type);
    w.endObject();

    post(w.write());
  }

  private void removeTopTile(int x, int y) {
    world.removeTopTile(x, y);

    Json.Writer w = json().newWriter();
    w.object();
    w.key("op"); w.value("removeTop");
    w.key("x"); w.value(x);
    w.key("y"); w.value(y);
    w.endObject();

    post(w.write());
  }

  private void post(String payload) {
    net().post("/rpc", payload, new Callback<String>() {
      @Override
      public void onSuccess(String response) {
        // Nada.
      }

      @Override
      public void onFailure(Throwable error) {
        // TODO
      }
    });
  }

  @Override
  public int updateRate() {
    return 33;
  }
}
