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

import playn.core.Touch;

public class AndroidTouch implements Touch {
  private Listener listener;
  private static final int MAX_STORED_EVENTS_PER_TYPE = 3;
  private Event[][] storedStartEvents = new Event[MAX_STORED_EVENTS_PER_TYPE][];
  private Event[][] storedMoveEvents = new Event[MAX_STORED_EVENTS_PER_TYPE][];
  private Event[][] storedEndEvents = new Event[MAX_STORED_EVENTS_PER_TYPE][];
  private int storedStartIndex, storedMoveIndex, storedEndIndex;

  synchronized void onTouchStart(Event[] touches) {
    if (listener != null && storedStartIndex < MAX_STORED_EVENTS_PER_TYPE)
      storedStartEvents[storedStartIndex++] = touches;
  }

  synchronized void onTouchMove(Event[] touches) {
    if (listener != null && storedMoveIndex < MAX_STORED_EVENTS_PER_TYPE)
      storedMoveEvents[storedMoveIndex++] = touches;
  }

  synchronized void onTouchEnd(Event[] touches) {
    if (listener != null && storedEndIndex < MAX_STORED_EVENTS_PER_TYPE)
      storedMoveEvents[storedEndIndex++] = touches;
  }

  @Override
  public synchronized void setListener(Listener listener) {
    this.listener = listener;
  }

  synchronized void processQueuedEvents() {
    if (listener != null) {
      for (int i = 0; i < MAX_STORED_EVENTS_PER_TYPE; i++) {
        if (storedStartEvents[i] != null) {
          listener.onTouchStart(storedStartEvents[i]);
          storedStartEvents[i] = null;
        }
        if (storedMoveEvents[i] != null) {
          listener.onTouchMove(storedMoveEvents[i]);
          storedMoveEvents[i] = null;
        }
        if (storedEndEvents[i] != null) {
          listener.onTouchEnd(storedEndEvents[i]);
          storedEndEvents[i] = null;
        }
        storedStartIndex = storedMoveIndex = storedEndIndex = 0;
      }
    }
  }
}
