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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL2ES2;

import playn.core.Asserts;
import playn.core.CanvasImage;
import playn.core.CanvasLayer;
import playn.core.Gradient;
import playn.core.Graphics;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Path;
import playn.core.Pattern;
import playn.core.SurfaceLayer;
import playn.core.Transform;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.View;

class AndroidGraphics implements Graphics {
  private static final int VERTEX_SIZE = 10; // 10 floats per vertex

  // TODO(jgw): Re-enable longer element buffers once we figure out why they're
  // causing weird
  // performance degradation.
  // private static final int MAX_VERTS = 400; // 100 quads
  // private static final int MAX_ELEMS = MAX_VERTS * 6 / 4; // At most 6 verts
  // per quad

  // These values allow only one quad at a time (there's no generalized polygon
  // rendering available
  // in Surface yet that would use more than 4 points / 2 triangles).
  private static final int MAX_VERTS = 4;
  private static final int MAX_ELEMS = 6;
  private static final int FLOAT_SIZE_BYTES = 4;
  private static final int INT_SIZE_BYTES = 4;
  
  private static int startingScreenWidth;
  private static int startingScreenHeight;
  
  private static AndroidAssetManager shaderAssetManager = new AndroidAssetManager();
  private static ShaderCallback shaderCallback = new ShaderCallback();

  private class Shader {
    int program, uScreenSizeLoc, aMatrix, aTranslation, aPosition, aTexture;
    FloatBuffer vertexBuffer;
    IntBuffer indexBuffer;

    // TODO(jonagill): Top method needs vertexData.position(0) called. Bottom
    // method might not work.
    FloatBuffer vertexData = ByteBuffer.allocateDirect(VERTEX_SIZE * MAX_VERTS * FLOAT_SIZE_BYTES).order(
        ByteOrder.nativeOrder()).asFloatBuffer();
    IntBuffer elementData = IntBuffer.wrap(new int[MAX_ELEMS]);

    int vertexOffset, elementOffset;

    Shader(String fragShaderName) {
      shaderAssetManager.getText(Shaders.vertexShader, shaderCallback);
      String vertexShader = shaderCallback.shader();
      shaderAssetManager.getText(fragShaderName, shaderCallback);
      String fragShader = shaderCallback.shader();
      Log.w("Shaders!", fragShader);
      program = createProgram(vertexShader, fragShader);
      
      // glGet*() calls are slow; determine locations once.
      uScreenSizeLoc = gfx.glGetUniformLocation(program, "u_ScreenSize");
      aMatrix = gfx.glGetAttribLocation(program, "a_Matrix");
      aTranslation = gfx.glGetAttribLocation(program, "a_Translation");
      aPosition = gfx.glGetAttribLocation(program, "a_Position");
      aTexture = gfx.glGetAttribLocation(program, "a_Texture");

      // Create the vertex and index buffers
      vertexBuffer = ByteBuffer.allocateDirect(MAX_VERTS * FLOAT_SIZE_BYTES).order(
          ByteOrder.nativeOrder()).asFloatBuffer();
      indexBuffer = ByteBuffer.allocateDirect(MAX_ELEMS * INT_SIZE_BYTES).order(
          ByteOrder.nativeOrder()).asIntBuffer();
      // TODO(jonagill): I think I calculated the size for these wrong. We'll
      // see.
    }

    boolean prepare() {
      if (useShader(this)) {
        gfx.glUseProgram(program);
        gfx.glUniform2fv(uScreenSizeLoc, 2,
            FloatBuffer.wrap(new float[] {screenWidth, screenHeight}));
        gfx.glBindBuffer(GL2ES2.GL_ARRAY_BUFFER, 0);
        gfx.glBindBuffer(GL2ES2.GL_ELEMENT_ARRAY_BUFFER, 0);

        // TODO(jonagill):MAKE SURE TO ADD BUFFERS!!
        //thisCodeWontWorkUntilIAddThose();

        gfx.glEnableVertexAttribArray(aMatrix);
        gfx.glEnableVertexAttribArray(aTranslation);
        gfx.glEnableVertexAttribArray(aPosition);
        if (aTexture != -1)
          gfx.glEnableVertexAttribArray(aTexture);

        gfx.glVertexAttribPointer(aMatrix, 4, GL2ES2.GL_FLOAT, false, 40, 0);
        gfx.glVertexAttribPointer(aTranslation, 2, GL2ES2.GL_FLOAT, false, 40, 16);
        gfx.glVertexAttribPointer(aPosition, 2, GL2ES2.GL_FLOAT, false, 40, 24);
        if (aTexture != -1)
          gfx.glVertexAttribPointer(aTexture, 2, GL2ES2.GL_FLOAT, false, 40, 32);

        return true;
      }
      return false;
    }

    void flush() {
      if (vertexOffset == 0) {
        return;
      }

      float[] vertexSubarray = new float[vertexOffset];
      int[] elementSubarray = new int[elementOffset];
      vertexData.get(vertexSubarray, 0, vertexOffset);
      elementData.get(elementSubarray, 0, elementOffset);
      vertexData.position(0);
      elementData.position(0);
      gfx.glBufferData(GL2ES2.GL_ARRAY_BUFFER, vertexOffset, FloatBuffer.wrap(vertexSubarray),
          GL2ES2.GL_STREAM_DRAW);
      gfx.glBufferData(GL2ES2.GL_ELEMENT_ARRAY_BUFFER, elementOffset,
          IntBuffer.wrap(elementSubarray), GL2ES2.GL_STREAM_DRAW);

      gfx.glDrawElements(GL2ES2.GL_TRIANGLES, elementOffset, GL2ES2.GL_UNSIGNED_SHORT, 0);
      vertexOffset = elementOffset = 0;
    }

    // Verbatim
    int beginPrimitive(int vertexCount, int elemCount) {
      int vertIdx = vertexOffset / VERTEX_SIZE;
      if ((vertIdx + vertexCount > MAX_VERTS) || (elementOffset + elemCount > MAX_ELEMS)) {
        flush();
        return 0;
      }
      return vertIdx;
    }

    void buildVertex(Transform local, float dx, float dy) {
      buildVertex(local, dx, dy, 0, 0);
    }

    void buildVertex(Transform local, float dx, float dy, float sx, float sy) {
      float[] vertexArray = vertexData.array();
      vertexArray[vertexOffset + 0] = local.m00();
      vertexArray[vertexOffset + 1] = local.m01();
      vertexArray[vertexOffset + 2] = local.m10();
      vertexArray[vertexOffset + 3] = local.m11();
      vertexArray[vertexOffset + 4] = local.tx();
      vertexArray[vertexOffset + 5] = local.ty();
      vertexArray[vertexOffset + 6] = dx;
      vertexArray[vertexOffset + 7] = dy;
      vertexArray[vertexOffset + 8] = sx;
      vertexArray[vertexOffset + 9] = sy;
      vertexOffset += VERTEX_SIZE;

    }

    void addElement(int index) {
      int[] elementArray = elementData.array();
      elementArray[elementOffset++] = index;
    }

    private int loadShader(int type, final String shaderSource) {
      int shader;

      // Create the shader object
      shader = gfx.glCreateShader(type);
      if (shader == 0)
        return 0;

      // Load the shader source
      gfx.glShaderSource(shader, shaderSource);

      // Compile the shader
      gfx.glCompileShader(shader);

      IntBuffer compiled = IntBuffer.allocate(1);
      gfx.glGetShaderiv(shader, GL2ES2.GL_COMPILE_STATUS, compiled);

      if (compiled.array()[0] == 0) { // Same as gfx.GL_FALSE
        Log.e(this.getClass().getName(), "Could not compile shader " + type + ":");
        Log.e(this.getClass().getName(), gfx.glGetShaderInfoLog(shader));
        gfx.glDeleteShader(shader);
        shader = 0;
      }

      return shader;
    }

    // Creates program object, attaches shaders, and links into pipeline
    protected int createProgram(String vertexSource, String fragmentSource) {
      // Load the vertex and fragment shaders
      int vertexShader = loadShader(GL2ES2.GL_VERTEX_SHADER, vertexSource);
      int fragmentShader = loadShader(GL2ES2.GL_FRAGMENT_SHADER, fragmentSource);
      // Create the program object
      int program = gfx.glCreateProgram();
      if (vertexShader == 0 || fragmentShader == 0 || program == 0)
        return 0;

      if (program != 0) {
        gfx.glAttachShader(program, vertexShader);
        checkGlError("glAttachShader");
        gfx.glAttachShader(program, fragmentShader);
        checkGlError("glAttachShader");
        gfx.glLinkProgram(program);
        IntBuffer linkStatus = IntBuffer.allocate(1);
        gfx.glGetProgramiv(program, GL2ES2.GL_LINK_STATUS, linkStatus);
        if (linkStatus.array()[0] != GL2ES2.GL_TRUE) {
          Log.e(this.getClass().getName(), "Could not link program: ");
          Log.e(this.getClass().getName(), gfx.glGetProgramInfoLog(program));
          gfx.glDeleteProgram(program);
          program = 0;
        }
      }
      return program;
    }
  }

  private class TextureShader extends Shader {
    int uTexture, uAlpha, lastTex;
    float lastAlpha;

    TextureShader() {
      super(Shaders.texFragmentShader);
      uTexture = gfx.glGetUniformLocation(program, "u_Texture");
      uAlpha = gfx.glGetUniformLocation(program, "u_Alpha");
    }

    @Override
    void flush() {
      gfx.glBindTexture(GL2ES2.GL_TEXTURE_2D, lastTex);
      super.flush();
    }

    void prepare(int tex, float alpha) {
      if (super.prepare()) {
        gfx.glActiveTexture(GL2ES2.GL_TEXTURE0);
        gfx.glUniform1i(uTexture, 0);
      }

      if (tex == lastTex && alpha == lastAlpha)
        return;
      flush();

      gfx.glUniform1f(uAlpha, alpha);
      lastAlpha = alpha;
      lastTex = tex;
    }
  }

  private class ColorShader extends Shader {
    int uColor, uAlpha, lastColor;
    FloatBuffer colors = FloatBuffer.allocate(4);
    float lastAlpha;

    ColorShader() {
      super(Shaders.colorFragmentShader);
      uColor = gfx.glGetUniformLocation(program, "u_Color");
      uAlpha = gfx.glGetUniformLocation(program, "u_Alpha");
    }

    void prepare(int color, float alpha) {
      super.prepare();

      if (color == lastColor && alpha == lastAlpha)
        return;
      flush();

      gfx.glUniform1f(uAlpha, alpha);
      lastAlpha = alpha;
      setColor(color);
    }

    private void setColor(int color) {
      float[] colorsArray = colors.array();
      colorsArray[3] = (float) ((color >> 24) & 0xff) / 255;
      colorsArray[0] = (float) ((color >> 16) & 0xff) / 255;
      colorsArray[1] = (float) ((color >> 8) & 0xff) / 255;
      colorsArray[2] = (float) ((color >> 0) & 0xff) / 255;
      gfx.glUniform4fv(uColor, colors.capacity(), colors);

      lastColor = color;
    }
  }

  protected final AndroidGL20 gfx;
  final AndroidGroupLayer rootLayer;
  private final GameViewGL gameView;
  private int width, height, lastFrameBuffer, screenWidth, screenHeight;
  private boolean sizeSetManually = false;

  // Shaders & Meshes
  private Shader curShader;
  private TextureShader texShader;
  private ColorShader colorShader;

  // Debug counter
  private int texCount;

  public AndroidGraphics(AndroidGL20 gfx) {
    this.gfx = gfx;
    gameView = AndroidPlatform.instance.activity.gameView();
    rootLayer = new AndroidGroupLayer(gfx);
    if (startingScreenWidth != 0) screenWidth = startingScreenWidth;
    if (startingScreenHeight != 0) screenHeight = startingScreenHeight;
//    
//    initGL();
//
//    shaderAssetManager.setPathPrefix(Shaders.pathPrefix);
//    texShader = new TextureShader();
//    colorShader = new ColorShader();
  }

  @Override
  public CanvasImage createImage(int w, int h) {
    return new AndroidImage(w, h, true);
  }

  public CanvasImage createImage(int w, int h, boolean alpha) {
    return new AndroidImage(w, h, alpha);
  }

  @Override
  public Gradient createLinearGradient(float x0, float y0, float x1, float y1, int[] colors,
      float[] positions) {
    LinearGradient gradient = new LinearGradient(x0, y0, x1, y1, colors, positions, TileMode.CLAMP);
    return new AndroidGradient(gradient);
  }

  @Override
  public Path createPath() {
    return new AndroidPath();
  }

  @Override
  public Pattern createPattern(Image img) {
    Asserts.checkArgument(img instanceof AndroidImage);
    Bitmap bitmap = ((AndroidImage) img).getBitmap();
    BitmapShader shader = new BitmapShader(bitmap, TileMode.REPEAT, TileMode.REPEAT);
    return new AndroidPattern(shader);
  }

  @Override
  public Gradient createRadialGradient(float x, float y, float r, int[] colors, float[] positions) {
    RadialGradient gradient = new RadialGradient(x, y, r, colors, positions, TileMode.CLAMP);
    return new AndroidGradient(gradient);
  }

  @Override
  public int screenHeight() {
    return screenHeight;
  }

  @Override
  public int screenWidth() {
    return screenWidth;
  }

  @Override
  public CanvasLayer createCanvasLayer(int width, int height) {
    return new AndroidCanvasLayer(gfx, width, height, true);
  }

  public CanvasLayer createCanvasLayer(int width, int height, boolean alpha) {
    return new AndroidCanvasLayer(gfx, width, height, alpha);
  }

  @Override
  public GroupLayer createGroupLayer() {
    return new AndroidGroupLayer(gfx);
  }

  @Override
  public ImageLayer createImageLayer() {
    return new AndroidImageLayer(gfx);
  }

  @Override
  public ImageLayer createImageLayer(Image image) {
    return new AndroidImageLayer(gfx, (AndroidImage) image);
  }

  @Override
  public SurfaceLayer createSurfaceLayer(int width, int height) {
    return new AndroidSurfaceLayer(gfx, width, height);
  }

  @Override
  public int height() {
    return height;
  }

  @Override
  public GroupLayer rootLayer() {
    return rootLayer;
  }

  void refreshScreenSize(boolean resize) {
    View viewLayout = AndroidPlatform.instance.activity.viewLayout();
    int oldWidth = screenWidth;
    int oldHeight = screenHeight;
    screenWidth = viewLayout.getWidth();
    screenHeight = viewLayout.getHeight();
    AndroidPlatform.instance.touchEventHandler().calculateOffsets();
    //Change game size to fill the screen if it's never been set manually.
    if (resize && !sizeSetManually && (screenWidth != oldWidth || screenHeight != oldHeight))
      setSize(screenWidth, screenHeight, false);
  }
  
  public void refreshScreenSize() {
    refreshScreenSize(true);
  }
  
  /**
   * Public manual setSize function.  Once this is
   * called, automatic calls to refreshScreenSize()
   * when something changes the size of the gameView
   * will not force a call to setSize.
   */
  @Override
  public void setSize(int width, int height) {
    setSize(width, height, true);
  }
  
  private void setSize(int width, int height, boolean manual) {
    if (manual) sizeSetManually = true; 
    if (!gameView.gameSizeSet) gameView.gameSizeSet = true;
    this.width = width;
    this.height = height;
    AndroidPlatform.instance.touchEventHandler().calculateOffsets();
    //Layout the views again to change the surface size
    AndroidPlatform.instance.activity.runOnUiThread(new Runnable() {
      public void run() {
        View viewLayout = AndroidPlatform.instance.activity.viewLayout();
        viewLayout.measure(viewLayout.getMeasuredWidth(), viewLayout.getMeasuredHeight());
        viewLayout.requestLayout();
      }
    });
    //TODO: ???
//    bindFrameBuffer(-1, width, height, true);
  }

  @Override
  public int width() {
    return width;
  }
  
  /**
   * Called by AndroidViewLayout to make sure that
   * AndroidGraphics is initialized with non-zero
   * screen dimensions.
   * @param width
   * @param height
   */
  static void setStartingScreenSize(int width, int height) {
    startingScreenWidth = width;
    startingScreenHeight = height;
  }
  
  void bindFrameBuffer() {
    bindFrameBuffer(-1, width(), height());
  }

  void bindFrameBuffer(int frameBuffer, int width, int height) {
    bindFrameBuffer(frameBuffer, width, height, false);
  }

  void bindFrameBuffer(int frameBuffer, int width, int height, boolean force) {
    if (force || lastFrameBuffer != frameBuffer) {
      flush();

      lastFrameBuffer = frameBuffer;
      gfx.glBindFramebuffer(GL2ES2.GL_FRAMEBUFFER, frameBuffer);
      gfx.glViewport(0, 0, width, height);
      screenWidth = width;
      screenHeight = height;
    }
  }

  int createTexture(boolean repeatX, boolean repeatY) {
    IntBuffer texBuffer = IntBuffer.allocate(1);
    gfx.glGenTextures(1, texBuffer);
    int texture = texBuffer.get();
    gfx.glBindTexture(GL2ES2.GL_TEXTURE_2D, texture);
    gfx.glTexParameterf(GL2ES2.GL_TEXTURE_2D, GL2ES2.GL_TEXTURE_MAG_FILTER, GL2ES2.GL_LINEAR);
    gfx.glTexParameterf(GL2ES2.GL_TEXTURE_2D, GL2ES2.GL_TEXTURE_MIN_FILTER, GL2ES2.GL_LINEAR);
    gfx.glTexParameterf(GL2ES2.GL_TEXTURE_2D, GL2ES2.GL_TEXTURE_WRAP_S, repeatX ? GL2ES2.GL_REPEAT
        : GL2ES2.GL_CLAMP_TO_EDGE);
    gfx.glTexParameterf(GL2ES2.GL_TEXTURE_2D, GL2ES2.GL_TEXTURE_WRAP_T, repeatY ? GL2ES2.GL_REPEAT
        : GL2ES2.GL_CLAMP_TO_EDGE);
    ++texCount;
    return texture;
  }

  void destroyTexture(int texture) {
    gfx.glDeleteTextures(1, IntBuffer.wrap(new int[] {texture}));
  }

  void updateLayers() {
    // TODO(jonagill): Is -1 appropriate?
    bindFrameBuffer(-1, width, height);

    // Clear to transparent
    gfx.glClear(GL2ES2.GL_COLOR_BUFFER_BIT);

    // Paint all the layers
    // TODO(jonagill): Must update AndroidGroupLayer for this to work
    //rootLayer.paint(gfx, Transform.IDENTITY, 1);

    // Guarantee a flush
    useShader(null);
  }

  void updateTexture(int texture, AndroidImage image) {
    Bitmap bitmap = image.getBitmap();
    ByteBuffer pixels = ByteBuffer.allocateDirect(
        bitmap.getWidth() * bitmap.getHeight() * INT_SIZE_BYTES).order(ByteOrder.nativeOrder());
    pixels.position(0);
    bitmap.copyPixelsToBuffer(pixels);
    gfx.glBindTexture(GL2ES2.GL_TEXTURE_2D, texture);
    gfx.glTexImage2D(GL2ES2.GL_TEXTURE_2D, 0, GL2ES2.GL_RGBA, image.width(), image.height(), 0,
        GL2ES2.GL_RGBA, GL2ES2.GL_BYTE, pixels);
  }

  void drawTexture(int texture, float texWidth, float texHeight, Transform local, float dw,
      float dh, boolean repeatX, boolean repeatY, float alpha) {
    drawTexture(texture, texWidth, texHeight, local, 0, 0, dw, dh, repeatX, repeatY, alpha);
  }

  void drawTexture(int texture, float texWidth, float texHeight, Transform local, float dx,
      float dy, float dw, float dh, boolean repeatX, boolean repeatY, float alpha) {
    float sw = repeatX ? dw : texWidth, sh = repeatY ? dh : texHeight;
    drawTexture(texture, texWidth, texHeight, local, dx, dy, dw, dh, 0, 0, sw, sh, alpha);
  }

  void drawTexture(int texture, float texWidth, float texHeight, Transform local, float dx,
      float dy, float dw, float dh, float sx, float sy, float sw, float sh, float alpha) {
    texShader.prepare(texture, alpha);
    sx /= texWidth;
    sw /= texWidth;
    sy /= texHeight;
    sh /= texHeight;

    // TODO(jonagill): Should this be more extensible? I suppose it's fine... 4
    // vertices, 6 indices (two triangles == square)
    int idx = texShader.beginPrimitive(4, 6);
    texShader.buildVertex(local, dx, dy, sx, sy);
    texShader.buildVertex(local, dx + dw, dy, sx + sw, sy);
    texShader.buildVertex(local, dx, dy + dh, sx, sy + sh);
    texShader.buildVertex(local, dx + dw, dy + sy, sx + sw, sy + sh);

    texShader.addElement(idx + 0);
    texShader.addElement(idx + 1);
    texShader.addElement(idx + 2);
    texShader.addElement(idx + 1);
    texShader.addElement(idx + 3);
    texShader.addElement(idx + 2);
  }

  void fillRect(Transform local, float dx, float dy, float dw, float dh, float texWidth,
      float texHeight, int texture, float alpha) {
    texShader.prepare(texture, alpha);

    float sx = dx / texWidth, sy = dy / texHeight;
    float sw = dw / texWidth, sh = dh / texHeight;

    // Redundant code... could be split off into a method
    int idx = texShader.beginPrimitive(4, 6);
    texShader.buildVertex(local, dx, dy, sx, sy);
    texShader.buildVertex(local, dx + dw, dy, sx + sw, sy);
    texShader.buildVertex(local, dx, dy + dh, sx, sy + sh);
    texShader.buildVertex(local, dx + dw, dy + sy, sx + sw, sy + sh);

    texShader.addElement(idx + 0);
    texShader.addElement(idx + 1);
    texShader.addElement(idx + 2);
    texShader.addElement(idx + 1);
    texShader.addElement(idx + 3);
    texShader.addElement(idx + 2);
  }

  void fillRect(Transform local, float dx, float dy, float dw, float dh, int color, float alpha) {
    colorShader.prepare(color, alpha);

    int idx = colorShader.beginPrimitive(4, 6);
    colorShader.buildVertex(local, dx, dy);
    colorShader.buildVertex(local, dx + dw, dy);
    colorShader.buildVertex(local, dx, dy + dh);
    colorShader.buildVertex(local, dx + dw, dy + dh);

    colorShader.addElement(idx + 0);
    colorShader.addElement(idx + 1);
    colorShader.addElement(idx + 2);
    colorShader.addElement(idx + 1);
    colorShader.addElement(idx + 3);
    colorShader.addElement(idx + 2);
  }

  // Verbatim
  void fillPoly(Transform local, float[] positions, int color, float alpha) {
    colorShader.prepare(color, alpha);

    int idx = colorShader.beginPrimitive(4, 6);
    int points = positions.length / 2;
    for (int i = 0; i < points; ++i) {
      float dx = positions[i * 2];
      float dy = positions[i * 2 + 1];
      colorShader.buildVertex(local, dx, dy);
    }

    int a = idx + 0, b = idx + 1, c = idx + 2;
    int tris = points - 2;
    for (int i = 0; i < tris; i++) {
      colorShader.addElement(a);
      colorShader.addElement(b);
      colorShader.addElement(c);
      a = c;
      b = a + 1;
      c = (i == tris - 2) ? idx : b + 1;
    }
  }

  void flush() {
    if (curShader != null) {
      curShader.flush();
      curShader = null;
    }
  }

  private void initGL() {
    
    gfx.glDisable(GL2ES2.GL_CULL_FACE);
    gfx.glEnable(GL2ES2.GL_BLEND);
    //FIXME: Neither of these functions are supported in GLES20.java
//    gfx.glBlendEquation(GL2ES2.GL_FUNC_ADD);
//gfx.glBlendFuncSeparate(GL2ES2.GL_SRC_ALPHA, GL2ES2.GL_ONE_MINUS_SRC_ALPHA, GL2ES2.GL_SRC_ALPHA,
//        GL2ES2.GL_DST_ALPHA);

    // TODO(jonagill): Try basic GL calls, give up if not
  }

  private boolean useShader(Shader shader) {
    if (curShader != shader) {
      flush();
      curShader = shader;
      return true;
    }
    return false;
  }

  // TODO(jonagill): Array printing debug functions?

  private void checkGlError(String op) {
    int error;
    while ((error = gfx.glGetError()) != GL2ES2.GL_NO_ERROR) {
      Log.e(this.getClass().getName(), op + ": glError " + error);
      throw new RuntimeException(op + ": glError " + error);
    }
  }
}
