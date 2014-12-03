/**
 * Copyright 2014 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package playn.robovm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSTimer;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.glkit.GLKViewDrawableColorFormat;
import org.robovm.apple.opengles.EAGLContext;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIInterfaceOrientation;
import org.robovm.apple.uikit.UIInterfaceOrientationMask;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.objc.Selector;
import org.robovm.objc.annotation.BindSelector;
import org.robovm.rt.bro.annotation.Callback;

import playn.core.*;
import playn.core.json.JsonImpl;

public class RoboPlatform extends AbstractPlatform {

  /** Used to configure the RoboVM platform. */
  public static class Config {
    /** Indicates which orients are supported by your app. You should also configure this
      * information in your {@code Info.plist} file. */
    public UIInterfaceOrientationMask orients = UIInterfaceOrientationMask.Portrait;

    /** If true, an iPad will be treated like a 2x Retina device with resolution 384x512 and which
      * will use @2x images. A Retina iPad will also have resolution 384x512 and will use @4x
      * images if they exist, then fall back to @2x (and default (1x) if necessary). If false, iPad
      * will be treated as a non-Retina device with resolution 768x1024 and will use default (1x)
      * images, and a Retina iPad will be treated as a Retina device with resolution 768x1024 and
      * will use @2x images. */
    public boolean iPadLikePhone = false;

    /** Indicates the frequency at which the game should be rendered (and updated). Defaults to
      * one, which means one render per device screen refresh (maximum FPS). Higher values (like 2)
      * can be used to reduce the update rate to half or third FPS for games that can't run at full
      * FPS. As the iOS docs say: a game that runs at a consistent but slow frame rate is better
      * than a game that runs at an erratic frame rate. */
    public int frameInterval = 1;

    /** If true, calls to CanvasImage.draw() on a retina device using a non-retina image as the
      * source will use the default interpolation defined for CGBitmapContext. This will
      * potentially make scaled non-retina images look better, but has performance and pixel
      * accuracy implications. */
    public boolean interpolateCanvasDrawing = true;

    /** The number of audio channels to reserve for OpenAL. This dictates the number of
      * simultaneous sounds that can be played via OpenAL. It can't be higher than 32, and can be
      * reduced from the default of 24 if you plan to play a lot of compressed sound effects
      * simultaneously (those don't go through OpenAL, they go through AVAudioPlayer, and I presume
      * AVAudioPlayer competes with OpenAL for sound channels). */
    public int openALSources = 24;

    /** Seconds to wait for the game loop to terminate before terminating GL and AL services. This
      * is only used if PlayN is integrated into a larger iOS application and does not control the
      * application lifecycle. */
    public float timeForTermination = 0.5f;

    /** Configures the format of the GL framebuffer. The default is RGBA8888, but one can use
      * RGB565 for higher performance at the cost of lower color fidelity. */
    public GLKViewDrawableColorFormat glBufferFormat = GLKViewDrawableColorFormat.RGBA8888;

    /** Dictates the name of the temporary file used by {@link RoboStorage}. Configure this if you
      * want to embed multiple games into your application. */
    public String storageFileName = "playn.db";
  }

  /** Enables games to respond to device orientation changes. */
  public static interface OrientationListener {
    void willRotate(UIInterfaceOrientation toOrient, double duration);
    void didRotate(UIInterfaceOrientation orientation);
  }

  /** Configures a listener to be notified when the device rotates. */
  public void setListener(OrientationListener listener) {
    orientListener = listener;
  }

  private RoboAudio audio; // lazily initialized
  private final RoboGraphics graphics;
  private final Json json;
  private final RoboKeyboard keyboard;
  private final RoboNet net;
  private final RoboPointer pointer;
  private final RoboStorage storage;
  private final RoboTouch touch;
  private final RoboAssets assets;

  /** It's used as a guard flag to avoid duplicated entries caused by the twice dispatches of 
   * GLKViewControllerDelegate.willPause in one cycle. That could be a bug of RoboVM. 
   * TODO: remove this after we figure out a better solution. **/
  private boolean paused = false;
  private Game game;
  private OrientationListener orientListener;
  private final long gameStart = System.nanoTime();
  private final ExecutorService pool = Executors.newFixedThreadPool(3);

  final int osVersion = getOSVersion();
  final Config config;

  protected RoboPlatform(CGRect bounds, Config config) {
    super(new RoboLog());
    this.config = config;
    bounds = bounds != null ? bounds : UIScreen.getMainScreen().getBounds();
    graphics = new RoboGraphics(this, bounds);
    json = new JsonImpl();
    keyboard = new RoboKeyboard(this);
    net = new RoboNet(this);
    pointer = new RoboPointer(this);
    touch = new RoboTouch(this);
    assets = new RoboAssets(this);
    storage = new RoboStorage(this);
  }

  @Override
  public void invokeAsync(Runnable action) {
    pool.execute(action);
  }

  @Override
  public Type type() {
    return Type.IOS;
  }

  @Override
  public RoboAssets assets() {
    return assets;
  }

  @Override
  public RoboAudio audio() {
    if (audio == null) audio = new RoboAudio(this, config.openALSources);
    return audio;
  }

  @Override
  public RoboGraphics graphics() {
    return graphics;
  }

  @Override
  public Json json() {
    return json;
  }

  @Override
  public RoboKeyboard keyboard() {
    return keyboard;
  }

  @Override
  public RoboNet net() {
    return net;
  }

  @Override
  public Mouse mouse() {
    return new MouseStub();
  }

  @Override
  public RoboTouch touch() {
    return touch;
  }

  @Override
  public RoboPointer pointer() {
    return pointer;
  }

  @Override
  public float random() {
    return (float) Math.random();
  }

  @Override
  public RoboStorage storage() {
    return storage;
  }

  @Override
  public double time() {
    return System.currentTimeMillis();
  }

  @Override
  public int tick() {
    return (int)((System.nanoTime() - gameStart) / 1000000);
  }

  @Override
  public void openURL(String url) {
    if (!UIApplication.getSharedApplication().openURL(new NSURL(url))) {
      log().warn("Failed to open URL: " + url);
    }
  }

  @Override
  public void setPropagateEvents(boolean propagate) {
    touch.setPropagateEvents(propagate);
    pointer.setPropagateEvents(propagate);
  }

  @Override
  public void run(Game game) {
    this.game = game;
    // initialize the game and start things off
    game.init();
  }

  void willRotate(UIInterfaceOrientation toOrient, double duration) {
    if (orientListener != null) {
      orientListener.willRotate(toOrient, duration);
    }
  }

  void didRotate (UIInterfaceOrientation fromOrient) {
    if (orientListener != null) {
      orientListener.didRotate(fromOrient);
    }
  }

  void update() {
    // process pending actions
    runQueue.execute();
    // perform the game updates
    game.tick(tick());
    // flush any pending draw calls (to surfaces)
    graphics.ctx().flush();
  }

  void paint() {
    graphics.paint();
  }

  void willEnterForeground () {
    if (!paused) return;
    paused = false;
    invokeLater(new Runnable() {
      public void run() {
        onResume();
      }
    });
  }
  
  void didEnterBackground () {
    if (paused) return;
    paused = true;
    // we call this directly rather than via invokeLater() because the PlayN thread is already
    // stopped at this point so a) there's no point in worrying about racing with that thread,
    // and b) onPause would never get called, since the PlayN thread is not processing events
    onPause();
  }
  
  void willTerminate () {
    // let the app know that we're terminating
    onExit();
    // shutdown the GL and AL systems
    ResourceCleaner.terminate(this);
  }

  private int getOSVersion () {
    String systemVersion = UIDevice.getCurrentDevice().getSystemVersion();
    int version = Integer.parseInt(systemVersion.split("\\.")[0]);
    return version;
  }

  private static class ResourceCleaner extends NSObject {
    private final static Selector SEL = Selector.register("cleanRelatedResources:");
    private RoboPlatform platform;

    private ResourceCleaner(RoboPlatform platform) {
      super();
      this.platform = platform;
    }

    // wait for the desired interval and then terminate the GL and AL
    // systems
    public static void terminate(RoboPlatform platform) {
      NSTimer.createScheduled(platform.config.timeForTermination, new ResourceCleaner(platform),
          ResourceCleaner.SEL, null, false);
    }

    @Callback @BindSelector("cleanRelatedResources:")
    private static void cleanRelatedResources(ResourceCleaner self, Selector sel) {
      if (self.platform != null) {
        // shutdown the GL view completely
        EAGLContext.setCurrentContext(null);
        // stop and release the AL resources (if audio was ever initialized)
        if (self.platform.audio != null) self.platform.audio.terminate();
      }

      self.platform = null;
      // clear out the platform in order to make sure the game creation
      // flow can be repeated when
      // it is used as a part of a larger application
      PlayN.setPlatform(null);
    }
  }

}
