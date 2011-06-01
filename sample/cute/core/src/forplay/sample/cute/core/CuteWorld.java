/**
 * Copyright 2010 The ForPlay Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package forplay.sample.cute.core;

import static forplay.core.ForPlay.*;

import static java.lang.Math.max;

import forplay.core.Image;
import forplay.core.Json;
import forplay.core.Surface;

import java.util.ArrayList;
import java.util.List;

public class CuteWorld {

  static class Stack {
    int[] tiles;
    List<CuteObject> objects = new ArrayList<CuteObject>();

    int height() {
      return tiles.length;
    }
  }

  private static final String[] tileNames = new String[] { "block_brown",
      "block_dirt", "block_grass", "block_plain", "block_stone", "block_wall",
      "block_water", "block_wood",

      "ramp_north", "ramp_east", "ramp_south", "ramp_west",

      "roof_north", "roof_northeast", "roof_east", "roof_southeast",
      "roof_south", "roof_southwest", "roof_west", "roof_northwest", };

  private static final String[] shadowNames = new String[] { "shadow_east",
      "shadow_northeast", "shadow_north", "shadow_northwest", "shadow_west",
      "shadow_southwest", "shadow_south", "shadow_southeast",
      "shadow_side_west" };

  private static final int SHADOW_EAST = 0;
  private static final int SHADOW_NORTHEAST = 1;
  private static final int SHADOW_NORTH = 2;
  private static final int SHADOW_NORTHWEST = 3;
  private static final int SHADOW_WEST = 4;
  private static final int SHADOW_SOUTHWEST = 5;
  private static final int SHADOW_SOUTH = 6;
  private static final int SHADOW_SOUTHEAST = 7;
  private static final int SHADOW_SIDE_WEST = 8;

  private static final int TILE_WIDTH = 100;
  private static final int TILE_HEIGHT = 80;
  private static final int TILE_DEPTH = 40;
  private static final int TILE_BASE = 90;
  private static final int TILE_IMAGE_HEIGHT = 170;
  private static final int OBJECT_BASE = 30;

  private static final double GRAVITY = -10.0;
  private static final double RESTITUTION = 0.4;
  private static final double FRICTION = 10.0;

  private static final int MAX_STACK_HEIGHT = 8;
  private static final Stack EMPTY_STACK;

  static {
    EMPTY_STACK = new Stack();
    EMPTY_STACK.tiles = new int[0];
  }

  private final Image[] tiles = new Image[tileNames.length];
  private final Image[] shadows = new Image[shadowNames.length];

  private Stack[] world;
  private int worldWidth, worldHeight;
  private double viewOriginX, viewOriginY, viewOriginZ;
  private int updateCounter = -1;

  public CuteWorld(Json.Object data) {
    loadImages();
    initWorld(data);
  }

  public CuteWorld(int width, int height) {
    worldWidth = width;
    worldHeight = height;

    loadImages();

    this.world = new Stack[worldWidth * worldHeight];
    int i = 0;
    for (int ty = 0; ty < worldHeight; ++ty) {
      for (int tx = 0; tx < worldWidth; ++tx) {
        this.world[i] = new Stack();
        world[i].tiles = new int[0];
        ++i;
      }
    }
  }

  public void addObject(CuteObject o) {
    Stack stack = stackForObject(o);
    stack.objects.add(o);
    o.stack = stack;
  }

  public void addTile(int tx, int ty, int type) {
    Stack stack = stack(tx, ty);
    int len = stack.tiles.length;
    if (len == MAX_STACK_HEIGHT) {
      return;
    }

    int[] newTiles = new int[len + 1];
    System.arraycopy(stack.tiles, 0, newTiles, 0, len);
    stack.tiles = newTiles;
    stack.tiles[len] = type;
  }

  public void removeTopTile(int tx, int ty) {
    Stack stack = stack(tx, ty);
    int len = stack.tiles.length;
    if (len == 0) {
      return;
    }

    int[] newTiles = new int[len - 1];
    System.arraycopy(stack.tiles, 0, newTiles, 0, len - 1);
    stack.tiles = newTiles;
  }

  public void paint(Surface surf, float alpha) {
    int startX = (int) pixelToWorldX(surf, 0);
    int endX = (int) pixelToWorldX(surf, surf.width());
    if (startX < 0)
      startX = 0;
    if (endX < 0)
      endX = 0;
    if (startX >= worldWidth)
      startX = worldWidth - 1;
    if (endX >= worldWidth)
      endX = worldWidth - 1;

    int startY = (int) pixelToWorldY(surf, 0, 0);
    int endY = (int) pixelToWorldY(surf, surf.height(), MAX_STACK_HEIGHT);
    if (startY < 0)
      startY = 0;
    if (endY < 0)
      endY = 0;
    if (startY >= worldHeight)
      startY = worldHeight - 1;
    if (endY >= worldHeight)
      endY = worldHeight - 1;

    // Paint all the tiles from back to front.
    for (int tz = 0; tz < MAX_STACK_HEIGHT; ++tz) {
      for (int ty = startY; ty <= endY; ++ty) {
        for (int tx = startX; tx <= endX; ++tx) {
          Stack stack = world[ty * worldWidth + tx];

          if (tz < stack.height()) {
            // Draw the tile and its shadows.

            // Skip obviously hidden tiles.
            if ((tz < stack.height() - 1) && (height(tx, ty + 1) > tz)) {
              continue;
            }

            // Figure out where the tile goes. If it's out of screen bounds,
            // skip it (paintShadow() is relatively expensive).
            int px = worldToPixelX(surf, tx);
            int py = worldToPixelY(surf, ty, tz) - TILE_BASE;
            if ((px > surf.width()) || (py > surf.height())
                || (px + TILE_WIDTH < 0) || (py + TILE_IMAGE_HEIGHT < 0)) {
              continue;
            }

            surf.drawImage(tiles[stack.tiles[tz]], px, py);
            paintShadow(surf, tx, ty, px, py);
          } else if (tz >= stack.height()) {
            // Paint the objects in this stack.
            paintObjects(surf, stack, tz, alpha);
          }
        }
      }
    }
  }

  public void setViewOrigin(double x, double y, double z) {
    viewOriginX = x;
    viewOriginY = y;
    viewOriginZ = z;
  }

  public void updatePhysics(double delta) {
    for (int ty = 0; ty < worldHeight; ++ty) {
      for (int tx = 0; tx < worldWidth; ++tx) {
        updatePhysics(stack(tx, ty), delta);
      }
    }
    updatePhysics(EMPTY_STACK, delta);

    ++updateCounter;
  }

  public void write(Json.Writer w) {
    w.object();
    {
      w.key("width");
      w.value(worldWidth);
      w.key("height");
      w.value(worldHeight);

      w.key("stacks");
      w.array();
      for (int y = 0; y < worldHeight; ++y) {
        for (int x = 0; x < worldWidth; ++x) {
          Stack stack = stack(x, y);
          w.array();
          for (int z = 0; z < stack.height(); ++z) {
            w.value(stack.tiles[z]);
          }
          w.endArray();
        }
      }
      w.endArray();
    }
    w.endObject();
  }

  private int height(int tx, int ty) {
    return stack(tx, ty).height();
  }

  private String imageRes(String name) {
    return "images/" + name + ".png";
  }

  private void initWorld(Json.Object data) {
    worldWidth = data.getInt("width");
    worldHeight = data.getInt("height");

    this.world = new Stack[worldWidth * worldHeight];

    Json.Array stacksData = data.getArray("stacks");
    int i = 0;
    for (int ty = 0; ty < worldHeight; ++ty) {
      for (int tx = 0; tx < worldWidth; ++tx) {
        Json.Array stackData = stacksData.getArray(i);
        world[i] = new Stack();
        world[i].tiles = new int[stackData.length()];
        for (int tz = 0; tz < stackData.length(); ++tz) {
          world[i].tiles[tz] = stackData.getInt(tz);
        }
        ++i;
      }
    }

    viewOriginX = 0;
    viewOriginY = 2.5;
  }

  private void loadImages() {
    // Load tiles.
    for (int i = 0; i < tiles.length; ++i) {
      tiles[i] = assetManager().getImage(imageRes(tileNames[i]));
    }

    // Load shadows.
    for (int i = 0; i < shadows.length; ++i) {
      shadows[i] = assetManager().getImage(imageRes(shadowNames[i]));
    }
  }

  /**
   * Moves an object by the given vector, handling all collisions.
   */
  private void moveBy(CuteObject o, double dx, double dy, double dz) {
    // Walls - start by getting relative heights of neighbors
    int tx = (int) o.x, ty = (int) o.y;
    int hc = (int) o.z;
    int hn = height(tx, ty - 1);
    int hs = height(tx, ty + 1);
    int hw = height(tx - 1, ty);
    int he = height(tx + 1, ty);
    int hse = height(tx + 1, ty + 1);
    int hne = height(tx + 1, ty - 1);
    int hsw = height(tx - 1, ty + 1);
    int hnw = height(tx - 1, ty - 1);

    double left = o.x + dx - o.r, right = o.x + dx + o.r;
    double top = o.y + dy - o.r, bottom = o.y + dy + o.r;
    boolean pastLeft = left < tx, pastTop = top < ty;
    boolean pastRight = right > tx + 1, pastBottom = bottom > ty + 1;

    // Collisions: north, east, west, south.
    if (pastLeft) {
      if (hw > hc) {
        dx = tx + o.r - o.x;
        o.vx = -o.vx * RESTITUTION;
      }
    } else if (pastRight) {
      if (he > hc) {
        dx = tx + 1 - o.r - o.x;
        o.vx = -o.vx * RESTITUTION;
      }
    }

    if (pastTop) {
      if (hn > hc) {
        dy = ty + o.r - o.y;
        o.vy = -o.vy * RESTITUTION;
      }
    } else if (pastBottom) {
      if (hs > hc) {
        dy = ty + 1 - o.r - o.y;
        o.vy = -o.vy * RESTITUTION;
      }
    }

    // Collisions: nw, ne, se, sw.
    if (pastLeft && pastTop) {
      if (hnw > hc) {
        if (tx - left > ty - top) {
          dy = ty - (o.y - o.r);
          o.vy = -o.vy * RESTITUTION;
        } else {
          dx = tx - (o.x - o.r);
          o.vx = -o.vx * RESTITUTION;
        }
      }
    }

    if (pastRight && pastTop) {
      if (hne > hc) {
        if (right - (tx + 1) > ty - top) {
          dy = ty - (o.y - o.r);
          o.vy = -o.vy * RESTITUTION;
        } else {
          dx = (tx + 1) - (o.r + o.x);
          o.vx = -o.vx * RESTITUTION;
        }
      }
    }

    if (pastRight && pastBottom) {
      if (hse > hc) {
        if (right - (tx + 1) > bottom - (ty + 1)) {
          dy = (ty + 1) - (o.r + o.y);
          o.vy = -o.vy * RESTITUTION;
        } else {
          dx = (tx + 1) - (o.r + o.x);
          o.vx = -o.vx * RESTITUTION;
        }
      }
    }

    if (pastLeft && pastBottom) {
      if (hsw > hc) {
        if (tx - left > bottom - (ty + 1)) {
          dy = (ty + 1) - (o.r + o.y);
          o.vy = -o.vy * RESTITUTION;
        } else {
          dx = tx - (o.x - o.r);
          o.vx = -o.vx * RESTITUTION;
        }
      }
    }

    // Update x/y position.
    o.x = o.x + dx;
    o.y = o.y + dy;

    // Clamp to world bounds.
    if (o.x < o.r) {
      o.x = o.r;
    }
    if (o.y < o.r) {
      o.y = o.r;
    }
    if (o.x > worldWidth - o.r) {
      o.x = worldWidth - o.r;
    }
    if (o.y > worldHeight - o.r) {
      o.y = worldHeight - o.r;
    }

    // Collisions: floors.
    left = o.x + dx - o.r;
    right = o.x + dx + o.r;
    top = o.y + dy - o.r;
    bottom = o.y + dy + o.r;
    pastLeft = left < tx - 0.01;
    pastTop = top < ty - 0.01;
    pastRight = right > tx + 1.01;
    pastBottom = bottom > ty + 1.01;

    double floor = height(tx, ty);
    if (pastLeft && hw - o.z < 0.5) {
      floor = max(floor, hw);
    }
    if (pastTop && hn - o.z < 0.5) {
      floor = max(floor, hn);
    }
    if (pastRight && he - o.z < 0.5) {
      floor = max(floor, he);
    }
    if (pastBottom && hs - o.z < 0.5) {
      floor = max(floor, hs);
    }

    if (o.z + dz < floor) {
      dz = floor - o.z;
      o.vz = -o.vz * RESTITUTION;

      if (o.vz < 0.01) {
        o.vz = 0;
      }
      o.resting = true;
    } else {
      o.resting = o.vz == 0;
    }

    o.z = o.z + dz;

    // Clamp to world bounds.
    if (o.z < 0) {
      o.z = 0;
    }
    if (o.z > MAX_STACK_HEIGHT - 0.01) {
      o.z = MAX_STACK_HEIGHT - 0.01;
    }
  }

  private void paintObjects(Surface surf, Stack stack, int tz, float alpha) {
    for (CuteObject o : stack.objects) {
      if ((int) o.z == tz) {
        int px = worldToPixelX(surf, o.x(alpha));
        int py = worldToPixelY(surf, o.y(alpha), o.z(alpha));
        int baseX = o.img.width() / 2;
        int baseY = o.img.height() - OBJECT_BASE;
        surf.drawImage(o.img, px - baseX, py - baseY);
      }
    }
  }

  private void paintShadow(Surface surf, int tx, int ty, int px, int py) {
    int hc = height(tx, ty);
    int hn = height(tx, ty - 1);
    int hs = height(tx, ty + 1);
    int hw = height(tx - 1, ty);
    int he = height(tx + 1, ty);
    int hse = height(tx + 1, ty + 1);
    int hne = height(tx + 1, ty - 1);
    int hsw = height(tx - 1, ty + 1);
    int hnw = height(tx - 1, ty - 1);

    if (hn > hc) {
      surf.drawImage(shadows[SHADOW_NORTH], px, py);
    }
    if (hs > hc) {
      surf.drawImage(shadows[SHADOW_SOUTH], px, py);
    }
    if (he > hc) {
      surf.drawImage(shadows[SHADOW_EAST], px, py);
    }
    if (hw > hc) {
      surf.drawImage(shadows[SHADOW_WEST], px, py);
    }

    if ((hse > hc) && (he <= hc)) {
      surf.drawImage(shadows[SHADOW_SOUTHEAST], px, py);
    }
    if ((hsw > hc) && (hw <= hc)) {
      surf.drawImage(shadows[SHADOW_SOUTHWEST], px, py);
    }
    if ((hne > hc) && (he <= hc) && (hn <= hc)) {
      surf.drawImage(shadows[SHADOW_NORTHEAST], px, py);
    }
    if ((hnw > hc) && (hw <= hc) && (hn <= hc)) {
      surf.drawImage(shadows[SHADOW_NORTHWEST], px, py);
    }

    // Special case: the side shadow has to be drawn potentially multiple
    // times, because it's rendered on the front face of the stack.
    while (hc > 0) {
      if ((hsw >= hc) && (hs < hc)) {
        surf.drawImage(shadows[SHADOW_SIDE_WEST], px, py);
      }
      py += TILE_DEPTH;
      if (hs >= hc) {
        break;
      }
      --hc;
    }
  }

  private double pixelToWorldX(Surface surf, int x) {
    double center = surf.width() * 0.5;
    return (int) (((viewOriginX * TILE_WIDTH) + x - center) / TILE_WIDTH);
  }

  private double pixelToWorldY(Surface surf, int y, double z) {
    double center = surf.height() * 0.5;
    return (y + (viewOriginY * TILE_HEIGHT - viewOriginZ * TILE_DEPTH)
        + (z * TILE_DEPTH) - center)
        / TILE_HEIGHT;
  }

  private Stack stack(int tx, int ty) {
    if ((tx < 0) || (tx >= worldWidth) || (ty < 0) || (ty >= worldHeight)) {
      return EMPTY_STACK;
    }

    return world[ty * worldWidth + tx];
  }

  private Stack stackForObject(CuteObject o) {
    if ((o.x < 0) || (o.y < 0) || (o.x >= worldWidth) || (o.y >= worldHeight)) {
      return EMPTY_STACK;
    }

    return stack((int) o.x, (int) o.y);
  }

  private void updatePhysics(CuteObject o, double delta) {
    // Avoid double-updates.
    if (o.lastUpdated == updateCounter) {
      return;
    }
    o.lastUpdated = updateCounter;
    o.saveOldPos();

    // Gravity & friction.
    if (o.z > (double) o.stack.height()) {
      o.az += delta * GRAVITY;
    }

    if (o.resting) {
      o.vx -= o.vx * FRICTION * delta;
      o.vy -= o.vy * FRICTION * delta;
      if (o.vz < 0) {
        o.vz = 0;
      }
    }

    // Update velocity
    o.vx += o.ax * delta;
    o.vy += o.ay * delta;
    o.vz += o.az * delta;

    // Update position and handle collisions.
    moveBy(o, o.vx, o.vy, o.vz);
  }

  private void updatePhysics(Stack stack, double delta) {
    for (int i = 0; i < stack.objects.size(); ++i) {
      // Run physics.
      CuteObject o = stack.objects.get(i);
      updatePhysics(o, delta);

      // Re-sort.
      Stack newStack = stackForObject(o);
      if (stack != newStack) {
        stack.objects.remove(i--);
        newStack.objects.add(o);
        o.stack = newStack;
      }
    }
  }

  private int worldToPixelX(Surface surf, double x) {
    double center = surf.width() * 0.5;
    return (int) (center - (viewOriginX * TILE_WIDTH) + x * TILE_WIDTH);
  }

  private int worldToPixelY(Surface surf, double y, double z) {
    double center = surf.height() * 0.5;
    return (int) (center
        - (viewOriginY * TILE_HEIGHT - viewOriginZ * TILE_DEPTH) + y
        * TILE_HEIGHT - z * TILE_DEPTH);
  }

  public double worldWidth() {
    return worldWidth;
  }

  public double worldHeight() {
    return worldHeight;
  }
}
