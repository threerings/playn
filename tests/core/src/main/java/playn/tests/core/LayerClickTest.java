/**
 * Copyright 2012 The PlayN Authors
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
package playn.tests.core;

import pythagoras.f.FloatMath;
import pythagoras.f.Vector;
import react.RFuture;
import react.Slot;

import playn.core.*;
import playn.scene.*;

class LayerClickTest extends Test {

  public LayerClickTest (TestsGame game) {
    super(game);
  }

  @Override public String getName() {
    return "LayerClickTest";
  }

  @Override public String getDescription() {
    return "Tests the hit testing and click/touch processing provided for layers.";
  }

  @Override public void init() {
    Image orange = game.assets.getImage("images/orange.png");
    RFuture<Texture> ortex = game.graphics.createTextureAsync(orange);

    ImageLayer l1 = new ImageLayer(ortex);
    game.rootLayer.add(l1.setScale(2).setRotation(FloatMath.PI/8).setTranslation(50, 50));
    // if (touch().hasTouch()) {
    //   l1.addListener((Touch.LayerListener)new Mover(l1));
    // } else {
    //   l1.addListener((Pointer.Listener)new Mover(l1));
    // }

    ImageLayer l2 = new ImageLayer(ortex);
    game.rootLayer.add(l2.setScale(1.5f).setRotation(FloatMath.PI/4).setTranslation(150, 50));
    // if (touch().hasTouch()) {
    //   l2.addListener((Touch.LayerListener)new Mover(l2));
    // } else {
    //   l2.addListener((Pointer.Listener)new Mover(l2));
    // }

    Image mdb = game.assets.getRemoteImage("https://graph.facebook.com/samskivert/picture");
    final ImageLayer l3 = new ImageLayer(game.graphics.createTextureAsync(mdb));
    game.rootLayer.add(l3.setRotation(-FloatMath.PI/4).setTranslation(50, 150));
    // if (touch().hasTouch()) {
    //   l3.addListener((Touch.LayerListener)new Mover(l3));
    // } else {
    //   l3.addListener((Pointer.Listener)new Mover(l3));
    // }
  }

  // protected static class Mover implements Pointer.Listener, Touch.LayerListener {
  //   private final Layer layer;

  //   public Mover (Layer layer) {
  //     this.layer = layer;
  //   }

  //   public void onTouchStart(Touch.Event event) {
  //     onStart(event.x(), event.y());
  //   }
  //   public void onTouchMove(Touch.Event event) {
  //     onMove(event.x(), event.y());
  //   }
  //   public void onTouchEnd(Touch.Event event) {
  //     // nada
  //   }
  //   public void onTouchCancel(Touch.Event event) {
  //     // nada
  //   }

  //   public void onPointerStart(Pointer.Event event) {
  //     onStart(event.x(), event.y());
  //   }
  //   public void onPointerDrag(Pointer.Event event) {
  //     onMove(event.x(), event.y());
  //   }
  //   public void onPointerEnd(Pointer.Event event) {
  //     // nada
  //   }
  //   public void onPointerCancel(Pointer.Event event) {
  //     // nada
  //   }

  //   protected void onStart(float x, float y) {
  //     _lstart = layer.transform().translation();
  //     _pstart = new Vector(x, y);
  //   }
  //   protected void onMove(float x, float y) {
  //     Vector delta = new Vector(x, y).subtractLocal(_pstart);
  //     layer.setTranslation(_lstart.x + delta.x, _lstart.y + delta.y);
  //   }

  //   protected Vector _lstart, _pstart;
  // }
}
