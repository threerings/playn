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

import static forplay.core.ForPlay.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import forplay.core.Asserts;
import forplay.core.ImageLayer;
import forplay.core.ResourceCallback;

/**
 * A Sprite is a collection of {@link SpriteImage}s.
 * <p>
 * Create a Sprite from an image and json data using
 * {@link SpriteLoader#getSprite(String imageUrl, String jsonUrl)}.
 * <p>
 * Create a Sprite from json data using {@link SpriteLoader#getSprite(String json)}.
 * <p>
 * To use, add {@link #layer()} to a {@link Layer} in your game. To change sprites, call
 * {@link #setSprite(int)}.
 */
public class Sprite {
  private ImageLayer layer;
  private List<SpriteImage> spriteImages;
  private HashMap<String, Integer> spriteIdMap;
  private ResourceCallback<Sprite> callback;
  private SpriteImage current;
  private int currentId = -1;
  private boolean imagesDone = false; // true when images have finished loading
  private boolean dataDone = false; // true when sprite data has finished loading

  /**
   * Do not call directly. Create using {@link SpriteLoader#getSprite(String, String)}
   */
  Sprite(ImageLayer imageLayer) {
    this.layer = imageLayer;
    spriteImages = new ArrayList<SpriteImage>(0);
    spriteIdMap = new HashMap<String, Integer>();
  }

  /**
   * Set callback that will be called when both the sprite data and sprite image have been loaded.
   */
  public void addCallback(ResourceCallback<Sprite> callback) {
    this.callback = callback;
    if (isReady()) {
      callback.done(this);
    }
  }

  /**
   * Return the sprite {@link ImageLayer}.
   */
  public ImageLayer layer() {
    return layer;
  }

  /**
   * Return the number of sprites.
   */
  public int numSprites() {
    return (spriteImages == null ? 0 : spriteImages.size());
  }

  /**
   * Return the height of the current sprite.
   */
  public float height() {
    if (current != null) {
      return current.height();
    } else {
      return 1;
    }
  }

  /**
   * Return true when both the sprite data and the sprite image have been loaded.
   * <p>
   * @see #addCallback(ResourceCallback)
   */
  public boolean isReady() {
    return imagesDone && dataDone;
  }

  /**
   * Set the current sprite via the index.
   * <p>
   * The index is an integer between 0 and the number of sprites (@see {@link #numSprites()})
   */
  public void setSprite(int index) {
    Asserts.checkElementIndex(index, spriteImages.size(), "Invalid sprite index");
    if (index != currentId) {
      current = spriteImages.get(index);
      currentId = index;
      updateLayer();
    }
  }

  /**
   * Set the current sprite via the sprite's id.
   */
  public void setSprite(String id) {
    setSprite(Asserts.checkNotNull(spriteIdMap.get(id), "Invalid sprite id"));
  }

  /**
   * Return the width of the current sprite.
   */
  public float width() {
    if (current != null) {
      return current.width();
    } else {
      return 1;
    }
  }

  /**
   * Add a {@link SpriteImage} to the sprites.
   */
  void addSpriteImage(String key, SpriteImage spriteImage) {
    spriteIdMap.put(key, spriteImages.size());
    spriteImages.add(spriteImage);
  }

  /**
   * Should be called when the sprite data and sprite image have been loaded. Will handle calling
   * the {@link ResourceCallback} of the {@link Sprite}.
   */
  void done() {
    if (callback != null) {
      callback.done(this);
    }
  }

  /**
   * Should be called when the sprite image(s) is done loading.
   */
  void doneLoadingImages() {
    imagesDone = true;
    if (isReady()) {
      done();
    }
  }

  /**
   * Should be called when the sprite data is done loading.
   */
  void doneLoadingData() {
    dataDone = true;
    if (isReady()) {
      done();
    }
  }

  /**
   * Should be called if an error occurs when loading the sprite image or data. Will handle calling
   * the {@link ResourceCallback} of the {@link Sprite}.
   */
  void error(Throwable err) {
    if (callback != null) {
      callback.error(err);
    } else {
      // don't let the error fall on deaf ears
      log().error("Error loading sprite", err);
    }
  }

  /**
   * Returns the {@link SpriteImage}s associated with this Sprite.
   */
  List<SpriteImage> spriteImages() {
    return spriteImages;
  }

  /**
   * Update the Sprite layer.
   */
  private void updateLayer() {
    if (current != null) {
      layer.setImage(current.image());
      layer.setWidth(current.width());
      layer.setHeight(current.height());
      layer.setSourceRect(current.x(), current.y(), current.width(), current.height());
    }
  }
}
