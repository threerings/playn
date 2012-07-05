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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

import playn.core.Keyboard;
import playn.core.Pointer;
import playn.core.Touch;

public class GameViewGL extends GLSurfaceView {

  private final GameLoop loop;
  private final AndroidPlatform platform;

  public GameViewGL(Context context, GameActivity activity) {
    super(context);
    this.platform = activity.platform();
    this.loop = new GameLoop(platform);

    getHolder().addCallback(this);
    setFocusable(true);
    setEGLContextClientVersion(2);
    if (activity.isHoneycombOrLater()) {
      // FIXME: Need to use android3.0 as a Maven artifact for this to work
      // setPreserveEGLContextOnPause(true);
    }
    setRenderer(new Renderer() {
      @Override
      public void onSurfaceCreated(GL10 gl, EGLConfig config) {} // nada
      @Override
      public void onSurfaceChanged(GL10 gl, int width, int height) {} // nada
      @Override
      public void onDrawFrame(GL10 gl) {
        loop.run(); // handles updating and painting
      }
    });
    setRenderMode(RENDERMODE_CONTINUOUSLY);
  }

  @Override
  public void onPause() {
    super.onPause();
    loop.pause();
  }

  @Override
  public void onResume() {
    super.onResume();
    loop.start();
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    super.surfaceCreated(holder);
    platform.log().debug("Surface created");
    platform.graphics().ctx.onSurfaceCreated();
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    super.surfaceChanged(holder, format, width, height);
    platform.log().debug("Surface changed " + width + "x" + height);
    platform.graphics().ctx.setSize(width, height);
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    super.surfaceDestroyed(holder);
    platform.log().debug("Surface destroyed");
    platform.graphics().ctx.onSurfaceLost();
  }

  void onKeyDown(final Keyboard.Event event) {
    queueEvent(new Runnable() {
      @Override
      public void run() {
        platform.keyboard().onKeyDown(event);
      }
    });
  }

  void onKeyTyped(final Keyboard.TypedEvent event) {
    queueEvent(new Runnable() {
      @Override
      public void run() {
        platform.keyboard().onKeyTyped(event);
      }
    });
  }

  void onKeyUp(final Keyboard.Event event) {
    queueEvent(new Runnable() {
      @Override
      public void run() {
        platform.keyboard().onKeyUp(event);
      }
    });
  }
}
