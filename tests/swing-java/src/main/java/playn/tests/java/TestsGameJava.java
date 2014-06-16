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
package playn.tests.java;

import playn.core.Game;
import playn.core.PlayN;
import playn.java.JavaGraphics;
import playn.java.JavaJOGLPlatform;
import playn.java.JavaPlatform;
import playn.tests.core.TestsGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestsGameJava extends JFrame {

  TestsGame game;

  TestsGameJava() {
    super("PlayN Demo");
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int inset = 50;
    setBounds(inset, inset, screenSize.width - inset * 2,
            screenSize.height - inset * 2);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    JavaJOGLPlatform.register(new JavaJOGLPlatform.GameCreator() {
      @Override
      public Game createGame() {
        game = new TestsGame();
        return game;
      }
    });
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(final ComponentEvent e) {
        if(game != null) {
          PlayN.invokeLater(new Runnable() {
            @Override
            public void run() {
              ((JavaGraphics) PlayN.graphics()).setSize(e.getComponent().getWidth(), e.getComponent().getHeight(), false);
              game.displayMenu();
            }
          });
        }
      }
    });

    getContentPane().add(((JavaJOGLPlatform) PlayN.platform()).getPanel(), BorderLayout.CENTER);
  }

  public static void main(String[] args) {
    TestsGameJava app = new TestsGameJava();
    app.setSize(800, 600);
    app.setVisible(true);
  }
}
