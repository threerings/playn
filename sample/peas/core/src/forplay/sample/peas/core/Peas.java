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

import static forplay.core.ForPlay.*;

import forplay.core.Game;
import forplay.core.Keyboard;
import forplay.core.Pointer;
import forplay.sample.peas.core.PeaWorld.Pea;

public class Peas implements Game, Keyboard.Listener, Pointer.Listener {

  private Sky sky;
  private PeaWorld world;

  @Override
  public void init() {
    keyboard().setListener(this);
    pointer().setListener(this);

    graphics().setSize(800, 600);

    sky = new Sky(graphics().rootLayer());
    world = new PeaWorld(graphics().rootLayer());
    testWorld();
  }

  private void testWorld() {
    int[][] tiles = new int[][] {
        new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        new int[] {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
        new int[] {1, 0, 0, 0, 2, 5, 0, 0, 0, 0, 1},
        new int[] {1, 0, 0, 0, 2, 2, 2, 3, 1, 0, 1},
        new int[] {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
        new int[] {1, 0, 0, 3, 0, 0, 0, 0, 1, 0, 1},
        new int[] {1, 0, 2, 1, 5, 0, 0, 2, 1, 0, 1},
        new int[] {1, 0, 1, 1, 1, 5, 0, 0, 0, 0, 1},
        new int[] {1, 0, 1, 1, 1, 0, 0, 0, 1, 2, 1},
        new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

    for (int y = 0; y < 11; ++y) {
      for (int x = 0; x < 11; ++x) {
        world.setTileIndex(x, y, tiles[y][x]);
      }
    }
  }

  @Override
  public void onPointerEnd(int x, int y) {
    makePea();
  }

  @Override
  public void onPointerMove(int x, int y) {
  }

  @Override
  public void onPointerStart(int x, int y) {
  }

  @Override
  public void onPointerDrag(int x, int y) {
  }

  @Override
  public void onPointerScroll(int velocity) {
  }

  @Override
  public void onKeyDown(int buttonCode) {
    if (buttonCode == 27) {
      makePea();
    }
  }

  @Override
  public void onKeyUp(int buttonCode) {
  }

  @Override
  public void update(float delta) {
    float deltaS = (float) (delta / 1000);
    sky.update(deltaS);
    world.update(deltaS);
  }

  @Override
  public void paint(float alpha) {
    // Only the world needs to be painted. Everything else is layer-based.
    world.paint();
  }

  private void makePea() {
    Pea p = world.addPea(0, 0);
    p.x = random() * 700; p.y = 0;
    p.vx = random() * 200 - 100; p.vy = 0;
    p.ax = p.ay = 0;
    p.update(0);
  }

  @Override
  public int updateRate() {
    return 33;
  }
}
