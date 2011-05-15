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

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import forplay.sample.peas.core.PeaWorld;

public class Portal extends StaticPhysicsEntity implements PhysicsEntity.HasContactListener {
  public static String TYPE = "Portal";

  public Portal other = null;
  
  private static int maxHysteresis = 10;
  private static int hysteresis = 0;

  public Portal(PeaWorld peaWorld, World world, float x, float y, float angle) {
    super(peaWorld, world, x, y, angle);
  }

  @Override
  Body initPhysicsBody(World world, float x, float y, float angle) {
    FixtureDef fixtureDef = new FixtureDef();
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.STATIC;
    bodyDef.position = new Vec2(0, 0);
    Body body = world.createBody(bodyDef);

    // height of the portal contact box
    float boxHeight = getHeight() / 12f;
    float boxWidth = getWidth() * 0.75f;
    PolygonShape polygonShape = new PolygonShape();
    Vec2[] polygon = new Vec2[4];
    polygon[0] = new Vec2(-boxWidth/2f, getHeight()/2f - boxHeight);
    polygon[1] = new Vec2(boxWidth/2f, getHeight()/2f - boxHeight);
    polygon[2] = new Vec2(boxWidth/2f, getHeight()/2f);
    polygon[3] = new Vec2(-boxWidth/2f, getHeight()/2f);
    polygonShape.set(polygon, polygon.length);
    fixtureDef.shape = polygonShape;
    fixtureDef.friction = 0.1f;
    fixtureDef.restitution = 0.8f;
    body.createFixture(fixtureDef);
    body.setTransform(new Vec2(x, y), angle);
    return body;
  }

  @Override
  public void initPostLoad(final PeaWorld peaWorld) {
    layer.setRotation(0f); // total hack so we can portal horizontally but not rotate the image
    peaWorld.staticLayerFront.add(layer);
  }

  @Override
  float getWidth() {
    return 2.0f;
  }

  @Override
  float getHeight() {
    return 2.0f;
  }

  /**
   * Return the size of the offset where the block is slightly lower than where
   * the image is drawn for a depth effect
   */
  public float getTopOffset() {
    return 2.0f / 8f;
  }

  @Override
  public String getImagePath() {
    return "images/teleport.png";
  }

  @Override
  public void update(float delta) {
    super.update(delta);
    if (hysteresis > 0) {
      hysteresis--;
    }
  }

  // Handle portal event
  @Override
  public void contact(PhysicsEntity contactEntity) {
    // keep a counter to prevent another portal event until a timeout
    if (hysteresis > 0) {
      return; // do not perform another portal event until hysteresis frames have passed
    } else {
      hysteresis = maxHysteresis;
    }
    Vec2 pos = contactEntity.getBody().getPosition();
    float ang = contactEntity.getBody().getAngle();
    Vec2 vel = contactEntity.getBody().getLinearVelocity();
    
    Vec2 posDiff = pos.sub(getBody().getPosition());
    float angDiff = other.getBody().getAngle() - getBody().getAngle();
    
    Vec2 newPos = rotate(posDiff, angDiff).add(other.getBody().getPosition());
    float newAng = ang + angDiff;
    if (contactEntity instanceof DynamicPhysicsEntity) {
      DynamicPhysicsEntity dynamic = (DynamicPhysicsEntity) contactEntity;
      dynamic.setPos(newPos.x, newPos.y);
      dynamic.setAngle(newAng);
    } else {
      contactEntity.getBody().setTransform(newPos, newAng);
    }
    Vec2 newVel = rotate(vel, angDiff);
    contactEntity.getBody().setLinearVelocity(newVel);
  }

  private Vec2 rotate(Vec2 vec, float theta) {
    Vec2 ret = new Vec2();
    float cTheta = (float)Math.cos(theta);
    float sTheta = (float)Math.sin(theta);
    ret.x = vec.x * cTheta - vec.y * sTheta;
    ret.y = vec.x * sTheta + vec.y * cTheta;
    return ret;
  }
}
