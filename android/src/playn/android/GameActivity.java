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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import playn.core.PlayN;
import playn.core.Game;

/**
 * TODO: pause/unpause TODO: save/restore state
 */
public abstract class GameActivity extends Activity {
  private final int REQUIRED_CONFIG_CHANGES = ActivityInfo.CONFIG_ORIENTATION
      | ActivityInfo.CONFIG_KEYBOARD_HIDDEN;

  private GameView gameView;
  private WakeLock wakeLock;
  private Context context;

  public abstract void main();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    context = getApplicationContext();

    if (supportsHardwareAcceleration()) {
      // Use the raw constant rather than the flag to avoid blowing up on
      // earlier Android
      int flagHardwareAccelerated = 0x1000000;

      getWindow().setFlags(flagHardwareAccelerated, flagHardwareAccelerated);
      gameView = new GameViewDraw(this, context);
      Log.i("playn", "Using hardware-acceleration-friendly game loop");
    } else {
      gameView = new GameViewSurface(this, context);
      Log.i("playn", "Using software-acceleration-friendly game loop");
    }

    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

    LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    getWindow().setContentView((View) gameView, params);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    try {
      PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
      wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
          | PowerManager.ON_AFTER_RELEASE, "playn");
      wakeLock.acquire();
    } catch (SecurityException e) {
      // Warn the developer of a missing permission. The other calls to
      // wakeLock.acquire/release will throw.
      new AlertDialog.Builder(this).setMessage(
          "Unable to acquire wake lock. Please add <uses-permission " +
          "android:name=\"android.permission.WAKE_LOCK\" /> to the manifest.").show();
    }

    try {
      ActivityInfo info = this.getPackageManager().getActivityInfo(
          new ComponentName(context, this.getPackageName() + "." + this.getLocalClassName()), 0);
      if ((info.configChanges & REQUIRED_CONFIG_CHANGES) != REQUIRED_CONFIG_CHANGES) {
        new AlertDialog.Builder(this).setMessage(
            "Unable to guarantee application will handle configuration changes. "
                + "Please add the following line to the Activity manifest: "
                + "      android:configChanges=\"keyboardHidden|orientation\"").show();
      }

    } catch (NameNotFoundException e) {
      Log.w("GameActivity", "Cannot access game AndroidManifest.xml file.");
    }

  }

  protected AndroidPlatform platform() {
    return AndroidPlatform.instance;
  }

  protected Context context() {
    return context;
  }

  private boolean supportsHardwareAcceleration() {
    return android.os.Build.VERSION.SDK_INT >= 11;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    wakeLock.release();
    platform().audio().destroy();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    AndroidGraphics graphics = (AndroidGraphics) PlayN.graphics();
    if (graphics != null)
      graphics.refreshScreenMetrics();
  }

  @Override
  protected void onPause() {
    Log.i("playn", "onPause");
    gameView.notifyVisibilityChanged(View.INVISIBLE);
    platform().audio().pause();
    wakeLock.release();
    super.onPause();

    // TODO: Notify game
  }

  @Override
  protected void onResume() {
    Log.i("playn", "onResume");
    gameView.notifyVisibilityChanged(View.VISIBLE);
    platform().audio().resume();
    wakeLock.acquire();
    super.onResume();

    // TODO: Notify game
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    // TODO
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return platform().keyboard().onKeyDown(event.getEventTime(), keyCode);
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
    return platform().keyboard().onKeyUp(event.getEventTime(), keyCode);
  }

  /**
   * Called automatically to handle touch events. Automatically passes through
   * the parsed MotionEvent to AndroidTouch.Listener and AndroidPointer.Listener
   * instances.
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return AndroidPlatform.instance.touchEventHandler().onMotionEvent(event);
  }

  public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    int displayWidth = right - left;
    int displayHeight = bottom - top;

    /*
     * TODO: Pass the width and height here into AndroidGraphics as the display
     * width/height (this is the only way to take into account the size of the
     * Honeycomb bezel). This requires the game activity lifecycle to be
     * reworked, so it is currently not implemented.
     */
  }
}
