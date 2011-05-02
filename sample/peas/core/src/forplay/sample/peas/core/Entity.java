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

import forplay.core.GroupLayer;
import forplay.core.ImageLayer;

public class Entity {

  public int radius;
  public ImageLayer layer;

  public float x, y;
  public float vx, vy;
  public float ax, ay;

  public Entity(GroupLayer parentLayer, ImageLayer layer, int radius) {
    this.layer = layer;
    this.radius = radius;

    parentLayer.add(layer);
  }

  public void update(float delta) {
    vx += ax * delta;
    vy += ay * delta;

    x += vx * delta;
    y += vy * delta;

    layer.setTranslation(x - layer.image().width() / 2, y - layer.image().height() / 2);
  }
}
