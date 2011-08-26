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
package playn.sample.hello.core;

import static playn.core.PlayN.assetManager;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;

import java.util.ArrayList;
import java.util.List;

import playn.core.Canvas.LineCap;
import playn.core.CanvasLayer;
import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import playn.core.SurfaceLayer;

public class HelloGame implements Game {
  static Image peaImage;
  GroupLayer peaLayer;
  List<Pea> peas = new ArrayList<Pea>(0);
  SurfaceLayer surf;
  CanvasLayer canv;
  ImageLayer bgLayer;
  int width = graphics().width();
  int height = graphics().height();

  @Override
  public void init() {
    
    // create and add background image layer
    Image bgImage = assetManager().getImage("images/bg.png");
    bgLayer = graphics().createImageLayer(bgImage);
    graphics().rootLayer().add(bgLayer);
    
    // create a group layer to hold the peas
    peaLayer = graphics().createGroupLayer();
    surf = graphics().createSurfaceLayer(width, height);
    canv = graphics().createCanvasLayer(width, height);
    graphics().rootLayer().add(canv);
    graphics().rootLayer().add(surf);
    graphics().rootLayer().add(peaLayer);
    
    canv.canvas().setLineCap(LineCap.SQUARE);
    canv.canvas().setStrokeColor(0xff0000ff);  
    canv.canvas().setStrokeWidth(6f);
    
    surf.surface().setFillColor(0xFfffff00);
    

    // preload the pea image into the asset manager cache
    peaImage = assetManager().getImage(Pea.IMAGE);


      pointer().setListener(new Pointer.Adapter() {
        @Override
        public void onPointerDrag(Pointer.Event event) {
          Pea pea = new Pea(peaLayer, event.x(), event.y());
          peas.add(pea);
        }
      });

  }
  
  public void onKeyDown(int keycode) {
    if (keycode == 20) {
      Pea pea = new Pea(peaLayer, PlayN.random() * 50, PlayN.random() * 50);
      peas.add(pea);
    }
  }
  
  public void onKeyUp(int keycode) {
    Pea pea = new Pea(peaLayer, PlayN.random() * 50, PlayN.random() * 50);
    peas.add(pea);
  }
  
  @Override
  public void paint(float alpha) {
    // layers automatically paint themselves (and their children). The rootlayer
    // will paint itself, the background, and the pea group layer automatically
    // so no need to do anything here!
//    if (PlayN.random() < .01) surf.surface().clear();
//    surf.surface().drawImage(assetManager().getImage(Pea.IMAGE), PlayN.random() * width, PlayN.random() * height);
    surf.surface().drawLine(0, graphics().screenHeight(), 100, graphics().screenHeight(), 100);
    surf.surface().drawLine(PlayN.random() * width, PlayN.random() * height, PlayN.random() * width, PlayN.random() * height, 5f);
//    canv.canvas().drawLine(PlayN.random() * width, PlayN.random() * height, PlayN.random() * width, PlayN.random() * height);
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