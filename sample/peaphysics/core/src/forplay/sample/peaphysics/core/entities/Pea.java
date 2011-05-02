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

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import forplay.core.GroupLayer;

public class Pea extends DynamicPhysicsEntity {
  public static String TYPE = "Pea";

  public Pea(final GroupLayer worldLayer, World world) {
    super(worldLayer, world);
  }

  @Override
  Body initPhysicsBody(World world) {
    FixtureDef fixtureDef = new FixtureDef();
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DYNAMIC;
    bodyDef.position = new Vec2(0, 0);
    Body body = world.createBody(bodyDef);

    CircleShape circleShape = new CircleShape();
    circleShape.m_radius = getWidth() / 2f;
    fixtureDef.shape = circleShape;
    fixtureDef.density = 0.5f;
    fixtureDef.friction = 0.1f;
    fixtureDef.restitution = 0.4f;
    circleShape.m_p.set(0, 0);
    body.createFixture(fixtureDef);
    return body;
  }

  @Override
  float getWidth() {
    return 1.0f;
  }

  @Override
  float getHeight() {
    return 1.0f;
  }

  @Override
  public String getImagePath() {
    return "images/pea.png";
  }
}
