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
package playn.java;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import pythagoras.f.Point;

import playn.core.MouseImpl;
import playn.core.PlayN;

class JavaMouse extends MouseImpl {

  private final JavaGraphics graphics;

  public JavaMouse(JavaGraphics graphics) {
    this.graphics = graphics;
  }

  @Override
  public void lock() {
    Mouse.setGrabbed(true);
  }

  @Override
  public void unlock() {
    Mouse.setGrabbed(false);
  }

  @Override
  public boolean isLocked() {
    return Mouse.isGrabbed();
  }

  @Override
  public boolean isLockSupported() {
    return true;
  }

  void init() throws LWJGLException {
    Mouse.create();
  }

  void update() {
    JavaPointer pointer = (JavaPointer) PlayN.pointer();

    while (Mouse.next()) {
      double time = (double) (Mouse.getEventNanoseconds() / 1000000);
      int btn = getButton(Mouse.getEventButton());
      Point m = new Point(Mouse.getEventX(), Display.getHeight() - Mouse.getEventY() - 1);
      graphics.transformMouse(m);

      int dx = Mouse.getEventDX(), dy = -Mouse.getEventDY();
      if (btn != -1) {
        if (Mouse.getEventButtonState()) {
          onMouseDown(new ButtonEvent.Impl(time, m.x, m.y, btn));
          pointer.onMouseDown(time, m.x, m.y);
        } else {
          onMouseUp(new ButtonEvent.Impl(time, m.x, m.y, btn));
          pointer.onMouseUp(time, m.x, m.y);
        }
      } else if (Mouse.getEventDWheel() != 0) {
        onMouseWheelScroll(new WheelEvent.Impl(time, Mouse.getEventDWheel()));
      } else {
        MotionEvent.Impl motionEvent = new MotionEvent.Impl(time, m.x, m.y, dx, dy);
        onMouseMove(motionEvent);
        onMouseOut(motionEvent);
        onMouseOver(motionEvent);
        pointer.onMouseMove(time, m.x, m.y);
      }
    }
  }

  protected static int getButton(int lwjglButton) {
    switch (lwjglButton) {
    case 0:  return BUTTON_LEFT;
    case 2:  return BUTTON_MIDDLE;
    case 1:  return BUTTON_RIGHT;
    default: return lwjglButton;
    }
  }
}
