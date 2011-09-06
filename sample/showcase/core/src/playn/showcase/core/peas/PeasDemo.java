/**
 * Copyright 2011 The PlayN Authors
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
package playn.showcase.core.peas;

import static playn.core.PlayN.assetManager;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;

import playn.core.PlayN;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import playn.core.ResourceCallback;

import playn.showcase.core.Demo;
import playn.showcase.core.peas.entities.Pea;

public class PeasDemo extends Demo {

  // scale difference between screen space (pixels) and world space (physics).
  public static float physUnitPerScreenUnit = 1 / 26.666667f;

  ImageLayer bgLayer;

  // main layer that holds the world. note: this gets scaled to world space
  GroupLayer worldLayer;

  // main world
  PeaWorld world = null;
  boolean worldLoaded = false;

  @Override
  public String name() {
    return "Pea Physics";
  }

  @Override
  public void init() {
    // load and show our background image
    Image bgImage = assetManager().getImage("peas/images/bg.png");
    bgLayer = graphics().createImageLayer(bgImage);
    graphics().rootLayer().add(bgLayer);

    // create our world layer (scaled to "world space")
    worldLayer = graphics().createGroupLayer();
    worldLayer.setScale(1f / physUnitPerScreenUnit);
    graphics().rootLayer().add(worldLayer);

    PeaLoader.CreateWorld("peas/levels/level1.json", worldLayer, new ResourceCallback<PeaWorld>() {
      @Override
      public void done(PeaWorld resource) {
        world = resource;
        worldLoaded = true;
      }

      @Override
      public void error(Throwable err) {
        PlayN.log().error("Error loading pea world: " + err.getMessage());
      }
    });

    // hook up our pointer listener
    pointer().setListener(new Pointer.Adapter() {
      @Override
      public void onPointerStart(Pointer.Event event) {
        if (worldLoaded) {
          Pea pea = new Pea(world, world.world, physUnitPerScreenUnit * event.x(),
                            physUnitPerScreenUnit * event.y(), 0);
          world.add(pea);
        }
      }
    });
  }

  @Override
  public void shutdown() {
    bgLayer.destroy();
    bgLayer = null;
    worldLayer.destroy();
    worldLayer = null;
    world = null;
    worldLoaded = false;
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
}
