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

import java.util.concurrent.atomic.AtomicBoolean;

import javax.media.opengl.GL2ES2;

import android.util.Log;

public class GameLoop implements Runnable {
  private static final int MAX_DELTA = 100;

  private AtomicBoolean running = new AtomicBoolean();
  private AndroidGraphics gfx;
  
  private long timeOffset = System.currentTimeMillis();

  private int updateRate;
  private int accum;
  private int lastTime;  
  private int lastStats;
  private int updateCount;
  private int updateTime;
  private int paintCount;
  private int paintQueueStart;
  private int runQueueStart;
  private int paintTime, paintLagTime;
  private int runCount;
  private int runQueueTime;

  private float paintAlpha;

  public GameLoop() {
    gfx = AndroidPlatform.instance.graphics();
  }

  public void start() {
    lastStats = time();

    if (!running.get()) {
      Log.i("playn", "Starting game loop");
      this.updateRate = AndroidPlatform.instance.game.updateRate();
      running.set(true);
      runQueueStart = lastTime = time();
    }
  }

  public void pause() {
    Log.i("playn", "Pausing game loop");
    running.set(false);
  }

  public void run() {
    // The thread can be stopped between runs.
    if (!running.get())
      return;
    
    runCount++;

    int now = time();
    float delta = now - lastTime;
    if (delta > MAX_DELTA)
      delta = MAX_DELTA;

    lastTime = now;
    runQueueTime += now - runQueueStart;

    if (now - lastStats > 10000) {
      updateStats(now);
    }

    if (updateRate == 0) {
      AndroidPlatform.instance.update(delta);
      accum = 0;
    } else {
      accum += delta;
      while (accum >= updateRate) {
        updateCount++;
        double start = time();
        AndroidPlatform.instance.update(updateRate);
        updateTime += time() - start;
        accum -= updateRate;
      }
    }

    paintAlpha = (updateRate == 0) ? 0 : accum / updateRate;
    paintQueueStart = time();
    paint();
  }

  private void updateStats(int now) {
    if (paintCount > 0)
      Log.i("playn", "Stats: paints = " + paintCount + " " + ((float) paintTime / paintCount)
          + "ms (" + ((float) paintCount / (now - lastStats) * 1000) + "/s) lag = "
          + ((float) paintLagTime / paintCount) + "ms");
    if (updateCount > 0)
      Log.i("playn", "Stats: updates = " + updateCount + " "
          + ((float) updateTime / updateCount) + "ms ("
          + ((float) updateCount / (now - lastStats) * 1000) + "/s)");
    Log.i("playn", "Stats: runs = " + runCount + " run lag = "
        + ((float) runQueueTime / runCount) + "ms (" + runCount / ((float) now - lastStats)
        * 1000 + "/s)");
    paintCount = updateCount = runCount = 0;
    paintTime = paintLagTime = updateTime = 0;
    lastStats = now;
  }

  private int time() {
    // System.nanoTime() would be better here, but it's busted on the HTC EVO
    // 2.3 update. Instead we use an offset from a known time to keep it within
    // int range.
    return (int) (System.currentTimeMillis() - timeOffset);
  }
  
  public boolean running() {
    return running.get();
  }

  protected void paint() {
    double start = time();
    paintLagTime += start - paintQueueStart;
    AndroidPlatform.instance.game.paint(paintAlpha);  //Run the game's custom layer-painting code
    gfx.updateLayers();  //Actually draw to the screen
    paintTime += time() - start;
    paintCount++;
  }

}
