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
package forplay.sample.hello.core;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import static forplay.core.ForPlay.*;

import forplay.core.ResourceCallback;
import forplay.core.GroupLayer;
import forplay.core.Image;
import forplay.core.ImageLayer;

public class Pea {
  private static float radius = 0.5f;

  private ImageLayer layer;
  private Body body;

  private float setPosX, setPosY, setVelX, setVelY;
  private float prevX, prevY, prevA;

  public Pea(final GroupLayer worldLayer, World world) {
    Image image = assetManager().getImage("images/pea.png");
    layer = graphics().createImageLayer(image);
    image.addCallback(new ResourceCallback<Image>() {
      @Override
      public void done(Image image) {
        // once the image is loaded, we can use its width and height
        layer.setOrigin(image.width() / 2f, image.height() / 2f);
        layer.setScale(radius * 2 / image.width(), radius * 2 / image.height());
        worldLayer.add(layer);
      }

      @Override
      public void error(Throwable err) {
      }
    });

    // create physics body
    FixtureDef fixtureDef = new FixtureDef();
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DYNAMIC;
    bodyDef.position = new Vec2(0, 0);
    body = world.createBody(bodyDef);

    CircleShape circleShape = new CircleShape();
    circleShape.m_radius = radius;
    fixtureDef.shape = circleShape;
    fixtureDef.density = 25;
    fixtureDef.friction = 0.1f;
    fixtureDef.restitution = 0.9f;
    circleShape.m_p.set(0, 0);
    body.createFixture(fixtureDef);
  }

  public void initLayer() {

  }

  public void paint(float alpha) {
    // interpolate based on previous state
    float x = (body.getPosition().x * alpha) + (prevX * (1f - alpha));
    float y = (body.getPosition().y * alpha) + (prevY * (1f - alpha));
    float a = (body.getAngle() * alpha) + (prevA * (1f - alpha));
    layer.setTranslation(x, y);
    layer.setRotation(a);
  }

  public void setPos(float x, float y) {
    setPosX = x;
    setPosY = y;
  }

  public void setVel(float x, float y) {
    setVelX = x;
    setVelY = y;
  }

  public void update(float delta) {
    // update if a set_() method was called
    if (setPosX != 0f || setPosY != 0f || setVelX != 0f || setVelY != 0f) {
      body.setTransform(new Vec2(setPosX, setPosY), body.getAngle());
      body.setLinearVelocity(new Vec2(setVelX, setVelY));
      setPosX = setPosY = setVelX = setVelY = 0;
    }
    // store state for interpolation in paint()
    prevX = body.getPosition().x;
    prevY = body.getPosition().y;
    prevA = body.getAngle();
  }
}
