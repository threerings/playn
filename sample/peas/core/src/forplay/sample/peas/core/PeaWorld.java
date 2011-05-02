/**
 * Copyright 2010 The ForPlay Authors
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
package forplay.sample.peas.core;

import static forplay.core.ForPlay.assetManager;
import static forplay.core.ForPlay.graphics;
import forplay.core.AssetWatcher;
import forplay.core.Canvas;
import forplay.core.CanvasLayer;
import forplay.core.GroupLayer;
import forplay.core.Image;

import java.util.LinkedList;
import java.util.List;

public class PeaWorld {

  private static final int TILE_SIZE_X = 71;
  private static final int TILE_SIZE_Y = 49;

  private static final int MAP_WIDTH = 11;
  private static final int MAP_HEIGHT = 11;

  public static final float GRAVITY = 5;
  public static final float RESTITUTION = 0.2f;

  class Pea extends Entity {
    static final int RADIUS = 18;

    public Pea(GroupLayer parentLayer) {
      super(parentLayer, graphics().createImageLayer(
          assetManager().getImage("images/Pea-Standard.png")), RADIUS);
    }

    @Override
    public void update(float delta) {
      super.update(delta);

      // Gravity.
      ay += GRAVITY;

      // Collision.
      int tx = (int) (x / TILE_SIZE_X);
      int ty = (int) (y / TILE_SIZE_Y);
      tile(tx + 1, ty).collide(tx + 1, ty, this);
      tile(tx, ty + 1).collide(tx, ty + 1, this);
      tile(tx - 1, ty).collide(tx - 1, ty, this);
      tile(tx, ty - 1).collide(tx, ty - 1, this);
    }
  }

  class Tile {
    Image image;

    Tile() {
    }

    Tile(String name) {
      image = assetManager().getImage("images/" + name + ".png");
    }

    void collide(int tx, int ty, Entity ent) {
      float left = tx * TILE_SIZE_X, top = ty * TILE_SIZE_Y;
      float right = left + TILE_SIZE_X, bottom = top + TILE_SIZE_Y;
      float cx = (left + right) / 2;
      float cy = (top + bottom) / 2;

      if (Math.abs(ent.x - cx) > Math.abs(Math.abs(ent.y - cy))) {
        if (ent.x < cx) {
          collideRight(left, top, left, bottom, ent);
        } else {
          collideLeft(right, top, right, bottom, ent);
        }

        if (ent.y < cy) {
          collideDown(left, top, right, top, ent);
        } else {
          // collideUp(...);
        }
      } else {
        if (ent.y < cy) {
          collideDown(left, top, right, top, ent);
        } else {
          // collideUp(...);
        }

        if (ent.x < cx) {
          collideRight(left, top, left, bottom, ent);
        } else {
          collideLeft(right, top, right, bottom, ent);
        }
      }
    }

    void paint(Canvas canvas, int px, int py) {
      canvas.drawImage(image, px, py);
    }

    private void collideDown(float px0, float py0, float px1, float py1, Entity ent) {
      float left = ent.x - ent.radius, right = ent.x + ent.radius;
      if (between(left, right, px0, px1)) {
        if (ent.y + ent.radius > py0) {
          ent.y = py0 - ent.radius;
          ent.vy = -ent.vy * RESTITUTION;
          ent.ay = 0;
        }
      }
    }

    private void collideRight(float px0, float py0, float px1, float py1, Entity ent) {
      float top = ent.y - ent.radius, bottom = ent.y + ent.radius;
      if (between(top, bottom, py0, py1)) {
        if (ent.x + ent.radius > px0) {
          ent.x = px0 - ent.radius;
          ent.vx = -ent.vx * RESTITUTION;
          ent.ax = 0;
        }
      }
    }

    private void collideLeft(float px0, float py0, float px1, float py1, Entity ent) {
      float top = ent.y - ent.radius, bottom = ent.y + ent.radius;
      if (between(top, bottom, py0, py1)) {
        if (ent.x - ent.radius < px0) {
          ent.x = px0 + ent.radius;
          ent.vx = -ent.vx * RESTITUTION;
          ent.ax = 0;
        }
      }
    }

    private boolean between(float b0, float e0, float b1, float e1) {
      return (((b0 > b1) && (b0 < e1)) || ((e0 > b1) && (e0 < e1)));
    }
  }

  class EmptyTile extends Tile {
    @Override
    void paint(Canvas canvas, int px, int py) {
    }

    void collide(int tx, int ty, Entity ent) {
    }
  }

  private Tile[] tiles = new Tile[] {
    new EmptyTile(),
    new Tile("Block-Normal"),
    new Tile("Block-Gel"),
    new Tile("Block-Spring"),
    new Tile("Block-LeftRamp"),
    new Tile("Block-RightRamp"),
  };

  private final GroupLayer parentLayer;
  private final CanvasLayer mapLayer;

  private int[] map = new int[MAP_WIDTH * MAP_HEIGHT];
  private List<Pea> peas = new LinkedList<Pea>();
  private boolean mapChanged = true;

  private AssetWatcher watcher = new AssetWatcher(new AssetWatcher.Listener() {
    @Override
    public void error(Throwable e) {
      // Ignore errors for now.
    }

    @Override
    public void done() {
      mapChanged = true;
    }
  });

  public PeaWorld(GroupLayer parentLayer) {
    this.parentLayer = parentLayer;
    this.mapLayer = graphics().createCanvasLayer(graphics().screenWidth(), graphics().screenHeight());
    parentLayer.add(this.mapLayer);
    emptyWorld();

    // Wait for all tile images to load.
    for (Tile tile : tiles) {
      if (tile.image != null) {
        watcher.add(tile.image);
      }
    }
    watcher.start();
  }

  public Pea addPea(int tx, int ty) {
    Pea p = new Pea(parentLayer);
    p.x = tx * TILE_SIZE_X + (TILE_SIZE_X / 2);
    p.y = ty * TILE_SIZE_Y + (TILE_SIZE_Y / 2);
    peas.add(p);
    return p;
  }

  public void removePea(Pea p) {
    peas.remove(p);
  }

  public void paint() {
    // Only do this when the map changes. Now that we're using layers, we can actually optimize quite a
    // bit by repainting only the parts of the map that have changed.
    if (mapChanged) {
      mapChanged = false;

      Canvas canvas = mapLayer.canvas();

      int px = 0, py = (MAP_HEIGHT - 1) * TILE_SIZE_Y;
      for (int ty = MAP_HEIGHT - 1; ty >= 0; --ty) {
        for (int tx = 0; tx < MAP_WIDTH; ++tx) {
          int tile = tileIndex(tx, ty);
          if (tile >= 0) {
            tiles[tile].paint(canvas, px, py);
          }
          px += TILE_SIZE_X;
        }
        px = 0;
        py -= TILE_SIZE_Y;
      }
    }
  }

  public void setTileIndex(int x, int y, int tile) {
    assert (x >= 0) && (y >= 0) && (x < MAP_WIDTH) && (y < MAP_HEIGHT);
    assert (tile >= 0) && (tile < tiles.length);

    map[y * MAP_WIDTH + x] = tile;
  }

  public int tileIndex(int x, int y) {
    // Treat all tiles outside the world as empty.
    if ((x < 0) || (y < 0) || (x >= MAP_WIDTH) || (y >= MAP_HEIGHT)) {
      return 0;
    }

    return map[y * MAP_WIDTH + x];
  }

  public void update(float delta) {
    // Update all the peas. This will update their layers' positions as well.
    for (Pea p : peas) {
      if (p != null) {
        p.update(delta);
      }
    }
  }

  private Tile tile(int tx, int ty) {
    int idx = tileIndex(tx, ty);
    return tiles[idx];
  }

  private void emptyWorld() {
    for (int i = 0; i < MAP_WIDTH * MAP_HEIGHT; ++i) {
      map[i] = 0;
    }
  }
}
