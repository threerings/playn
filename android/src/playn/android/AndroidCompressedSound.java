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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.media.MediaPlayer;

public class AndroidCompressedSound extends AndroidSound {
  private File cachedFile;
  private boolean paused, loaded, playOnLoad;
  private float volume = 0.99f;
  private final MediaPlayer mp;

  public AndroidCompressedSound(InputStream in, String extension) throws IOException {
    cachedFile = new File(AndroidPlatform.instance.activity.getFilesDir(), "sound-" + Integer.toHexString(hashCode())
        + extension);
    try {
      FileOutputStream out = new FileOutputStream(cachedFile);
      try {
        byte[] buffer = new byte[16 * 1024];
        while (true) {
          int r = in.read(buffer);
          if (r < 0)
            break;
          out.write(buffer, 0, r);
        }
      } finally {
        out.close();
      }
    } finally {
      in.close();
    }

    FileInputStream ins = new FileInputStream(cachedFile);

    try {
      mp = new MediaPlayer();
      mp.setDataSource(ins.getFD());
      mp.setOnPreparedListener(new SoundPreparedListener(this));
      mp.prepareAsync();
    } finally {
      ins.close();
    }
  }

  @Override
  public boolean play() {
    if (!loaded) playOnLoad = true;
    else {
      if (paused) mp.stop(); //Restart the sound if paused to differentiate from resume()
      mp.start();
    }
    return true;
  }

  @Override
  public void stop() {
    mp.stop();
  }

  @Override
  public void setLooping(boolean looping) {
    mp.setLooping(looping);
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

  @Override
  void pause() {
    if (!paused) {
      if (mp.isPlaying()) mp.pause();
      paused = true;
    }
  }

  @Override
  void resume() {
    if (paused) {
      mp.start();
      paused = false;
    }
  }

  void loaded() {
    loaded = true;
    if (playOnLoad) {
      play();
    }
  }

  @Override
  public void finalize() {
    cachedFile.delete();
  }

  private class SoundPreparedListener implements MediaPlayer.OnPreparedListener {
    AndroidCompressedSound sound;

    SoundPreparedListener(AndroidCompressedSound sound) {
      this.sound = sound;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (sound != null) sound.loaded();
    }
  }
}
