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
package playn.android;

import static javax.media.opengl.GL.GL_COLOR_ATTACHMENT0;
import static javax.media.opengl.GL.GL_FRAMEBUFFER;
import static javax.media.opengl.GL.GL_RGBA;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_UNSIGNED_BYTE;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import playn.core.Asserts;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.ResourceCallback;
import playn.core.StockInternalTransform;
import playn.core.gl.GLUtil;
import android.graphics.Bitmap;

/**
 * Android implementation of CanvasImage class. Prioritizes the SoftReference to
 * the bitmap, and only holds a hard reference if the game has requested that a
 * Canvas be created.
 */
class AndroidImage implements CanvasImage {

	private SoftReference<Bitmap> bitmapRef;
	private AndroidCanvas canvas;
	private Bitmap canvasBitmap;
	private List<ResourceCallback<Image>> callbacks = new ArrayList<ResourceCallback<Image>>();
	private int width, height, tex, pow2tex;
	private String path;

	public AndroidImage(String path, Bitmap bitmap) {
		this.path = path;
		bitmapRef = new SoftReference<Bitmap>(bitmap);
		width = bitmap.getWidth();
		height = bitmap.getHeight();
	}

	public AndroidImage(int w, int h, boolean alpha) {
		Bitmap newBitmap = Bitmap.createBitmap(w, h,
				alpha ? AndroidPlatform.instance.preferredBitmapConfig
						: Bitmap.Config.RGB_565);
		bitmapRef = new SoftReference<Bitmap>(newBitmap);
		width = w;
		height = h;
	}

	public void addCallback(ResourceCallback<Image> callback) {
		callbacks.add(callback);
		if (isReady()) {
			runCallbacks(true);
		}
	}

	public Canvas canvas() {
		if (canvas == null) {
			canvasBitmap = getBitmap();
			canvas = new AndroidCanvas(
					new android.graphics.Canvas(canvasBitmap));
		}
		bitmapRef = null;
		return canvas;
	}

	public int height() {
		return height;
	}

	public boolean isReady() {
		return bitmapRef != null || canvas != null;
	}

	public void replaceWith(Image image) {
		Asserts.checkArgument(image instanceof AndroidImage);
		bitmapRef = new SoftReference<Bitmap>(
				((AndroidImage) image).getBitmap());
		canvas = null;
	}

	public int width() {
		return width;
	}

	public Bitmap getBitmap() {
		if (canvasBitmap != null) {
			return canvasBitmap;
		}
		if (bitmapRef != null) {
			Bitmap bm = bitmapRef.get();
			if (bm == null && path != null) {
				// Log.i("playn", "Bitmap " + path + " fell out of memory");
				bitmapRef = new SoftReference<Bitmap>(
						bm = AndroidPlatform.instance.assetManager()
								.doGetBitmap(path));
			}
			return bm;
		}
		return null;
	}

	private void runCallbacks(boolean success) {
		for (ResourceCallback<Image> cb : callbacks) {
			if (success) {
				cb.done(this);
			} else {
				cb.error(new Exception("Error loading image"));
			}
		}
		callbacks.clear();
	}
	
  /*
   * Clears textures associated with this image. This does not destroy the image -- a subsequent
   * call to ensureTexture() will recreate them.
   */
	//TODO(jonagill): Should 0 be used as a null texture, or would -1 be better?
  void clearTexture(AndroidGraphics gfx) {
    if (pow2tex == tex) {
      pow2tex = 0;
    }

    if (tex != 0) {
      gfx.destroyTexture(tex);
      tex = 0;
    }
    if (pow2tex != 0) {
      gfx.destroyTexture(tex);
      pow2tex = 0;
    }
  }
  
  int ensureTexture(AndroidGraphics gfx, boolean repeatX, boolean repeatY) {
    // Create requested textures if loaded.
    if (isReady()) {
      if (repeatX || repeatY) {
        scaleTexture(gfx, repeatX, repeatY);
        return pow2tex;
      } else {
        loadTexture(gfx);
        return tex;
      }
    }
    
    return 0;
  }
  
  private void loadTexture(AndroidGraphics gfx) {
    if (tex != 0) {
      return;
    }

    tex = gfx.createTexture(false, false);
    gfx.updateTexture(tex, getBitmap());
  }

  private void scaleTexture(AndroidGraphics gfx, boolean repeatX, boolean repeatY) {
//    if (pow2tex != 0) {
//      return;
//    }
//
//    // Ensure that 'tex' is loaded. We use it below.
//    loadTexture(gfx);
//
//    // GL requires pow2 on axes that repeat.
//    int width = GLUtil.nextPowerOfTwo(width()), height = GLUtil.nextPowerOfTwo(height());
//
//    // Don't scale if it's already a power of two.
//    if ((width == 0) && (height == 0)) {
//      pow2tex = tex;
//      return;
//    }
//
//    // width/height == 0 => already a power of two.
//    if (width == 0) {
//      width = width();
//    }
//    if (height == 0) {
//      height = height();
//    }
//
//    // Create the pow2 texture.
//    pow2tex = gfx.createTexture(repeatX, repeatY);
//    AndroidGL20 gl20 = gfx.gl20;
//    gl20.glBindTexture(GL_TEXTURE_2D, pow2tex);
//    gl20.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);
//
//    // Point a new framebuffer at it.
//    int[] fbufBuffer = new int[1];
//    gl20.glGenFramebuffers(1, fbufBuffer, 0);
//    int fbuf = fbufBuffer[0];
//    gfx.bindFramebuffer(fbuf, width, height);
//    gl20.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, pow2tex, 0);
//    //TODO(jonagill): No mip-mapping?
//
//    // Render the scaled texture into the framebuffer.
//    // (rebind the texture because gfx.bindFramebuffer() may have bound it when flushing)
//    gl20.glBindTexture(GL_TEXTURE_2D, pow2tex);
//    gfx.drawTexture(tex, width(), height(), StockInternalTransform.IDENTITY, 0, height, width, -height, false,
//        false, 1);
//    gfx.flush();
//    gfx.bindFramebuffer();
//
//    gl20.glDeleteFramebuffers(1, new int[] {fbuf}, 0);
  }
  
  
}
