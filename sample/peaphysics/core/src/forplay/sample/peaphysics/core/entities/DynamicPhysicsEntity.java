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
package forplay.sample.peaphysics.core.entities;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import forplay.core.GroupLayer;

public abstract class DynamicPhysicsEntity extends PhysicsEntity {
  // for calculating interpolation
  private float prevX, prevY, prevA;
  
  public DynamicPhysicsEntity(GroupLayer worldLayer, World world) {
    super(worldLayer, world);
  }

  @Override
  public void paint(float alpha) {
    // interpolate based on previous state
    float x = (body.getPosition().x * alpha) + (prevX * (1f - alpha));
    float y = (body.getPosition().y * alpha) + (prevY * (1f - alpha));
    float a = (body.getAngle() * alpha) + (prevA * (1f - alpha));
    layer.setTranslation(x, y);
    layer.setRotation(a);
  }

  @Override
  public void update(float delta) {
    // store state for interpolation in paint()
    prevX = body.getPosition().x;
    prevY = body.getPosition().y;
    prevA = body.getAngle();
  }

  public void setLinearVelocity(float x, float y) {
    body.setLinearVelocity(new Vec2(x, y));
  }

  public void setAngularVelocity(float w) {
    body.setAngularVelocity(w);
  }
}
