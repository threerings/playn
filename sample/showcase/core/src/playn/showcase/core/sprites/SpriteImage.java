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
package playn.showcase.core.sprites;

import playn.core.Image;

/**
 * Represents the data associated with a single sprite.
 */
class SpriteImage {
  private final Image image;
  private final int x;
  private final int y;
  private final int width;
  private final int height;

  public SpriteImage(final Image image, int x, int y, int width, int height) {
    this.image = image;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public Image image() {
    return image;
  }

  public int height() {
    return height;
  }

  public int width() {
    return width;
  }

  public int x() {
    return x;
  }

  public int y() {
    return y;
  }
}
