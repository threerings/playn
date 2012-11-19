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

import android.view.MotionEvent;

import pythagoras.f.IPoint;

import playn.core.Events;
import playn.core.Pointer;
import playn.core.Touch;

class AndroidTouchEventHandler {

  private final GameViewGL gameView;

  AndroidTouchEventHandler(GameViewGL gameView) {
    this.gameView = gameView;
  }

  public void onMotionEvent(MotionEvent nativeEvent) {
    // extract the native event data while we're on the UI thread
    final int action = nativeEvent.getAction();
    final double time = nativeEvent.getEventTime();
    final Events.Flags flags = new Events.Flags.Impl();
    final Touch.Event.Impl[] touches = parseMotionEvent(nativeEvent, flags);

    // then process it (issuing game callbacks) on the GL/Game thread
    gameView.platform.invokeLater(new Runnable() {
      public void run() {
        Touch.Event pointerEvent = touches[0];
        switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
          gameView.platform.touch().onTouchStart(touches);
          gameView.platform.pointer().onPointerStart(
            new Pointer.Event.Impl(flags, time, pointerEvent.x(), pointerEvent.y(), true));
          break;
        case MotionEvent.ACTION_UP:
          gameView.platform.touch().onTouchEnd(touches);
          gameView.platform.pointer().onPointerEnd(
            new Pointer.Event.Impl(flags, time, pointerEvent.x(), pointerEvent.y(), true));
          break;
        case MotionEvent.ACTION_POINTER_DOWN:
          gameView.platform.touch().onTouchStart(getChangedTouches(action, touches));
          break;
        case MotionEvent.ACTION_POINTER_UP:
          gameView.platform.touch().onTouchEnd(getChangedTouches(action, touches));
          break;
        case MotionEvent.ACTION_MOVE:
          gameView.platform.touch().onTouchMove(touches);
          gameView.platform.pointer().onPointerDrag(
            new Pointer.Event.Impl(flags, time, pointerEvent.x(), pointerEvent.y(), true));
          break;
        case MotionEvent.ACTION_CANCEL:
          gameView.platform.touch().onTouchCancel(touches);
          gameView.platform.pointer().onPointerCancel(
            new Pointer.Event.Impl(flags, time, pointerEvent.x(), pointerEvent.y(), true));
          break;
        // case MotionEvent.ACTION_OUTSIDE:
        //   break;
        }
      }
    });
  }

  private Touch.Event.Impl[] getChangedTouches(int action, Touch.Event.Impl[] touches) {
    int changed = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
      >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    return new Touch.Event.Impl[] { touches[changed] };
  }

  /**
   * Performs the actual parsing of the MotionEvent event.
   *
   * @param event The MotionEvent to process
   * @param preventDefault Shared preventDefault state among returned {@link AndroidTouchEventImpl}
   * @return Processed array of {@link AndroidTouchEventImpl}s which share a preventDefault state.
   */
  private Touch.Event.Impl[] parseMotionEvent(MotionEvent event, Events.Flags flags) {
    int eventPointerCount = event.getPointerCount();
    Touch.Event.Impl[] touches = new Touch.Event.Impl[eventPointerCount];
    double time = event.getEventTime();
    float pressure, size;
    int id;
    for (int t = 0; t < eventPointerCount; t++) {
      int pointerIndex = t;
      IPoint xy = gameView.platform.graphics().transformTouch(
        event.getX(pointerIndex), event.getY(pointerIndex));
      pressure = event.getPressure(pointerIndex);
      size = event.getSize(pointerIndex);
      id = event.getPointerId(pointerIndex);
      touches[t] = new Touch.Event.Impl(flags, time, xy.x(), xy.y(), id, pressure, size);
    }
    return touches;
  }
}
