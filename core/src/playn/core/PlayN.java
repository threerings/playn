/**
 * Copyright 2010 The PlayN Authors
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
package playn.core;

/**
 * The main PlayN interface. The static methods in this class provide access to
 * the various available subsystems.
 *
 * <p>
 * You must register a {@link Platform} before calling any of these methods. For
 * example, {@code JavaPlatform.register()}.
 * </p>
 */
public class PlayN {

  private static Platform platform;

  /** Used to receive lifecycle notifications. See {@link #setLifecycleListener}. */
  public static interface LifecycleListener {
    /** Called just before the game is paused (made non-active on mobile platforms). */
    void onPause();
    /** Called just after the game is resumed (made active on mobile platforms). */
    void onResume();
    /** Called just before the game will be closed (on mobile and web platforms). */
    void onExit();
  }

  /** Used to customize top-level error reporting. See {@link #setErrorReporter}. */
  public static interface ErrorReporter {
    /** Called to report a top-level error. This is called by internal PlayN backend code when it
     * encounters an error from which it can recover, but which it would like to report in an
     * orderly fashion. <em>NOTE:</em> this method may be called from any thread, not just the main
     * PlayN thread. So an implementation must take care to get back onto the PlayN thread if it's
     * going to do anything other than turn around and log the message.
     */
    void reportError(String message, Throwable err);
  }

  /**
   * Returns the currently registered platform instance. This can be useful for games that wish to
   * avoid littering their code with calls to the static {@link PlayN} methods, and for services
   * that can accept a mocked {@link Platform} for testing.
   */
  public static Platform platform() {
    return platform;
  }

  /**
   * Returns the platform {@link Platform.Type}.
   */
  public static Platform.Type platformType() {
    return platform.type();
  }

  /**
   * Call this method to start your {@link Game}. It must be called only once,
   * and all work after this call is made will be performed in {@link Game}'s
   * callback methods.
   */
  public static void run(Game game) {
    platform.run(game);
  }

  /**
   * Called when a backend (or other framework code) encounters an exception that it can recover
   * from, but which it would like to report in some orderly fashion. <em>NOTE:</em> this method
   * may be called from threads other than the main PlayN thread.
   */
  public static void reportError(String message, Throwable err) {
    platform.reportError(message, err);
  }

  /**
   * Returns the current time, as a double value in millis since January 1, 1970, 00:00:00 GMT.
   *
   * <p> This is equivalent to the standard JRE {@code new Date().getTime();}, but it slightly
   * terser, and avoids the use of {@code long} values, which are best avoided when translating to
   * JavaScript. </p>
   */
  public static double currentTime() {
    return platform.time();
  }

  /**
   * Returns the number of milliseconds that have elapsed since the game started.
   */
  public static int tick() {
    return platform.tick();
  }

  /**
   * Gets a random floating-point value in the range [0, 1).
   */
  public static float random() {
    return platform.random();
  }

  /**
   * Opens the given URL in the default browser.
   */
  public static void openURL(String url) {
    platform.openURL(url);
  }

  /**
   * Queues the supplied runnable for invocation on the game thread prior to the next call to
   * {@link Game.Default#update}.
   */
  public static void invokeLater(Runnable runnable) {
    platform.invokeLater(runnable);
  }

  /**
   * Configures a listener to be notified of lifecycle events. Any previous listener will be
   * overwritten. Supply null to clear the listener.
   */
  public static void setLifecycleListener(LifecycleListener listener) {
    platform.setLifecycleListener(listener);
  }

  /**
   * Configures the top-level error reporter. Any previous reporter will be overwritten. Supply
   * null to revert to the default error reporter. <em>NOTE:</em> the error reporter may be called
   * from any thread, not just the main PlayN thread. So an implementation must take care to get
   * back onto the PlayN thread if it's going to do anything other than turn around and log the
   * message.
   */
  public static void setErrorReporter(ErrorReporter reporter) {
    platform.setErrorReporter(reporter);
  }

  /**
   * Configures whether events on a layer (pointer, touch and mouse) are automatically dispatched
   * to the layer's ancestors. This is a global option that may affect performance, so applications
   * must call this opt in.
   */
  public static void setPropagateEvents(boolean propagate) {
    platform.setPropagateEvents(propagate);
  }

  /**
   * Returns the {@link Audio} service.
   */
  public static Audio audio() {
    return platform.audio();
  }

  /**
   * Returns the {@link Graphics} service.
   */
  public static Graphics graphics() {
    return platform.graphics();
  }

  /**
   * Returns the {@link Assets} service.
   */
  public static Assets assets() {
    return platform.assets();
  }

  /**
   * Returns the {@link Json} service.
   */
  public static Json json() {
    return platform.json();
  }

  /**
   * Returns the {@link Keyboard} input service.
   */
  public static Keyboard keyboard() {
    return platform.keyboard();
  }

  /**
   * Returns the {@link Log} service.
   */
  public static Log log() {
    return platform.log();
  }

  /**
   * Returns the {@link Net} service.
   */
  public static Net net() {
    return platform.net();
  }

  /**
   * Returns the {@link Pointer} input service.
   */
  public static Pointer pointer() {
    return platform.pointer();
  }

  /**
   * Returns the {@link Mouse} input service.
   */
  public static Mouse mouse() {
    return platform.mouse();
  }

  /**
   * Returns the {@link Touch} input service.
   */
  public static Touch touch() {
    return platform.touch();
  }
  
  /**
   * Returns the {@link Gamepads} input service.
   */
  public static Gamepads gamepads() {
    return platform.gamepads();
  }

  /**
   * Returns the {@link Storage} storage service.
   */
  public static Storage storage() {
    return platform.storage();
  }

  /**
   * Configures the current {@link Platform}. Do not call this directly unless you're implementing
   * a new platform.
   */
  public static void setPlatform(Platform platform) {
    PlayN.platform = platform;
  }

  // Non-instantiable
  private PlayN() {
  }
}
