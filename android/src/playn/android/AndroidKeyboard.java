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
package playn.android;

import playn.core.Keyboard;

public class AndroidKeyboard implements Keyboard {

  private Listener listener;

  @Override
  public void setListener(Listener listener) {
    this.listener = listener;
  }

  boolean onKeyDown(double time, int keyCode) {
    if (listener != null) {
      Event.Impl event = new Event.Impl(time, keyCode);
      listener.onKeyDown(event);
      return event.getPreventDefault();
    }
    return false;
  }

  boolean onKeyUp(double time, int keyCode) {
    if (listener != null) {
      Event.Impl event = new Event.Impl(time, keyCode);
      listener.onKeyUp(event);
      return event.getPreventDefault();
    }
    return false;
  }
}
