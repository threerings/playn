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
package playn.showcase.core.snake;

import pythagoras.f.Transform;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import static playn.core.PlayN.*;

import playn.showcase.core.Demo;

/**
 * Animates a chain of images undulating around the screen, using depth and scale to make them
 * appear to move in and out in the z-dimension.
 */
public class SnakeDemo extends Demo
{
  private GroupLayer layer;
  private ImageLayer[] segments;
  private float dx = 10, dy = 5, dd = 1;

  @Override
  public String name() {
    return "Snake";
  }

  @Override
  public void init() {
    // create a group layer to hold everything
    layer = graphics().createGroupLayer();
    graphics().rootLayer().add(layer);

    // create and add background image layer
    Image bgImage = assetManager().getImage("sprites/bg.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    bgLayer.setDepth(-1);
    layer.add(bgLayer);

    // create our snake segments
    Image segImage = assetManager().getImage("sprites/pea.png");
    segments = new ImageLayer[25];
    for (int ii = 0; ii < segments.length; ii++) {
      segments[ii] = graphics().createImageLayer(segImage);
      segments[ii].setDepth(50);
      layer.add(segments[ii]);
    }
  }

  @Override
  public void shutdown() {
    segments = null;
    layer.destroy();
    layer = null;
  }

  @Override
  public void update(float delta) {
    // the tail segments play follow the leader
    for (int ii = segments.length-1; ii > 0; ii--) {
      ImageLayer cur = segments[ii], prev = segments[ii-1];
      Transform t1 = cur.transform(), t2 = prev.transform();
      t1.setTx(t2.tx());
      t1.setTy(t2.ty());
      t1.setUniformScale(t2.uniformScale());
      cur.setDepth(prev.depth());
    }

    // and the head segment leads the way
    ImageLayer first = segments[0];
    Transform t = first.transform();
    float nx = t.tx() + dx, ny = t.ty() + dy, nd = first.depth() + dd;
    if (nx < 0 || nx > graphics().width()) {
      dx *= -1;
      nx += dx;
    }
    if (ny < 0 || ny > graphics().height()) {
      dy *= -1;
      ny += dy;
    }
    if (nd < 25 || nd > 125) {
      dd *= -1;
      nd += dd;
    }
    t.setTx(nx);
    t.setTy(ny);
    t.setUniformScale(nd/50f);
    first.setDepth(nd);
  }
}
