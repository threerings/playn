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
package playn.flash;

import flash.events.KeyboardEvent;
import flash.display.InteractiveObject;

import playn.core.PlayN;
import playn.core.Keyboard;

class FlashKeyboard implements Keyboard {

  private Listener listener;

  FlashKeyboard() {
    // Key handlers.
    FlashPlatform.captureEvent(InteractiveObject.KEYDOWN, new EventHandler<KeyboardEvent>() {
      public void handleEvent(KeyboardEvent nativeEvent) {
        if (listener != null) {
          Event.Impl event = new Event.Impl(PlayN.currentTime(), nativeEvent.keyCode());
          listener.onKeyDown(event);
          if (event.getPreventDefault()) {
            nativeEvent.preventDefault();
          }
        }
      }
    });

    FlashPlatform.captureEvent(InteractiveObject.KEYUP, new EventHandler<KeyboardEvent>() {
      public void handleEvent(KeyboardEvent nativeEvent) {
        if (listener != null) {
          Event.Impl event = new Event.Impl(PlayN.currentTime(), nativeEvent.keyCode());
          listener.onKeyUp(event);
          if (event.getPreventDefault()) {
            nativeEvent.preventDefault();
          }
        }
      }
    });
  }

  @Override
  public void setListener(Listener listener) {
    this.listener = listener;
  }
}
