/**
 * Copyright 2011 The PlayN Authors
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
package playn.showcase.core.peas;

import playn.core.AssetWatcher;
import playn.core.PlayN;
import playn.core.GroupLayer;
import playn.core.Json;
import playn.core.ResourceCallback;

import playn.showcase.core.peas.entities.Block;
import playn.showcase.core.peas.entities.BlockGel;
import playn.showcase.core.peas.entities.BlockLeftRamp;
import playn.showcase.core.peas.entities.BlockRightRamp;
import playn.showcase.core.peas.entities.BlockSpring;
import playn.showcase.core.peas.entities.Cloud1;
import playn.showcase.core.peas.entities.Cloud3;
import playn.showcase.core.peas.entities.Entity;
import playn.showcase.core.peas.entities.FakeBlock;
import playn.showcase.core.peas.entities.Pea;
import playn.showcase.core.peas.entities.Portal;

public class PeaLoader {

  public static void CreateWorld(String level, final GroupLayer worldLayer,
      final ResourceCallback<PeaWorld> callback) {
    final PeaWorld peaWorld = new PeaWorld(worldLayer);

    // load the level
    PlayN.assetManager().getText(level, new ResourceCallback<String>() {
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

        // parse the level
        Json.Object document = PlayN.json().parse(resource);
        
        // previous Portal (used for linking portals)
        Portal lastPortal = null;

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
            entity = new Pea(peaWorld, peaWorld.world, x, y, a);
          } else if (Block.TYPE.equalsIgnoreCase(type)) {
            entity = new Block(peaWorld, peaWorld.world, x, y, a);
          } else if (BlockRightRamp.TYPE.equalsIgnoreCase(type)) {
            entity = new BlockRightRamp(peaWorld, peaWorld.world, x, y, a);
          } else if (BlockLeftRamp.TYPE.equalsIgnoreCase(type)) {
            entity = new BlockLeftRamp(peaWorld, peaWorld.world, x, y, a);
          } else if (BlockGel.TYPE.equalsIgnoreCase(type)) {
            entity = new BlockGel(peaWorld, peaWorld.world, x, y, a);
          } else if (BlockSpring.TYPE.equalsIgnoreCase(type)) {
            entity = new BlockSpring(peaWorld, peaWorld.world, x, y, a);
          } else if (Cloud1.TYPE.equalsIgnoreCase(type)) {
            entity = new Cloud1(peaWorld);
          } else if (Cloud3.TYPE.equalsIgnoreCase(type)) {
            entity = new Cloud3(peaWorld);
          } else if (FakeBlock.TYPE.equalsIgnoreCase(type)) {
            entity = new FakeBlock(peaWorld, x, y, a);
          } else if (Portal.TYPE.equalsIgnoreCase(type)) {
            entity = new Portal(peaWorld, peaWorld.world, x, y, a);
            if (lastPortal == null) {
              lastPortal = (Portal) entity;
            } else {
              lastPortal.other = (Portal) entity;
              ((Portal) entity).other = lastPortal;
              lastPortal = null;
            }
          }

          if (entity != null) {
            assetWatcher.add(entity.getImage());
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
