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
package playn.showcase.core.sprites;

import java.util.ArrayList;
import java.util.List;

import static playn.core.PlayN.*;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;

import playn.showcase.core.Demo;

public class SpritesDemo extends Demo {
  GroupLayer layer;
  List<Pea> peas = new ArrayList<Pea>(0);

  @Override
  public String name() {
    return "Sprites";
  }

  @Override
  public void init() {
    // create a group layer to hold everything
    layer = graphics().createGroupLayer();
    graphics().rootLayer().add(layer);

    // create and add background image layer
    Image bgImage = assetManager().getImage("sprites/bg.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    layer.add(bgLayer);

    // add a listener for pointer (mouse, touch) input
    pointer().setListener(new Pointer.Adapter() {
      @Override
      public void onPointerEnd(Pointer.Event event) {
        Pea pea = new Pea(layer, event.x(), event.y());
        peas.add(pea);
      }
    });
  }

  @Override
  public void shutdown() {
    pointer().setListener(null);

    layer.destroy();
    layer = null;
  }

  @Override
  public void paint(float alpha) {
    // layers automatically paint themselves (and their children). The rootlayer
    // will paint itself, the background, and the pea group layer automatically
    // so no need to do anything here!
  }

  @Override
  public void update(float delta) {
    for (Pea pea : peas) {
      pea.update(delta);
    }
  }
}
