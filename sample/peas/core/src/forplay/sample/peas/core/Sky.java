/**
 * Copyright 2010 The ForPlay Authors
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
package forplay.sample.peas.core;

import static forplay.core.ForPlay.assetManager;
import static forplay.core.ForPlay.graphics;
import static forplay.core.ForPlay.random;
import forplay.core.GroupLayer;
import forplay.core.Image;
import forplay.core.ImageLayer;

public class Sky {

  class Cloud extends Entity {
    Cloud(GroupLayer parentLayer, Image image) {
      super(parentLayer, graphics().createImageLayer(image), 0);

      x = random() * graphics().screenWidth();
      y = random() * graphics().screenHeight() / 2;

      vx = random() * 100 + 10;
    }

    @Override
    public void update(float delta) {
      super.update(delta);

      int halfWidth = layer.image().width() / 2;
      if (x - halfWidth > graphics().screenWidth()) {
        x = -halfWidth;
        y = random() * graphics().screenHeight() / 2;
      }
    }
  }

  private static final String[] CLOUD_NAMES = new String[] {
    "Cloud1", "Cloud2", "Cloud3"
  };

  private ImageLayer bgLayer;
  private Entity[] clouds = new Entity[CLOUD_NAMES.length];

  public Sky(GroupLayer parentLayer) {
    GroupLayer layer = graphics().createGroupLayer();
    parentLayer.add(layer);

    Image bg = assetManager().getImage("images/Background.png");
    bgLayer = graphics().createImageLayer(bg);
    layer.add(bgLayer);

    for (int i = 0; i < clouds.length; ++i) {
      Image image = assetManager().getImage("images/" + CLOUD_NAMES[i] + ".png");
      clouds[i] = new Cloud(layer, image);
    }
  }

  public void update(float delta) {
    // Update all the clouds. This will update their layers' positions as well.
    for (int i = 0; i < clouds.length; ++i) {
      clouds[i].update(delta);
    }
  }
}
