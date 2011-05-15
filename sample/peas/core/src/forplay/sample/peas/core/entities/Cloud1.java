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
package forplay.sample.peas.core.entities;

import forplay.sample.peas.core.PeaWorld;

public class Cloud1 extends Entity {
  public static String TYPE = "Cloud1";

  public Cloud1(PeaWorld peaWorld) {
    super(peaWorld, 0, 0, 0);
    y = (float) (Math.random() * getMaximumHeight());
    x = (float) (Math.random() * getMaximumWidth());
  }

  @Override
  public void update(float delta) {
    x += delta * getVelocity();
    layer.setTranslation(x, y);
    
    if (x > getWidth() + getMaximumWidth()) {
      x = -getWidth();
      y = (float) (Math.random() * getMaximumHeight());
    }
  }

  @Override
  float getWidth() {
    return 0.3f * 26.0f;
  }

  @Override
  float getHeight() {
    return 0.3f * 18.0f;
  }

  float getMaximumWidth() {
    return 24.0f;
  }

  float getMaximumHeight() {
    return 3.0f;
  }

  float getVelocity() {
    return 0.003f;
  }

  @Override
  public void setPos(float x, float y) {
    this.x = x;
    this.y = y;
    layer.setTranslation(x, y);
  }

  @Override
  String getImagePath() {
    return "images/Cloud1.png";
  }

  @Override
  public void initPreLoad(PeaWorld peaWorld) {
    peaWorld.dynamicLayer.add(layer);
  }

  @Override
  public void initPostLoad(PeaWorld peaWorld) {
  }
}
