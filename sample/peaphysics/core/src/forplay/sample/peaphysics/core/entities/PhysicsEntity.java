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
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import forplay.core.GroupLayer;

public abstract class PhysicsEntity extends Entity {
  Body body;

  public PhysicsEntity(GroupLayer worldLayer, World world) {
    super(worldLayer);
    body = initPhysicsBody(world);
  }

  abstract Body initPhysicsBody(World world);

  @Override
  public void paint(float delta) {
    layer.setTranslation(body.getPosition().x, body.getPosition().y);
    layer.setRotation(body.getAngle());
  }

  @Override
  public void setPos(float x, float y) {
    body.setTransform(new Vec2(x, y), body.getAngle());
  }

  @Override
  public void setAngle(float a) {
    body.setTransform(body.getPosition(), a);
  }
}
