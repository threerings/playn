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
package forplay.sample.sprites.core.sprite;

import static forplay.core.ForPlay.assetManager;
import static forplay.core.ForPlay.graphics;
import static forplay.core.ForPlay.json;

import forplay.core.Asserts;
import forplay.core.AssetWatcher;
import forplay.core.Image;
import forplay.core.Json;
import forplay.core.ResourceCallback;

/**
 * Class for loading and parsing sprite sheets.
 * <p>
 * To use, call {@link #getSprite(String imageUrl, String jsonUrl)} with an image path and json
 * data, or {@link #getSprite(String jsonUrl)} with json data containing image urls.
 */
// TODO(pdr): the two getSprite() methods are messy, clean them up.
public class SpriteLoader {

  // prevent instantiation
  private SpriteLoader() {
  }

  /**
   * Return a {@link Sprite}, given a path to the image and a path to the json sprite description.
   * <p>
   * json data should be in the following format:
   * 
   * <pre>
   * {@code {
   *   "sprites": [
   *     {"id": "sprite_0", "x": 30, "y": 30, "w": 37, "h": 37},
   *     {"id": "sprite_1", "x": 67, "y": 30, "w": 37, "h": 37},
   *     {"id": "sprite_2", "x": 104, "y": 30, "w": 37, "h": 37},
   *     {"id": "sprite_3", "x": 141, "y": 30, "w": 37, "h": 37}
   *   ]}
   * }
   * </pre>
   */
  public static Sprite getSprite(String imagePath, String jsonPath) {
    Image image = assetManager().getImage(imagePath);
    final Image[] images = new Image[]{image};
    // temp image to prevent NPE if using the Sprite's Layer (Sprite.getLayer()) before the image
    // has loaded or before a sprite has been set (Sprite.setSprite()).
    final Image tempImage = graphics().createImage(1, 1);
    final Sprite sprite = new Sprite(graphics().createImageLayer(tempImage));

    // load and parse json
    assetManager().getText(jsonPath, new ResourceCallback<String>() {
      @Override
      public void done(String json) {
        try {
          parseJson(images, sprite, json);
        } catch (Throwable err) {
          sprite.error(err);
          return;
        }
        sprite.doneLoadingData();
      }

      @Override
      public void error(Throwable err) {
        sprite.error(err);
      }
    });

    // set callback for image
    image.addCallback(new ResourceCallback<Image>() {
      @Override
      public void done(Image resource) {
        sprite.doneLoadingImages();
      }

      @Override
      public void error(Throwable err) {
        sprite.error(err);
      }
    });

    return sprite;
  }

  /**
   * Return a {@link Sprite}, given a path to the json sprite description.
   * <p>
   * json data should be in the following format:
   * 
   * <pre>
   * {@code {
   *   "urls": ["images/peasprites2.png", "images/peasprites3.png"],
   *   "sprites": [
   *     {"id": "sprite_0", "url": 0, "x": 30, "y": 30, "w": 37, "h": 37},
   *     {"id": "sprite_1", "url": 0, "x": 67, "y": 30, "w": 37, "h": 37},
   *     {"id": "sprite_2", "url": 1, "x": 104, "y": 30, "w": 37, "h": 37},
   *     {"id": "sprite_3", "url": 1, "x": 141, "y": 30, "w": 37, "h": 37}
   *   ]}
   * }
   * </pre>
   */
  public static Sprite getSprite(String jsonPath) {
    // temp image to prevent NPE if using the Sprite's Layer (Sprite.getLayer()) before the image
    // has loaded or before a sprite has been set (Sprite.setSprite()).
    final Image tempImage = graphics().createImage(1, 1);
    final Sprite sprite = new Sprite(graphics().createImageLayer(tempImage));

    // create asset watcher for the image assets
    final AssetWatcher watcher = new AssetWatcher(new AssetWatcher.Listener() {
      @Override
      public void done() {
        sprite.doneLoadingImages();
      }

      @Override
      public void error(Throwable e) {
        sprite.error(e);
      }
    });

    // load and parse json, then add each image parsed from the json to the asset watcher to load
    assetManager().getText(jsonPath, new ResourceCallback<String>() {
      @Override
      public void done(String json) {
        try {
          parseJson(null, sprite, json);
          for (SpriteImage spriteImage : sprite.spriteImages()) {
            watcher.add(spriteImage.image());
          }
          watcher.start();
        } catch (Throwable err) {
          sprite.error(err);
          return;
        }
        sprite.doneLoadingData();
      }

      @Override
      public void error(Throwable err) {
        sprite.error(err);
      }
    });

    return sprite;
  }

  /**
   * Parse a json sprite sheet and add the sprite images to the sheet.
   * <p>
   * If images is null, the images urls are parsed from the json.
   * 
   * @param image Image to associate with each {@link SpriteImage}, or null to parse from the json
   * @param sheet Sprite to store the {@link SpritesImage}s
   * @param json json to parse
   */
  private static void parseJson(Image[] images, Sprite sprite, String json) {
    Json.Object document = json().parse(json);

    // parse image urls, if necessary
    if (images == null || images.length == 0) {
      Json.Array urls = document.getArray("urls");
      Asserts.checkNotNull(urls, "No urls provided for sprite images");
      images = new Image[urls.length()];
      for (int i = 0; i < urls.length(); i++) {
        images[i] = assetManager().getImage(urls.getString(i));
      }
    }

    // parse the sprite images
    Json.Array spriteImages = document.getArray("sprites");
    for (int i = 0; i < spriteImages.length(); i++) {
      Json.Object jsonSpriteImage = spriteImages.getObject(i);
      String id = jsonSpriteImage.getString("id");
      int imageId = jsonSpriteImage.getInt("url"); // will return 0 if not specified
      Asserts.checkElementIndex(imageId, images.length, "URL must be an index into the URLs array");
      int x = jsonSpriteImage.getInt("x");
      int y = jsonSpriteImage.getInt("y");
      int width = jsonSpriteImage.getInt("w");
      int height = jsonSpriteImage.getInt("h");
      SpriteImage spriteImage = new SpriteImage(images[imageId], x, y, width, height);
      sprite.addSpriteImage(id, spriteImage);
    }
  }
}
