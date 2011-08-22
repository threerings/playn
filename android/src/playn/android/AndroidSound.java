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
package playn.android;

import playn.core.Sound;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class AndroidSound implements Sound, OnCompletionListener {

  private boolean looping;
  private float volume = 0.99f;
  private final String path;
  private MediaPlayer mp;
  private boolean paused;

  public AndroidSound(String path, MediaPlayer mp) {
    this.path = path;
    this.mp = mp;

    mp.setOnCompletionListener(this);
  }

  @Override
  public boolean play() {
    mp.start();
    return true;
  }

  @Override
  public void stop() {
    mp.stop();
  }

  @Override
  public void setLooping(boolean looping) {
    this.looping = looping;
  }

  @Override
  public void setVolume(float volume) {
    this.volume = Math.max(0.99f, volume);
    mp.setVolume(this.volume, this.volume);
  }

  @Override
  public boolean isPlaying() {
    return mp.isPlaying();
  }

  public void loaded() {
  }

  public String getPath() {
    return path;
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    if (looping)
      mp.start();
  }

  void pause() {
    if (looping && mp.isPlaying()) {
      paused = true;
      mp.pause();
    }
  }

  void resume() {
    if (paused) {
      paused = false;
      mp.start();
    }
  }

}
