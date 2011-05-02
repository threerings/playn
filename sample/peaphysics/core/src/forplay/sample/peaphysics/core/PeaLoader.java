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
package forplay.sample.peaphysics.core;

import forplay.sample.peaphysics.core.entities.BlockGel;
import forplay.sample.peaphysics.core.entities.BlockLeftRamp;
import forplay.sample.peaphysics.core.entities.BlockRightRamp;
import forplay.sample.peaphysics.core.entities.Block;
import forplay.sample.peaphysics.core.entities.Entity;
import forplay.sample.peaphysics.core.entities.Pea;

import forplay.core.AssetWatcher;
import forplay.core.ForPlay;
import forplay.core.GroupLayer;
import forplay.core.Json;
import forplay.core.ResourceCallback;

public class PeaLoader {

  public static void CreateWorld(String level, final GroupLayer worldLayer,
      final ResourceCallback<PeaWorld> callback) {
    final PeaWorld peaWorld = new PeaWorld(worldLayer);

    // load the level
    ForPlay.assetManager().getText(level, new ResourceCallback<String>() {
      @Override
      public void done(String resource) {
        // create an asset watcher that will call our callback when all assets
        // are loaded
        AssetWatcher assetWatcher = new AssetWatcher(new AssetWatcher.Listener() {
          @Override
          public void done() {
            callback.done(peaWorld);
          }

          @Override
          public void error(Throwable e) {
            callback.error(e);
          }
        });

        ForPlay.log().info("about to parse, resource is: " + resource);
        // parse the level
        Json.Object document = ForPlay.json().parse(resource);
        
        ForPlay.log().info("done parsing");

        // parse the entities, adding each asset to the asset watcher
        Json.Array jsonEntities = document.getArray("Entities");
        for (int i = 0; i < jsonEntities.length(); i++) {
          Json.Object jsonEntity = jsonEntities.getObject(i);
          String type = jsonEntity.getString("type");
          float x = (float) jsonEntity.getNumber("x");
          float y = (float) jsonEntity.getNumber("y");
          float a = (float) jsonEntity.getNumber("a");

          Entity entity = null;
          if (Pea.TYPE.equalsIgnoreCase(type)) {
            entity = new Pea(peaWorld.dynamicLayer, peaWorld.world);
          } else if (Block.TYPE.equalsIgnoreCase(type)) {
            entity = new Block(peaWorld.dynamicLayer, peaWorld.world);
          } else if (BlockRightRamp.TYPE.equalsIgnoreCase(type)) {
            entity = new BlockRightRamp(peaWorld.dynamicLayer, peaWorld.world);
          } else if (BlockLeftRamp.TYPE.equalsIgnoreCase(type)) {
            entity = new BlockLeftRamp(peaWorld.dynamicLayer, peaWorld.world);
          } else if (BlockGel.TYPE.equalsIgnoreCase(type)) {
            entity = new BlockGel(peaWorld.dynamicLayer, peaWorld.world);
          }

          if (entity != null) {
            assetWatcher.add(entity.getImage());
            entity.setPos(x, y);
            entity.setAngle(a);
            peaWorld.add(entity);
          }
        }

        // start the watcher (it will call the callback when everything is
        // loaded)
        assetWatcher.start();
      }

      @Override
      public void error(Throwable err) {
        callback.error(err);
      }
    });
  }

}
