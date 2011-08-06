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
package playn.sample.noise.core;

import playn.core.PlayN;
import playn.core.Game;
import playn.core.Sound;

public class NoiseGame implements Game {

  private Sound[] sounds = new Sound[45];
  private int count;
  private Sound bgSound;

  @Override
  public void init() {
    for (int i = 0; i < sounds.length; i++) {
      PlayN.log().info("Create sound " + i + "...");
      // Currently:
      // - Java Platform will search for freesoundproject_28917__junggle__btn107.wav
      // - HTML Platform will search for freesoundproject_28917__junggle__btn107.mp3
      sounds[i] = PlayN.assetManager().getSound("freesoundproject_28917__junggle__btn107");
      sounds[i].setVolume(.8f);
    }

    bgSound = PlayN.assetManager().getSound("freesoundproject_12742__Leady__reverse_fill_effect");
    bgSound.setLooping(true);
    bgSound.play();
  }

  @Override
  public void update(float delta) {
    count++;

    // PlayN.log().info("update(" + count + ")");
    if (count <= 3) {
      playSound(count % sounds.length);
    }
  }

  private void playSound(int idx) {
    // PlayN.log().info("play " + idx + "...");
    sounds[idx].play();
  }

  @Override
  public void paint(float alpha) {
  }

  @Override
  public int updateRate() {
    return 500;
  }

}
