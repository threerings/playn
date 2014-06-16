/**
 * Copyright 2014 The PlayN Authors
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

import playn.core.Mouse;

import javax.media.opengl.awt.GLJPanel;
import java.awt.event.*;

class JavaJOGLMouse extends JavaMouse {

  private final GLJPanel panel;
  private QueueEvent queueHead, queueTail;

  public JavaJOGLMouse(JavaPlatform platform, GLJPanel panel) {
    super(platform);
    this.panel = panel;
  }

  @Override
  void init() {
    JOGLMouseListener listener = new JOGLMouseListener();
    panel.addMouseListener(listener);
    panel.addMouseMotionListener(listener);
    panel.addMouseWheelListener(listener);
  }

  @Override
  void update() {
    if(queueHead == null) {
      return;
    }
    QueueEvent p = queueHead;
    queueHead = null;
    while (p != null) {
      switch (p.type) {
        case DOWN:
          onMouseDown(p.time, p.x, p.y, p.button);
          break;
        case UP:
          onMouseUp(p.time, p.x, p.y, p.button);
          break;
        case MOVE:
          onMouseMove(p.time, p.x, p.y, p.dx, p.dy);
          break;
        case WHEEL:
          onMouseWheelScroll(p.time, p.x, p.y, p.delta);
          break;
      }
      p = p.next;
    }
  }

  private class JOGLMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {

    private int x, y, dx, dy;
    boolean entered;

    @Override
    public void mouseClicked(MouseEvent e) {
            /* ignore */
    }

    @Override
    public void mousePressed(MouseEvent e) {
      updateDxDy(e);
      add(QueueEvent.down(platform.time(), x, y, getButton(e.getButton())));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      add(QueueEvent.up(platform.time(), x, y, getButton(e.getButton())));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      entered = true;
      x = e.getX();
      y = e.getY();
    }

    @Override
    public void mouseExited(MouseEvent e) {
      entered = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      updateDxDy(e);
      add(QueueEvent.move(platform.time(), x, y, dx, dy));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
      updateDxDy(e);
      add(QueueEvent.move(platform.time(), x, y, dx, dy));
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
      updateDxDy(e);
      add(QueueEvent.wheel(platform.time(), x, y, e.getWheelRotation() > 0 ? 1 : -1));
    }

    private void updateDxDy(MouseEvent e) {
      int newX = e.getX();
      int newY = e.getY();
      dx = newX - x;
      dy = newY - y;
      x = newX;
      y = newY;
    }

  }

  private void add(QueueEvent event) {
    if(queueHead == null) {
      queueHead = queueTail = event;
    } else {
      queueTail.next = event;
      queueTail = event;
    }
  }

  private static class QueueEvent {
    static enum Type {
      DOWN, UP, MOVE, WHEEL
    }

    QueueEvent next;

    Type type;
    double time;
    float x, y, dx, dy;
    int button;
    int delta;

    static QueueEvent up(double time, float x, float y, int button) {
      QueueEvent event = new QueueEvent();
      event.type = Type.UP;
      event.time = time;
      event.x = x;
      event.y = y;
      event.button = button;
      return event;
    }

    static QueueEvent down(double time, float x, float y, int button) {
      QueueEvent event = new QueueEvent();
      event.type = Type.DOWN;
      event.time = time;
      event.x = x;
      event.y = y;
      event.button = button;
      return event;
    }

    static QueueEvent move(double time, float x, float y, float dx, float dy) {
      QueueEvent event = new QueueEvent();
      event.type = Type.MOVE;
      event.time = time;
      event.x = x;
      event.y = y;
      event.dx = dx;
      event.dy = dy;
      return event;
    }

    static QueueEvent wheel(double time, float x, float y, int delta) {
      QueueEvent event = new QueueEvent();
      event.type = Type.WHEEL;
      event.time = time;
      event.x = x;
      event.y = y;
      event.delta = delta;
      return event;
    }
  }

  private static int getButton(int b) {
    switch (b) {
      case MouseEvent.BUTTON1:
        return Mouse.BUTTON_LEFT;
      case MouseEvent.BUTTON2:
        return Mouse.BUTTON_MIDDLE;
      case MouseEvent.BUTTON3:
        return Mouse.BUTTON_RIGHT;
    }
    return Mouse.BUTTON_LEFT;
  }
}
