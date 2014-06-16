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

import com.jogamp.opengl.util.FPSAnimator;
import playn.core.*;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;

public class JavaJOGLPlatform implements Platform {

  public static interface GameCreator {
    Game createGame();
  }

  private static JavaJOGLPlatform singleInstance;

  private final GLJPanel panel;
  private final FPSAnimator animator;

  private JavaPlatform wrappedPlatform;
  private GLAutoDrawable glAutoDrawable;
  private Game game;

  public static JavaJOGLPlatform register(GameCreator gameCreator) {
    return register(new JavaPlatform.Config(), gameCreator);
  }

  public static JavaJOGLPlatform register(JavaPlatform.Config config, GameCreator gameCreator) {
    if(singleInstance != null) {
      throw new RuntimeException("only single instance allowed");
    }
    singleInstance = new JavaJOGLPlatform(config, gameCreator);
    return singleInstance;
  }

  JavaJOGLPlatform(final JavaPlatform.Config config, final GameCreator gameCreator) {
    panel = new GLJPanel();
    animator = new FPSAnimator(panel, 60);
    PlayN.setPlatform(this);
    panel.addGLEventListener(new GLEventListener() {
      @Override
      public void init(GLAutoDrawable drawable) {
        glAutoDrawable = drawable;
        wrappedPlatform = new JavaPlatformImpl(config);
        game = gameCreator.createGame();
        wrappedPlatform.init(game);
        animator.start();
      }

      @Override
      public void dispose(GLAutoDrawable drawable) {
      }

      @Override
      public void display(GLAutoDrawable drawable) {
        wrappedPlatform.processFrame(game);
      }

      @Override
      public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();

        GLU glu = new GLU();
        glu.gluOrtho2D(0.0f, width, 0.0f, height);

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();

        gl2.glViewport(0, 0, width, height);
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

        graphics().setSize(width, height, false);
      }
    });
  }

  public GLJPanel getPanel() {
    return panel;
  }

  @Override
  public Type type() {
    return Type.JAVA;
  }

  @Override
  public void run(Game game) {
    /* ignore */
  }

  @Override
  public void reportError(String message, Throwable cause) {
    wrappedPlatform.reportError(message, cause);
  }

  @Override
  public double time() {
    return wrappedPlatform.time();
  }

  @Override
  public int tick() {
    return wrappedPlatform.tick();
  }

  @Override
  public float random() {
    return wrappedPlatform.random();
  }

  @Override
  public void openURL(String url) {
    wrappedPlatform.openURL(url);
  }

  @Override
  public void invokeLater(Runnable runnable) {
    wrappedPlatform.invokeLater(runnable);
  }

  @Override
  public void setLifecycleListener(PlayN.LifecycleListener listener) {
    wrappedPlatform.setLifecycleListener(listener);
  }

  @Override
  public void setErrorReporter(PlayN.ErrorReporter reporter) {
    wrappedPlatform.setErrorReporter(reporter);
  }

  @Override
  public void setPropagateEvents(boolean propagate) {
    wrappedPlatform.setPropagateEvents(propagate);
  }

  @Override
  public Audio audio() {
    return wrappedPlatform.audio();
  }

  @Override
  public JavaGraphics graphics() {
    return wrappedPlatform.graphics();
  }

  @Override
  public Assets assets() {
    return wrappedPlatform.assets();
  }

  @Override
  public Json json() {
    return wrappedPlatform.json();
  }

  @Override
  public Keyboard keyboard() {
    return wrappedPlatform.keyboard();
  }

  @Override
  public Log log() {
    return wrappedPlatform.log();
  }

  @Override
  public Net net() {
    return wrappedPlatform.net();
  }

  @Override
  public Pointer pointer() {
    return wrappedPlatform.pointer();
  }

  @Override
  public Mouse mouse() {
    return wrappedPlatform.mouse();
  }

  @Override
  public Touch touch() {
    return wrappedPlatform.touch();
  }

  @Override
  public Storage storage() {
    return wrappedPlatform.storage();
  }

  private class JavaPlatformImpl extends JavaPlatform {

    public JavaPlatformImpl(Config config) {
      super(config);
    }

    @Override
    protected JavaGraphics createGraphics(Config config) {
      return new JavaGraphicsImpl(this, config);
    }

    @Override
    protected TouchImpl createTouch(Config config) {
      if (config.emulateTouch) {
        return new JavaJOGLEmulatedTouch(panel);
      } else {
        return new TouchStub();
      }
    }

    @Override
    protected JavaMouse createMouse() {
      return new JavaJOGLMouse(this, panel);
    }

    @Override
    protected JavaKeyboard createKeyboard() {
      return new JavaJOGLKeyboard(panel);
    }

    @Override
    protected void unpackNatives() {
      /* nothing to do */
    }

    @Override
    public void run(Game game) {
      /* ignore */
    }
  }

  private class JavaGraphicsImpl extends JavaGraphics {

    public JavaGraphicsImpl(JavaPlatform platform, JavaPlatform.Config config) {
      super(platform, config);
    }

    @Override
    protected JavaGLContext createJavaGLContext(JavaPlatform platform, float scaleFactor) {
      GL2 gl2 = glAutoDrawable.getGL().getGL2();
      return new JavaGLContext(platform, new JavaJOGL20(gl2), scaleFactor);
    }

    @Override
    protected void init() {
      ctx.setSize(ctx.scale.invScaledFloor(glAutoDrawable.getWidth()),
              ctx.scale.invScaledFloor(glAutoDrawable.getHeight()));
      ctx.init();
    }

    @Override
    public int screenHeight() {
      return ctx.scale.invScaledFloor(glAutoDrawable.getWidth());
    }

    @Override
    public int screenWidth() {
      return ctx.scale.invScaledFloor(glAutoDrawable.getWidth());
    }
  }
}
