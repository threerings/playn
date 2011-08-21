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
package playn.showcase.core;

import java.util.ArrayList;
import java.util.List;

import playn.core.Game;
import playn.core.Keyboard;
import playn.core.PlayN;

import playn.showcase.core.peas.PeasDemo;
import playn.showcase.core.sprites.SpritesDemo;
import playn.showcase.core.swirl.SwirlDemo;

/**
 * The main entry point for the showcase "game".
 */
public class Showcase implements Game
{
  private Demo activeDemo;
  private Demo menuDemo = new Menu(this);

  public List<Demo> demos = new ArrayList<Demo>(); {
    // add your demo here to enable it in the showcase
    demos.add(new SpritesDemo());
    demos.add(new PeasDemo());
    demos.add(new SwirlDemo());
  }

  public void activateDemo(Demo demo) {
    if (activeDemo != null) {
      activeDemo.shutdown();
    }
    activeDemo = demo;
    activeDemo.init();
  }

  @Override
  public void init() {
    PlayN.keyboard().setListener(new Keyboard.Adapter() {
      public void onKeyDown(Keyboard.Event event) {
        if (event.keyCode() == Keyboard.KEY_ESC) {
          activateDemo(menuDemo);
        } else {
          Keyboard.Listener delegate = activeDemo.keyboardListener();
          if (delegate != null) {
            delegate.onKeyDown(event);
          }
        }
      }

      public void onKeyUp(Keyboard.Event event) {
        Keyboard.Listener delegate = activeDemo.keyboardListener();
        if (delegate != null) {
          delegate.onKeyUp(event);
        }
      }
    });

    activateDemo(menuDemo);
  }

  @Override
  public void update(float delta) {
    activeDemo.update(delta);
  }

  @Override
  public void paint(float alpha) {
    activeDemo.paint(alpha);
  }

  @Override
  public int updateRate() {
    return 25;
  }
}
