/**
 * Copyright 2011 The ForPlay Authors
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
package forplay.sample.noise.core;

import forplay.core.ForPlay;
import forplay.core.Game;
import forplay.core.Sound;

public class NoiseGame implements Game {

  private Sound[] sounds = new Sound[45];
  private int count;
  private Sound bgSound;

  @Override
  public void init() {
    for (int i = 0; i < sounds.length; i++) {
      ForPlay.log().info("Create sound " + i + "...");
      // Currently:
      // - Java Platform will search for freesoundproject_28917__junggle__btn107.wav
      // - HTML Platform will search for freesoundproject_28917__junggle__btn107.mp3
      sounds[i] = ForPlay.assetManager().getSound("freesoundproject_28917__junggle__btn107");
      sounds[i].setVolume(.8f);
    }

    bgSound = ForPlay.assetManager().getSound("freesoundproject_12742__Leady__reverse_fill_effect");
    bgSound.setLooping(true);
    bgSound.play();
  }

  @Override
  public void update(float delta) {
    count++;

    // ForPlay.log().info("update(" + count + ")");
    if (count <= 3) {
      playSound(count % sounds.length);
    }
  }

  private void playSound(int idx) {
    // ForPlay.log().info("play " + idx + "...");
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
