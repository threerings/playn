/**
 * Copyright 2010 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package playn.core;

/**
 * Generic platform interface. New platforms are defined as implementations of this interface.
 */
public interface Platform {

  enum Type { JAVA, HTML, ANDROID, IOS, FLASH, STUB }

  /**
   * Returns the platform {@link Platform.Type}.
   */
  Platform.Type type();

  /**
   * Call this method to start your {@link Game}. It must be called only once,
   * and all work after this call is made will be performed in {@link Game}'s
   * callback methods.
   */
  void run(Game game);

  /**
   * Called when a backend (or other framework code) encounters an exception that it can recover
   * from, but which it would like to report in some orderly fashion. <em>NOTE:</em> this method
   * may be called from threads other than the main PlayN thread.
   */
  void reportError(String message, Throwable cause);

  /**
   * Returns the current time, as a double value in millis since January 1, 1970, 00:00:00 GMT.
   *
   * <p> This is equivalent to the standard JRE {@code new Date().getTime();}, but it slightly
   * terser, and avoids the use of {@code long} values, which are best avoided when translating to
   * JavaScript. </p>
   */
  double time();

  /**
   * Returns the number of milliseconds that have elapsed since the game started.
   */
  int tick();

  /**
   * Gets a random floating-point value in the range [0, 1).
   */
  float random();

  /**
   * Opens the given URL in the default browser.
   */
  void openURL(String url);

  /**
   * Queues the supplied runnable for invocation on the game thread prior to the next call to
   * {@link Game.Default#update}.
   */
  void invokeLater(Runnable runnable);

  /**
   * Configures a listener to be notified of lifecycle events. Any previous listener will be
   * overwritten. Supply null to clear the listener.
   */
  void setLifecycleListener(PlayN.LifecycleListener listener);

  /**
   * Configures the top-level error reporter. Any previous reporter will be overwritten. Supply
   * null to revert to the default error reporter. <em>NOTE:</em> the error reporter may be called
   * from any thread, not just the main PlayN thread. So an implementation must take care to get
   * back onto the PlayN thread if it's going to do anything other than turn around and log the
   * message.
   */
  void setErrorReporter(PlayN.ErrorReporter reporter);

  /**
   * Configures whether events on a layer (pointer, touch and mouse) are automatically dispatched
   * to the layer's ancestors. This is a global option that may affect performance, so applications
   * must call this opt in.
   */
  void setPropagateEvents(boolean propagate);

  /**
   * Returns the {@link Audio} service.
   */
  Audio audio();

  /**
   * Returns the {@link Graphics} service.
   */
  Graphics graphics();

  /**
   * Returns the {@link Assets} service.
   */
  Assets assets();

  /**
   * Returns the {@link Json} service.
   */
  Json json();

  /**
   * Returns the {@link Keyboard} input service.
   */
  Keyboard keyboard();

  /**
   * Returns the {@link Log} service.
   */
  Log log();

  /**
   * Returns the {@link Net} service.
   */
  Net net();

  /**
   * Returns the {@link Pointer} input service.
   */
  Pointer pointer();

  /**
   * Returns the {@link Mouse} input service.
   */
  Mouse mouse();

  /**
   * Returns the {@link Touch} input service.
   */
  Touch touch();

  /**
   * Returns the {@link Storage} storage service.
   */
  Storage storage();
}
