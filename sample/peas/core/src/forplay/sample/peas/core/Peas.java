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
package forplay.sample.peas.core;

import static forplay.core.ForPlay.assetManager;
import static forplay.core.ForPlay.graphics;
import static forplay.core.ForPlay.pointer;

import forplay.core.ForPlay;
import forplay.core.Game;
import forplay.core.GroupLayer;
import forplay.core.Image;
import forplay.core.ImageLayer;
import forplay.core.Pointer;
import forplay.core.ResourceCallback;
import forplay.sample.peas.core.entities.Pea;

public class Peas implements Game, Pointer.Listener {
  
  // scale difference between screen space (pixels) and world space (physics).
  public static float physUnitPerScreenUnit = 1 / 26.666667f;

  // main layer that holds the world. note: this gets scaled to world space
  GroupLayer worldLayer;

  // main world
  PeaWorld world = null;
  boolean worldLoaded = false;

  @Override
  public void init() {
    // load and show our background image
    Image bgImage = assetManager().getImage("images/bg.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    graphics().rootLayer().add(bgLayer);

    // create our world layer (scaled to "world space")
    worldLayer = graphics().createGroupLayer();
    worldLayer.setScale(1f / physUnitPerScreenUnit);
    graphics().rootLayer().add(worldLayer);

    PeaLoader.CreateWorld("levels/level1.json", worldLayer, new ResourceCallback<PeaWorld>() {
      @Override
      public void done(PeaWorld resource) {
        world = resource;
        worldLoaded = true;
      }

      @Override
      public void error(Throwable err) {
        ForPlay.log().error("Error loading pea world: " + err.getMessage());
      }
    });

    // hook up our pointer listener
    pointer().setListener(this);
  }

  @Override
  public void onPointerStart(float x, float y) {
    if (worldLoaded) {
      Pea pea = new Pea(world, world.world, physUnitPerScreenUnit * x, physUnitPerScreenUnit * y, 0);
      world.add(pea);
    }
  }

  @Override
  public void paint(float alpha) {
    if (worldLoaded) {
      world.paint(alpha);
    }
  }

  @Override
  public void update(float delta) {
    if (worldLoaded) {
      world.update(delta);
    }
  }

  @Override
  public int updateRate() {
    return 25;
  }
  
  @Override
  public void onPointerDrag(float x, float y) {
  }

  @Override
  public void onPointerEnd(float x, float y) {
  }
}
