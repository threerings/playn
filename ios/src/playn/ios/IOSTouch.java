/**
 * Copyright 2012 The PlayN Authors
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
package playn.ios;

import cli.System.Drawing.PointF;
import cli.System.Convert;

import cli.MonoTouch.Foundation.NSObject;
import cli.MonoTouch.Foundation.NSSet;
import cli.MonoTouch.Foundation.NSSetEnumerator;
import cli.MonoTouch.UIKit.UIEvent;
import cli.MonoTouch.UIKit.UITouch;

import pythagoras.f.IPoint;

import playn.core.Events;
import playn.core.TouchImpl;

public class IOSTouch extends TouchImpl {

  private final IOSGraphics graphics;

  public IOSTouch(IOSPlatform platform) {
    super(platform);
    this.graphics = platform.graphics();
  }

  void onTouchesBegan(NSSet touches, UIEvent event) {
    onTouchStart(toTouchEvents(touches, event));
  }

  void onTouchesMoved(NSSet touches, UIEvent event) {
    onTouchMove(toTouchEvents(touches, event));
  }

  void onTouchesEnded(NSSet touches, UIEvent event) {
    onTouchEnd(toTouchEvents(touches, event));
  }

  void onTouchesCancelled(NSSet touches, UIEvent event) {
    onTouchCancel(toTouchEvents(touches, event));
  }

  private Event.Impl[] toTouchEvents(NSSet touches, UIEvent event) {
    final Event.Impl[] events = new Event.Impl[Convert.ToInt32(touches.get_Count())];
    touches.Enumerate(new NSSetEnumerator(new NSSetEnumerator.Method() {
      public void Invoke (NSObject obj, boolean[] stop) {
        UITouch touch = (UITouch) obj;
        PointF loc = touch.LocationInView(touch.get_View());
        // transform the point based on our current scale
        IPoint xloc = graphics.transformTouch(loc.get_X(), loc.get_Y());
        // on iOS the memory address of the UITouch object is the unique id
        int id = touch.get_Handle().ToInt32();
        events[_idx++] = new Event.Impl(
          new Events.Flags.Impl(), touch.get_Timestamp() * 1000, xloc.x(), xloc.y(), id);
        stop[0] = false;
      }
      private int _idx = 0;
    }));
    return events;
  }
}
