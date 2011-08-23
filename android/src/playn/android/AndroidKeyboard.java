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
package playn.android;

import playn.core.Keyboard;

public class AndroidKeyboard implements Keyboard {
  private Listener listener;
  private static final int MAX_STORED_EVENTS_PER_TYPE = 3;
  private Event.Impl[] storedDownEvents = new Event.Impl[MAX_STORED_EVENTS_PER_TYPE];
  private Event.Impl[] storedUpEvents = new Event.Impl[MAX_STORED_EVENTS_PER_TYPE];
  private int storedDownIndex, storedUpIndex;

  @Override
  public void setListener(Listener listener) {
    this.listener = listener;
  }

  synchronized void onKeyDown(double time, int keyCode) {
    if (listener != null && storedDownIndex < MAX_STORED_EVENTS_PER_TYPE)
      storedDownEvents[storedDownIndex++] = new Event.Impl(time, keyCode);
  }

  synchronized void onKeyUp(double time, int keyCode) {
    if (listener != null && storedUpIndex < MAX_STORED_EVENTS_PER_TYPE)
      storedUpEvents[storedUpIndex++] = new Event.Impl(time, keyCode);
  }

  synchronized void processQueuedEvents() {
    if (listener != null) {
      for (int i = 0; i < MAX_STORED_EVENTS_PER_TYPE; i++) {
        if (storedDownEvents[i] != null) {
          listener.onKeyDown(storedDownEvents[i]);
          storedDownEvents[i] = null;
        }
        if (storedUpEvents[i] != null) {
          listener.onKeyUp(storedUpEvents[i]);
          storedUpEvents[i] = null;
        }
        storedDownIndex = storedUpIndex = 0;
      }
    }
  }
}
