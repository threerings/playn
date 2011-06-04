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
package forplay.sample.sprites.core;

import java.util.ArrayList;
import java.util.List;

import static forplay.core.ForPlay.*;

import forplay.core.Game;
import forplay.core.GroupLayer;
import forplay.core.Image;
import forplay.core.ImageLayer;
import forplay.core.Pointer;

public class SpritesGame implements Game, Pointer.Listener {
  GroupLayer peaLayer;
  List<Pea> peas = new ArrayList<Pea>(0);

  @Override
  public void init() {
    // create and add background image layer
    Image bgImage = assetManager().getImage("images/bg.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    graphics().rootLayer().add(bgLayer);

    // create a group layer to hold the peas
    peaLayer = graphics().createGroupLayer();
    graphics().rootLayer().add(peaLayer);

    // add a listener for pointer (mouse, touch) input
    pointer().setListener(this);
  }

  @Override
  public void onPointerDrag(float x, float y) {
  }

  @Override
  public void onPointerEnd(float x, float y) {
    Pea pea = new Pea(peaLayer, x, y);
    peas.add(pea);
  }

  @Override
  public void onPointerStart(float x, float y) {
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

  @Override
  public int updateRate() {
    return 25;
  }
}
