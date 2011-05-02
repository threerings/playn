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

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import forplay.core.GroupLayer;

public class BlockRightRamp extends Block {
  public static String TYPE = "BlockRightRamp";
  
  public BlockRightRamp(GroupLayer worldLayer, World world) {
    super(worldLayer, world);
  }

  @Override
  Body initPhysicsBody(World world) {
    FixtureDef fixtureDef = new FixtureDef();
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.STATIC;
    bodyDef.position = new Vec2(0, 0);
    Body body = world.createBody(bodyDef);

    PolygonShape polygonShape = new PolygonShape();
    Vec2[] polygon = new Vec2[3];
    polygon[0] = new Vec2(-getWidth()/2f, -getHeight()/2f + getTopOffset());
    polygon[1] = new Vec2(getWidth()/2f, getHeight()/2f);
    polygon[2] = new Vec2(-getWidth()/2f, getHeight()/2f);
    polygonShape.set(polygon, polygon.length);
    fixtureDef.shape = polygonShape;
    fixtureDef.friction = 0.1f;
    fixtureDef.restitution = 0.9f;
    body.createFixture(fixtureDef);
    return body;
  }

  @Override
  public String getImagePath() {
    return "images/Block-RightRamp.png";
  }
}
