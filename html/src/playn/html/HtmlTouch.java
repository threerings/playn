/**
 * Copyright 2011 The PlayN Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package playn.html;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;

import playn.core.PlayN;
import playn.core.Touch;

class HtmlTouch extends HtmlInput implements Touch {
  private Listener listener;
  boolean inTouchSequence = false; // true when we are in a touch sequence (after touch start but before touch end)

  /**
   * Special implementation of Event.Impl that has a shared static preventDefault state.
   * <p>
   * Note: Call HtmlTouchEventImpl.init() before using to initialize the shared state.
   */
  static class HtmlTouchEventImpl extends Event.Impl {
    public static boolean preventDefault;

    public HtmlTouchEventImpl(double time, float x, float y, int id) {
      super(time, x, y, id);
    }

    @Override
    public void setPreventDefault(boolean preventDefault) {
      HtmlTouchEventImpl.preventDefault = preventDefault;
    }

    @Override
    public boolean getPreventDefault() {
      return HtmlTouchEventImpl.preventDefault;
    }

    /**
     * Initialize the shared state
     */
    public static void init() {
      preventDefault = false;
    }
  }

  HtmlTouch(final Element rootElement) {
    // capture touch start on the root element, only.
    captureEvent(rootElement, "touchstart", new EventHandler() {
      @Override
      public void handleEvent(NativeEvent nativeEvent) {
        if (listener != null) {
          JsArray<com.google.gwt.dom.client.Touch> nativeTouches = nativeEvent.getTouches();
          int nativeTouchesLen = nativeTouches.length();

          if (nativeTouchesLen == 0) {
            listener.onTouchStart(new Event[0]);
            return;
          }

          inTouchSequence = true;

          HtmlTouchEventImpl.init();

          // Convert the JsArray<Native Touch> to an array of Touch.Events
          Event[] touches = new Event[nativeTouchesLen];
          for (int t = 0; t < nativeTouchesLen; t++) {
            com.google.gwt.dom.client.Touch touch = nativeTouches.get(t);
            float x = touch.getRelativeX(rootElement);
            float y = touch.getRelativeY(rootElement);
            int id = getTouchIdentifier(nativeEvent, t);
            touches[t] = new HtmlTouchEventImpl(PlayN.currentTime(), x, y, id);
          }
          listener.onTouchStart(touches);
          if (HtmlTouchEventImpl.preventDefault) {
            nativeEvent.preventDefault();
          }
        }
      }
    });

    // capture touch end anywhere on the page as long as we are in a touch sequence
    capturePageEvent("touchend", new EventHandler() {
      @Override
      public void handleEvent(NativeEvent nativeEvent) {
        if (listener != null && inTouchSequence) {
          JsArray<com.google.gwt.dom.client.Touch> nativeTouches = nativeEvent.getTouches();
          int nativeTouchesLen = nativeTouches.length();

          HtmlTouchEventImpl.init();

          // Convert the JsArray<Native Touch> to an array of Touch.Events
          Event[] touches = new Event[nativeTouchesLen];
          for (int t = 0; t < nativeTouchesLen; t++) {
            com.google.gwt.dom.client.Touch touch = nativeTouches.get(t);
            float x = touch.getRelativeX(rootElement);
            float y = touch.getRelativeY(rootElement);
            int id = getTouchIdentifier(nativeEvent, t);
            touches[t] = new HtmlTouchEventImpl(PlayN.currentTime(), x, y, id);
          }
          listener.onTouchEnd(touches);
          if (HtmlTouchEventImpl.preventDefault) {
            nativeEvent.preventDefault();
          }

          // ending a touch sequence
          inTouchSequence = false;
        }
      }
    });

    // capture touch move anywhere on the page as long as we are in a touch sequence
    capturePageEvent("touchmove", new EventHandler() {
      @Override
      public void handleEvent(NativeEvent nativeEvent) {
        if (listener != null && inTouchSequence) {
          JsArray<com.google.gwt.dom.client.Touch> nativeTouches = nativeEvent.getTouches();
          int nativeTouchesLen = nativeTouches.length();

          HtmlTouchEventImpl.init();

          // Convert the JsArray<Native Touch> to an array of Touch.Events
          Event[] touches = new Event[nativeTouchesLen];
          for (int t = 0; t < nativeTouchesLen; t++) {
            com.google.gwt.dom.client.Touch touch = nativeTouches.get(t);
            float x = touch.getRelativeX(rootElement);
            float y = touch.getRelativeY(rootElement);
            int id = getTouchIdentifier(nativeEvent, t);
            touches[t] = new HtmlTouchEventImpl(PlayN.currentTime(), x, y, id);
          }
          listener.onTouchMove(touches);
          if (HtmlTouchEventImpl.preventDefault) {
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

  /**
   * Return the unique identifier of a touch, or 0
   * 
   * @return return the unique identifier of a touch, or 0
   */
  private static native int getTouchIdentifier(NativeEvent evt, int index) /*-{
    return evt.touches[index].identifier || 0;
  }-*/;
}
