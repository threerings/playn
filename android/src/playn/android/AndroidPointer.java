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

import playn.core.Pointer;

class AndroidPointer implements Pointer {
  private static final int MAX_STORED_EVENTS_PER_TYPE = 1;
  private Event[] storedStartEvents = new Event[MAX_STORED_EVENTS_PER_TYPE];
  private Event[] storedMoveEvents = new Event[MAX_STORED_EVENTS_PER_TYPE];
  private Event[] storedEndEvents = new Event[MAX_STORED_EVENTS_PER_TYPE];
  private int storedStartIndex, storedMoveIndex, storedEndIndex;

  // true when we are in a drag sequence (after pointer start but before pointer
  // end)
  private boolean inDragSequence = false;
  private Listener listener;

  @Override
  public synchronized void setListener(Listener listener) {
    this.listener = listener;
  }

  synchronized void onPointerStart(Event event) {
    if (listener != null && storedStartIndex < MAX_STORED_EVENTS_PER_TYPE) {
      inDragSequence = true;
      storedStartEvents[storedStartIndex++] = event;
    }
  }

  synchronized void onPointerMove(Event event) {
    if (listener != null && storedMoveIndex < MAX_STORED_EVENTS_PER_TYPE) {
      if (inDragSequence) {
        listener.onPointerDrag(event);
        storedMoveEvents[storedMoveIndex++] = event;
      }
    }
  }

  synchronized void onPointerEnd(Event event) {
    if (listener != null && storedEndIndex < MAX_STORED_EVENTS_PER_TYPE) {
      inDragSequence = false;
      storedEndEvents[storedEndIndex++] = event;
    }
  }

  synchronized void processQueuedEvents() {
    if (listener != null) {
      for (int i = 0; i < MAX_STORED_EVENTS_PER_TYPE; i++) {
        if (storedStartEvents[i] != null) {
          listener.onPointerStart(storedStartEvents[i]);
          storedStartEvents[i] = null;
        }
        if (storedMoveEvents[i] != null) {
          listener.onPointerDrag(storedMoveEvents[i]);
          storedMoveEvents[i] = null;
        }
        if (storedEndEvents[i] != null) {
          listener.onPointerEnd(storedEndEvents[i]);
          storedEndEvents[i] = null;
        }
        storedStartIndex = storedMoveIndex = storedEndIndex = 0;
      }
    }
  }
}
