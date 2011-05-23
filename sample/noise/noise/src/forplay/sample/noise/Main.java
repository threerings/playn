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
package forplay.sample.noise;

import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Main {

  private static final boolean USE_JLAYER = false;

  public static void main(String[] args) throws InterruptedException {
    for (AudioFileFormat.Type fileFormat : AudioSystem.getAudioFileTypes()) {
      System.out.print(fileFormat + ", ");
    }
    System.out.println();

    System.out.println();
    for (Info info : AudioSystem.getMixerInfo()) {
      System.out.println(info);
    }
    System.out.println();

    Mixer mixer = AudioSystem.getMixer(null);
    int maxLines = mixer.getMaxLines(mixer.getLineInfo());
    System.out.println("maxlines=" + maxLines);
    Thread.sleep(100);

    play("35631__reinsamba__crystal_glass.wav");
    play("9874__vixuxx__crow.au");
    play("9874__vixuxx__crow.aiff");
    play("Bird_Black_Ready1.wav");
    play("Bird_Black_Clicked1.mp3");
    play("ambient_construction.mp3");
    play("28917__junggle__btn107.mp3");
    play("forty-two.mp3");
    Thread.sleep(1000);
    System.out.println("Done");
  }

  private static void play(String filename) {
    try {
      final InputStream fis = Main.class.getResourceAsStream("/" + filename);
      if (USE_JLAYER) {
        Runnable r = new Runnable() {
          @Override
          public void run() {
            Player player;
            try {
              player = new Player(fis);
              player.play();
            } catch (JavaLayerException e) {
              e.printStackTrace();
            }
          }
        };
        new Thread(r).start();
      } else {
        System.out.println(filename);
        System.out.println(AudioSystem.getAudioFileFormat(fis).getFormat().toString());

        AudioInputStream ais = AudioSystem.getAudioInputStream(fis);
        Clip clip = AudioSystem.getClip();
        clip.addLineListener(new LineListener() {

          @Override
          public void update(LineEvent evt) {
            // System.out.println(evt.getType() + ":" +
            // evt.getLine());
          }
        });

        clip.open(ais);
        // for (int i = 0; i < 20; i++) {
        clip.start();
        // Thread.sleep(100);
        // clip.stop();
        // clip.setFramePosition(0);
        // }
        // clip.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
