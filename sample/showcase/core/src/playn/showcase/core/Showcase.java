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

import playn.showcase.core.sprites.SpritesDemo;

/**
 * The main entry point for the showcase "game".
 */
public class Showcase implements Game
{
  private Demo activeDemo;

  private List<Demo> demos = new ArrayList<Demo>(); {
    demos.add(new SpritesDemo());
  }

  @Override
  public void init() {
    activateDemo(demos.get(0));
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

  private void activateDemo(Demo demo) {
    if (activeDemo != null) {
      activeDemo.shutdown();
    }
    activeDemo = demo;
    activeDemo.init();
  }

  private void activateNextDemo() {
    int curIdx = demos.indexOf(activeDemo);
    int nextIdx = (curIdx + 1) % demos.size();
    activateDemo(demos.get(nextIdx));
  }
}
