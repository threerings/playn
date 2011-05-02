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
package forplay.sample.peaphysics.core;

import forplay.sample.peaphysics.core.entities.Pea;

import forplay.core.ForPlay;

import forplay.core.ResourceCallback;

import static forplay.core.ForPlay.*;

import forplay.core.Game;
import forplay.core.GroupLayer;
import forplay.core.Image;
import forplay.core.ImageLayer;
import forplay.core.Pointer;

public class PeaPhysicsGame implements Game, Pointer.Listener {
  // scale difference between screen space (pixels) and world space (physics).
  static float physUnitPerScreenUnit = 1 / 26.666667f;

  // main layer that holds the world
  // note: this gets scaled to world space
  GroupLayer worldLayer;

  // when initialized, this will become non-null
  PeaWorld world = null;
  
  float dragStartX, dragStartY;

  @Override
  public void init() {
    Image bgImage = assetManager().getImage("images/bg.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    graphics().rootLayer().add(bgLayer);

    pointer().setListener(this);

    worldLayer = graphics().createGroupLayer();
    worldLayer.setScale(1f / physUnitPerScreenUnit);
    graphics().rootLayer().add(worldLayer);

    PeaLoader.CreateWorld("levels/level1.json", worldLayer, new ResourceCallback<PeaWorld>() {
      @Override
      public void done(PeaWorld resource) {
        world = resource;
      }

      @Override
      public void error(Throwable err) {
        ForPlay.log().error("Error loading pea world: " + err.getMessage());
      }
    });
  }

  @Override
  public void onPointerDrag(int x, int y) {
  }

  @Override
  public void onPointerEnd(int x, int y) {
    if (world != null) {
      Pea pea = new Pea(world.dynamicLayer, world.world);
      pea.setPos(physUnitPerScreenUnit * x, physUnitPerScreenUnit * y);
      pea.setLinearVelocity(physUnitPerScreenUnit * (x - dragStartX), physUnitPerScreenUnit * (y - dragStartY));
      pea.update(0f);
      world.add(pea);
    }
  }

  @Override
  public void onPointerMove(int x, int y) {
  }

  @Override
  public void onPointerStart(int x, int y) {
    dragStartX = x;
    dragStartY = y;
  }

  @Override
  public void onPointerScroll(int velocity) {
  }

  @Override
  public void paint(float alpha) {
    if (world != null) {
      world.paint(alpha);
    }
  }

  @Override
  public void update(float delta) {
    if (world != null) {
      world.update(delta);
    }
  }

  @Override
  public int updateRate() {
    return 25;
  }
}
