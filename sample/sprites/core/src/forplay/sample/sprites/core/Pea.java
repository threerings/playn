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
package forplay.sample.sprites.core;

import static forplay.core.ForPlay.log;

import forplay.core.GroupLayer;
import forplay.core.ResourceCallback;
import forplay.sample.sprites.core.sprite.Sprite;
import forplay.sample.sprites.core.sprite.SpriteLoader;

public class Pea {
  public static String IMAGE = "images/peasprites.png";
  public static String JSON = "sprites/peasprite.json";
  public static String JSON_WITH_IMAGE = "sprites/peasprite2.json";
  private Sprite sprite;
  private int spriteIndex = 0;
  private float angle;
  private boolean hasLoaded = false; // set to true when resources have loaded and we can update

  public Pea(final GroupLayer peaLayer, final float x, final float y) {
    // Sprite method #1: use a sprite image and json data describing the sprites
    sprite = SpriteLoader.getSprite(IMAGE, JSON);

    // Sprite method #2: use json data describing the sprites and containing the image urls
    // sprite = SpriteLoader.getSprite(JSON_WITH_IMAGE);

    // Add a callback for when the image loads.
    // This is necessary because we can't use the width/height (to center the
    // image) until after the image has been loaded
    sprite.addCallback(new ResourceCallback<Sprite>() {
      @Override
      public void done(Sprite sprite) {
        sprite.setSprite(spriteIndex);
        sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
        sprite.layer().setTranslation(x, y);
        peaLayer.add(sprite.layer());
        hasLoaded = true;
      }

      @Override
      public void error(Throwable err) {
        log().error("Error loading image!", err);
      }
    });
  }

  public void update(float delta) {
    if (hasLoaded) {
      if (Math.random() > 0.95) {
        spriteIndex = (spriteIndex + 1) % sprite.numSprites();
        sprite.setSprite(spriteIndex);
      }
      angle += delta;
      sprite.layer().setRotation(angle);
    }
  }
}
