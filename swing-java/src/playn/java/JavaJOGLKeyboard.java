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

import playn.core.Key;
import playn.core.util.Callback;

import javax.media.opengl.awt.GLJPanel;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class JavaJOGLKeyboard extends JavaKeyboard {

  private final GLJPanel panel;

  JavaJOGLKeyboard(GLJPanel panel) {
    this.panel = panel;
  }

  @Override
  public void getText(TextType textType, String label, String initialValue, Callback<String> callback) {
    Object result = JOptionPane.showInputDialog(
            panel, label, "", JOptionPane.QUESTION_MESSAGE, null, null, initialValue);
    callback.onSuccess((String) result);
  }

  @Override
  void init(Listener platformListener) {
    panel.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        post(translateKey(e.getKeyCode()), true, e.getKeyChar());
      }

      @Override
      public void keyReleased(KeyEvent e) {
        post(translateKey(e.getKeyCode()), false, e.getKeyChar());
      }
    });
    super.init(platformListener);
  }

  private static Key translateKey(int keyCode) {
    switch (keyCode) {
      case KeyEvent.VK_ESCAPE       : return Key.ESCAPE;
      case KeyEvent.VK_1            : return Key.K1;
      case KeyEvent.VK_2            : return Key.K2;
      case KeyEvent.VK_3            : return Key.K3;
      case KeyEvent.VK_4            : return Key.K4;
      case KeyEvent.VK_5            : return Key.K5;
      case KeyEvent.VK_6            : return Key.K6;
      case KeyEvent.VK_7            : return Key.K7;
      case KeyEvent.VK_8            : return Key.K8;
      case KeyEvent.VK_9            : return Key.K9;
      case KeyEvent.VK_0            : return Key.K0;
      case KeyEvent.VK_MINUS        : return Key.MINUS;
      case KeyEvent.VK_EQUALS       : return Key.EQUALS;
      case KeyEvent.VK_BACK_SPACE   : return Key.BACK;
      case KeyEvent.VK_TAB          : return Key.TAB;
      case KeyEvent.VK_Q            : return Key.Q;
      case KeyEvent.VK_W            : return Key.W;
      case KeyEvent.VK_E            : return Key.E;
      case KeyEvent.VK_R            : return Key.R;
      case KeyEvent.VK_T            : return Key.T;
      case KeyEvent.VK_Y            : return Key.Y;
      case KeyEvent.VK_U            : return Key.U;
      case KeyEvent.VK_I            : return Key.I;
      case KeyEvent.VK_O            : return Key.O;
      case KeyEvent.VK_P            : return Key.P;
      case KeyEvent.VK_OPEN_BRACKET : return Key.LEFT_BRACKET;
      case KeyEvent.VK_CLOSE_BRACKET: return Key.RIGHT_BRACKET;
      case KeyEvent.VK_ENTER        : return Key.ENTER;
      case KeyEvent.VK_A            : return Key.A;
      case KeyEvent.VK_S            : return Key.S;
      case KeyEvent.VK_D            : return Key.D;
      case KeyEvent.VK_F            : return Key.F;
      case KeyEvent.VK_G            : return Key.G;
      case KeyEvent.VK_H            : return Key.H;
      case KeyEvent.VK_J            : return Key.J;
      case KeyEvent.VK_K            : return Key.K;
      case KeyEvent.VK_L            : return Key.L;
      case KeyEvent.VK_SEMICOLON    : return Key.SEMICOLON;
      case KeyEvent.VK_QUOTE        : return Key.QUOTE;
      case KeyEvent.VK_BACK_QUOTE   : return Key.BACKQUOTE;
      case KeyEvent.VK_BACK_SLASH   : return Key.BACKSLASH;
      case KeyEvent.VK_Z            : return Key.Z;
      case KeyEvent.VK_X            : return Key.X;
      case KeyEvent.VK_C            : return Key.C;
      case KeyEvent.VK_V            : return Key.V;
      case KeyEvent.VK_B            : return Key.B;
      case KeyEvent.VK_N            : return Key.N;
      case KeyEvent.VK_M            : return Key.M;
      case KeyEvent.VK_COMMA        : return Key.COMMA;
      case KeyEvent.VK_PERIOD       : return Key.PERIOD;
      case KeyEvent.VK_SLASH        : return Key.SLASH;
      case KeyEvent.VK_SHIFT        : return Key.SHIFT;
      case KeyEvent.VK_MULTIPLY     : return Key.MULTIPLY;
      case KeyEvent.VK_WINDOWS      : return Key.ALT; // PlayN doesn't know left v. right
      case KeyEvent.VK_SPACE        : return Key.SPACE;
      case KeyEvent.VK_CAPS_LOCK    : return Key.CAPS_LOCK;
      case KeyEvent.VK_F1           : return Key.F1;
      case KeyEvent.VK_F2           : return Key.F2;
      case KeyEvent.VK_F3           : return Key.F3;
      case KeyEvent.VK_F4           : return Key.F4;
      case KeyEvent.VK_F5           : return Key.F5;
      case KeyEvent.VK_F6           : return Key.F6;
      case KeyEvent.VK_F7           : return Key.F7;
      case KeyEvent.VK_F8           : return Key.F8;
      case KeyEvent.VK_F9           : return Key.F9;
      case KeyEvent.VK_F10          : return Key.F10;
      case KeyEvent.VK_NUM_LOCK     : return Key.NP_NUM_LOCK;
      case KeyEvent.VK_SCROLL_LOCK  : return Key.SCROLL_LOCK;
      case KeyEvent.VK_NUMPAD7      : return Key.NP7;
      case KeyEvent.VK_NUMPAD8      : return Key.NP8;
      case KeyEvent.VK_NUMPAD9      : return Key.NP9;
      case KeyEvent.VK_SUBTRACT     : return Key.NP_SUBTRACT;
      case KeyEvent.VK_NUMPAD4      : return Key.NP4;
      case KeyEvent.VK_NUMPAD5      : return Key.NP5;
      case KeyEvent.VK_NUMPAD6      : return Key.NP6;
      case KeyEvent.VK_ADD          : return Key.NP_ADD;
      case KeyEvent.VK_NUMPAD1      : return Key.NP1;
      case KeyEvent.VK_NUMPAD2      : return Key.NP2;
      case KeyEvent.VK_NUMPAD3      : return Key.NP3;
      case KeyEvent.VK_NUMPAD0      : return Key.NP0;
      case KeyEvent.VK_DECIMAL      : return Key.NP_DECIMAL;
      case KeyEvent.VK_F11          : return Key.F11;
      case KeyEvent.VK_F12          : return Key.F12;
      //case KeyEvent.VK_F13          : return Key.F13;
      //case KeyEvent.VK_F14          : return Key.F14;
      //case KeyEvent.VK_F15          : return Key.F15;
      //case KeyEvent.VK_F16          : return Key.F16;
      //case KeyEvent.VK_F17          : return Key.F17;
      //case KeyEvent.VK_F18          : return Key.F18;
      //case KeyEvent.VK_KANA         : return Key.
      //case KeyEvent.VK_F19          : return Key.F19;
      //case KeyEvent.VK_CONVERT      : return Key.
      //case KeyEvent.VK_NOCONVERT    : return Key.
      //case KeyEvent.VK_YEN          : return Key.
      //case KeyEvent.VK_NUMPADEQUALS : return Key.
      case KeyEvent.VK_CIRCUMFLEX   : return Key.CIRCUMFLEX;
      case KeyEvent.VK_AT           : return Key.AT;
      case KeyEvent.VK_COLON        : return Key.COLON;
      case KeyEvent.VK_UNDERSCORE   : return Key.UNDERSCORE;
      //case KeyEvent.VK_KANJI        : return Key.
      //case KeyEvent.VK_STOP         : return Key.
      //case KeyEvent.VK_AX           : return Key.
      //case KeyEvent.VK_UNLABELED    : return Key.
      //case KeyEvent.VK_NUMPADENTER  : return Key.
      case KeyEvent.VK_CONTROL      : return Key.CONTROL;
      //case KeyEvent.VK_SECTION      : return Key.
      //case KeyEvent.VK_NUMPADCOMMA  : return Key.
      //case KeyEvent.VK_DIVIDE       :
      //case KeyEvent.VK_SYSRQ        : return Key.SYSRQ;
      case KeyEvent.VK_CONTEXT_MENU : return Key.ALT;
      //case KeyEvent.VK_FUNCTION     : return Key.FUNCTION;
      case KeyEvent.VK_PAUSE        : return Key.PAUSE;
      case KeyEvent.VK_HOME         : return Key.HOME;
      case KeyEvent.VK_UP           : return Key.UP;
      case KeyEvent.VK_PAGE_UP      : return Key.PAGE_UP;
      case KeyEvent.VK_LEFT         : return Key.LEFT;
      case KeyEvent.VK_RIGHT        : return Key.RIGHT;
      case KeyEvent.VK_END          : return Key.END;
      case KeyEvent.VK_DOWN         : return Key.DOWN;
      case KeyEvent.VK_PAGE_DOWN    : return Key.PAGE_DOWN;
      case KeyEvent.VK_INSERT       : return Key.INSERT;
      case KeyEvent.VK_DELETE       : return Key.DELETE;
      case KeyEvent.VK_CLEAR        : return Key.CLEAR;
      case KeyEvent.VK_META         : return Key.META;
      //case KeyEvent.VK_LWIN         : return Key.WINDOWS; // Duplicate with VK_LMETA
      //case KeyEvent.VK_RWIN         : return Key.WINDOWS; // Duplicate with VK_RMETA
      //case KeyEvent.VK_APPS         : return Key.
      //case KeyEvent.VK_POWER        : return Key.POWER;
      //case KeyEvent.VK_SLEEP        : return Key.
    }

    return null;
  }
}
