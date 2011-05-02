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

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.List;

import static forplay.core.ForPlay.*;

import forplay.core.CanvasLayer;
import forplay.core.DebugDrawBox2D;
import forplay.core.GroupLayer;

public class PeaWorld {
  private GroupLayer worldLayer;

  // size of world
  private static int width = 16;
  private static int height = 12;

  // box2d object containing physics world
  private World world;

  private List<Pea> peas = new ArrayList<Pea>();

  private static boolean showDebugDraw = false;
  private DebugDrawBox2D debugDraw;

  public PeaWorld(GroupLayer mainLayer) {
    worldLayer = graphics().createGroupLayer();
    mainLayer.add(worldLayer);

    // create the physics world
    Vec2 gravity = new Vec2(0.0f, 10.0f);
    world = new World(gravity, true);
    world.setWarmStarting(true);
    world.setAutoClearForces(true);

    // create the ground
    Body ground = world.createBody(new BodyDef());
    PolygonShape groundShape = new PolygonShape();
    groundShape.setAsEdge(new Vec2(0, height), new Vec2(width, height));
    ground.createFixture(groundShape, 0.0f);

    // create the walls
    Body wallLeft = world.createBody(new BodyDef());
    PolygonShape wallLeftShape = new PolygonShape();
    wallLeftShape.setAsEdge(new Vec2(0, 0), new Vec2(0, height));
    wallLeft.createFixture(wallLeftShape, 0.0f);
    Body wallRight = world.createBody(new BodyDef());
    PolygonShape wallRightShape = new PolygonShape();
    wallRightShape.setAsEdge(new Vec2(width, 0), new Vec2(width, height));
    wallRight.createFixture(wallRightShape, 0.0f);

    if (showDebugDraw) {
      CanvasLayer canvasLayer =
          graphics().createCanvasLayer((int) (width / HelloGame.physUnitPerScreenUnit),
              (int) (height / HelloGame.physUnitPerScreenUnit));
      graphics().rootLayer().add(canvasLayer);
      debugDraw = new DebugDrawBox2D();
      debugDraw.setCanvas(canvasLayer);
      debugDraw.setFlipY(false);
      debugDraw.setStrokeAlpha(150);
      debugDraw.setFillAlpha(75);
      debugDraw.setStrokeWidth(2.0f);
      debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit | DebugDraw.e_aabbBit);
      debugDraw.setCamera(0, 0, 1f / HelloGame.physUnitPerScreenUnit);
      world.setDebugDraw(debugDraw);
    }
  }

  public void update(float delta) {
    for (Pea p : peas) {
      p.update(delta);
    }
    // the step delta is fixed so box2d isn't affected by framerate
    world.step(0.033f, 10, 10);
  }

  public void paint(float delta) {
    if (debugDraw != null) {
      debugDraw.getCanvas().canvas().clear();
      world.drawDebugData();
    }
    for (Pea p : peas) {
      p.paint(delta);
    }
  }

  public Pea addPea() {
    Pea pea = new Pea(worldLayer, world);
    peas.add(pea);
    return pea;
  }
}
