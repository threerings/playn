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
  @Deprecated
  public static Platform platform() {
    return platform;
  }

  /**
   * @deprecated use {@link Platform#type()} instead
   */
  @Deprecated
  public static Platform.Type platformType() {
    return platform.type();
  }

  /**
   * @deprecated use {@link Platform#run(Game game)} instead
   */
  @Deprecated
  public static void run(Game game) {
    platform.run(game);
  }

  /**
   * @deprecated use {@link Platform#reportError(String message, Throwable err)} instead
   */
  @Deprecated
  public static void reportError(String message, Throwable err) {
    platform.reportError(message, err);
  }

  /**
   * @deprecated use {@link Platform#time()} instead
   */
  @Deprecated
  public static double currentTime() {
    return platform.time();
  }

  /**
   * @deprecated use {@link Platform#tick()} instead
   */
  @Deprecated
  public static int tick() {
    return platform.tick();
  }

  /**
   * @deprecated use {@link Platform#random()} instead
   */
  @Deprecated
  public static float random() {
    return platform.random();
  }

  /**
   * @deprecated use {@link Platform#openURL(String url)} instead
   */
  @Deprecated
  public static void openURL(String url) {
    platform.openURL(url);
  }

  /**
   * @deprecated use {@link Platform#invokeLater(Runnable runnable)} instead
   */
  @Deprecated
  public static void invokeLater(Runnable runnable) {
    platform.invokeLater(runnable);
  }

  /**
   * @deprecated use {@link Platform#setLifecycleListener(LifecycleListener listener)} instead
   */
  @Deprecated
  public static void setLifecycleListener(LifecycleListener listener) {
    platform.setLifecycleListener(listener);
  }

  /**
   * @deprecated use {@link Platform#setErrorReporter(ErrorReporter reporter)} instead
   */
  @Deprecated
  public static void setErrorReporter(ErrorReporter reporter) {
    platform.setErrorReporter(reporter);
  }

  /**
   * @deprecated use {@link Platform#setPropagateEvents(boolean propagate)} instead
   */
  @Deprecated
  public static void setPropagateEvents(boolean propagate) {
    platform.setPropagateEvents(propagate);
  }

  /**
   * @deprecated use {@link Platform#audio()} instead
   */
  @Deprecated
  public static Audio audio() {
    return platform.audio();
  }

  /**
   * @deprecated use {@link Platform#graphics()} instead
   */
  @Deprecated
  public static Graphics graphics() {
    return platform.graphics();
  }

  /**
   * @deprecated use {@link Platform#assets()} instead
   */
  @Deprecated
  public static Assets assets() {
    return platform.assets();
  }

  /**
   * @deprecated use {@link Platform#json()} instead
   */
  @Deprecated
  public static Json json() {
    return platform.json();
  }

  /**
   * @deprecated use {@link Platform#keyboard()} instead
   */
  @Deprecated
  public static Keyboard keyboard() {
    return platform.keyboard();
  }

  /**
   * @deprecated use {@link Platform#log()} instead
   */
  @Deprecated
  public static Log log() {
    return platform.log();
  }

  /**
   * @deprecated use {@link Platform#net()} instead
   */
  @Deprecated
  public static Net net() {
    return platform.net();
  }

  /**
   * @deprecated use {@link Platform#pointer()} instead
   */
  @Deprecated
  public static Pointer pointer() {
    return platform.pointer();
  }

  /**
   * @deprecated use {@link Platform#mouse()} instead
   */
  @Deprecated
  public static Mouse mouse() {
    return platform.mouse();
  }

  /**
   * @deprecated use {@link Platform#touch()} instead
   */
  @Deprecated
  public static Touch touch() {
    return platform.touch();
  }

  /**
   * @deprecated use {@link Platform#storage()} instead
   */
  @Deprecated
  public static Storage storage() {
    return platform.storage();
  }

  /**
   * Configures the current {@link Platform}. Do not call this directly unless you're implementing
   * a new platform.
   */
  @Deprecated
  public static void setPlatform(Platform platform) {
    PlayN.platform = platform;
  }

  // Non-instantiable
  private PlayN() {
  }
}
