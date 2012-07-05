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

import playn.core.Pointer;
import playn.core.Touch;

class AndroidTouchEventHandler {

  private final AndroidPlatform platform;

  AndroidTouchEventHandler(AndroidPlatform platform) {
    this.platform = platform;
  }

  public void onMotionEvent(MotionEvent nativeEvent) {
    // extract the native event data while we're on the UI thread
    final int action = nativeEvent.getAction();
    final double time = nativeEvent.getEventTime();
    int eventPointerCount = nativeEvent.getPointerCount();
    final Touch.Event.Impl[] touches = new Touch.Event.Impl[eventPointerCount];
    float pressure, size;
    int id;
    for (int t = 0; t < eventPointerCount; t++) {
      int pointerIndex = t;
      IPoint xy = platform.graphics().transformTouch(
        nativeEvent.getX(pointerIndex), nativeEvent.getY(pointerIndex));
      pressure = nativeEvent.getPressure(pointerIndex);
      size = nativeEvent.getSize(pointerIndex);
      id = nativeEvent.getPointerId(pointerIndex);
      platform.log().info("Touch event " + xy.x() + " " + xy.y() + " " + id);
      touches[t] = new Touch.Event.Impl(time, xy.x(), xy.y(), id, pressure, size);
    }

    // then process it (issuing game callbacks) on the GL/Game thread
    platform.invokeLater(new Runnable() {
      public void run() {
        processMotionEvent(action, time, touches);
      }
    });
  }

  private void processMotionEvent(int action, double time, Touch.Event.Impl[] touches) {
    Touch.Event pointerEvent = touches[0];
    switch (action & MotionEvent.ACTION_MASK) {
    case MotionEvent.ACTION_DOWN:
      platform.touch().onTouchStart(touches);
      platform.pointer().onPointerStart(
        new Pointer.Event.Impl(time, pointerEvent.x(), pointerEvent.y(), true));
      break;
    case MotionEvent.ACTION_UP:
      platform.touch().onTouchEnd(touches);
      platform.pointer().onPointerEnd(
        new Pointer.Event.Impl(time, pointerEvent.x(), pointerEvent.y(), true));
      break;
    case MotionEvent.ACTION_POINTER_DOWN:
      platform.touch().onTouchStart(getChangedTouches(action, touches));
      break;
    case MotionEvent.ACTION_POINTER_UP:
      platform.touch().onTouchEnd(getChangedTouches(action, touches));
      break;
    case MotionEvent.ACTION_MOVE:
      platform.touch().onTouchMove(touches);
      platform.pointer().onPointerDrag(
        new Pointer.Event.Impl(time, pointerEvent.x(), pointerEvent.y(), true));
      break;
    // case MotionEvent.ACTION_CANCEL:
    //   break;
    // case MotionEvent.ACTION_OUTSIDE:
    //   break;
    }
  }

  private Touch.Event.Impl[] getChangedTouches(int action, Touch.Event.Impl[] touches) {
    int changed = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
      >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    return new Touch.Event.Impl[] { touches[changed] };
  }
}
