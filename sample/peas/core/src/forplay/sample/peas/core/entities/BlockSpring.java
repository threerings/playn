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
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;

import static forplay.core.ForPlay.graphics;

import forplay.core.ImageLayer;
import forplay.sample.peas.core.PeaWorld;

public class BlockSpring extends Entity implements PhysicsEntity {
  public static String TYPE = "BlockSpring";

  ImageLayer layerBase;
  MouseJoint mj;
  Body body;

  public BlockSpring(PeaWorld peaWorld, World world, float x, float y, float angle) {
    super(peaWorld, x, y, angle);
    
    // add the spring joint
    MouseJointDef mjd = new MouseJointDef();
    mjd.bodyA = world.getBodyList();
    body = initPhysicsBody(world, x, y, angle);
    mjd.bodyB = getBody();
    mjd.target.set(new Vec2(x, y));
    mjd.maxForce = 40f * getBody().getMass();
    mjd.dampingRatio = 0.2f;
    mj = (MouseJoint) world.createJoint(mjd);
    
    setPos(x, y);
    setAngle(angle);
  }

  @Override
  public void initPreLoad(final PeaWorld peaWorld) {
  }

  @Override
  public void initPostLoad(final PeaWorld peaWorld) {
    layerBase = graphics().createImageLayer(layer.image());
    
    // set our layer base settings/source
    layerBase.setWidth(layer.image().width());
    layerBase.setHeight(30);
    layerBase.setSourceRect(0, 31, 71, 30);
    layerBase.setOrigin(image.width() / 2f, -30 + image.height() / 2f);
    layerBase.setScale(getWidth() / image.width(), getHeight() / image.height());

    // set our top layer settings/source
    layer.setWidth(layer.image().width());
    layer.setHeight(31);
    layer.setSourceRect(0, 0, 71, 31);
    layer.setOrigin(image.width() / 2f, image.height() / 2f);
    layer.setScale(getWidth() / image.width(), getHeight() / image.height());
    
    peaWorld.dynamicLayer.add(layerBase);
    peaWorld.dynamicLayer.add(layer);
    
    setPos(x, y);
    setAngle(angle);
  }

  Body initPhysicsBody(World world, float x, float y, float angle) {
    FixtureDef fixtureDef = new FixtureDef();
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DYNAMIC;
    bodyDef.position = new Vec2(0, 0);
    Body body = world.createBody(bodyDef);

    PolygonShape polygonShape = new PolygonShape();
    Vec2[] polygon = new Vec2[4];
    polygon[0] = new Vec2(-getWidth() / 2f, -getHeight() / 2f + getTopOffset());
    polygon[1] = new Vec2(getWidth() / 2f, -getHeight() / 2f + getTopOffset());
    polygon[2] = new Vec2(getWidth() / 2f, polygon[0].y + getSpringBoxHeight());
    polygon[3] = new Vec2(-getWidth() / 2f, polygon[1].y + getSpringBoxHeight());
    polygonShape.set(polygon, polygon.length);
    fixtureDef.shape = polygonShape;
    fixtureDef.friction = 0.1f;
    fixtureDef.restitution = 1.4f;
    body.createFixture(fixtureDef);
    body.setTransform(new Vec2(x, y), angle);
    return body;
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
   * Return the height of the spring's top box.
   */
  public float getSpringBoxHeight() {
    return getHeight() / 2.7f;
  }

  /**
   * Return the size of the offset where the block is slightly lower than where
   * the image is drawn for a depth effect
   */
  public float getTopOffset() {
    return 2.0f / 8f;
  }

  @Override
  public void setPos(float x, float y) {
    if (getBody() != null && layerBase != null) {
      getBody().setTransform(new Vec2(x, y), getBody().getAngle());
      mj.setTarget(new Vec2(x, y - 0 * getSpringBoxHeight()));
      layerBase.setTranslation(x, y);
      layer.setTranslation(x, y);
    }
  }

  @Override
  public void setAngle(float a) {
    if (getBody() != null && layerBase != null) {
      getBody().setTransform(getBody().getPosition(), a);
      layerBase.setRotation(a);
      layer.setRotation(a);
    }
  }

  @Override
  public void update(float alpha) {
    layer.setTranslation(body.getPosition().x, body.getPosition().y);
    layer.setRotation(body.getAngle());
  }

  @Override
  public String getImagePath() {
    return "images/Block-Spring.png";
  }

  @Override
  public Body getBody() {
    return body;
  }
}
