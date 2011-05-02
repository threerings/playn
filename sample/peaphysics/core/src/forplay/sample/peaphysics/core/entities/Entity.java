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
package forplay.sample.peaphysics.core.entities;

import static forplay.core.ForPlay.assetManager;
import static forplay.core.ForPlay.graphics;

import forplay.core.ForPlay;
import forplay.core.GroupLayer;
import forplay.core.Image;
import forplay.core.ImageLayer;
import forplay.core.ResourceCallback;

public abstract class Entity {
  ImageLayer layer;
  Image image;

  public Entity(final GroupLayer worldLayer) {
    image = assetManager().getImage(getImagePath());
    layer = graphics().createImageLayer(image);
    image.addCallback(new ResourceCallback<Image>() {
      @Override
      public void done(Image image) {
        // since the image is loaded, we can use its width and height
        layer.clearWidth();
        layer.clearHeight();
        layer.setOrigin(image.width() / 2f, image.height() / 2f);
        layer.setScale(getWidth() / image.width(), getHeight() / image.height());
        worldLayer.add(layer);
      }

      @Override
      public void error(Throwable err) {
        ForPlay.log().error("Error loading image: " + err.getMessage());
      }
    });
  }

  public void paint(float alpha) {
  }

  public void update(float delta) {
  }

  public void setPos(float x, float y) {
    layer.setTranslation(x, y);
  }

  public void setAngle(float a) {
    layer.setRotation(a);
  }

  abstract float getWidth();

  abstract float getHeight();

  abstract String getImagePath();
  
  public Image getImage() {
    return image;
  }
}
