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
import playn.core.InternalTransform;
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
      uScreenSizeLoc = gl20.glGetUniformLocation(program, "u_ScreenSize");
      aMatrix = gl20.glGetAttribLocation(program, "a_Matrix");
      aTranslation = gl20.glGetAttribLocation(program, "a_Translation");
      aPosition = gl20.glGetAttribLocation(program, "a_Position");
      aTexture = gl20.glGetAttribLocation(program, "a_Texture");

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
        gl20.glUseProgram(program);
        gl20.glUniform2fv(uScreenSizeLoc, 2,
            FloatBuffer.wrap(new float[] {screenWidth, screenHeight}));
        gl20.glBindBuffer(GL2ES2.GL_ARRAY_BUFFER, 0);
        gl20.glBindBuffer(GL2ES2.GL_ELEMENT_ARRAY_BUFFER, 0);

        // TODO(jonagill):MAKE SURE TO ADD BUFFERS!!
        //thisCodeWontWorkUntilIAddThose();

        gl20.glEnableVertexAttribArray(aMatrix);
        gl20.glEnableVertexAttribArray(aTranslation);
        gl20.glEnableVertexAttribArray(aPosition);
        if (aTexture != -1)
          gl20.glEnableVertexAttribArray(aTexture);

        gl20.glVertexAttribPointer(aMatrix, 4, GL2ES2.GL_FLOAT, false, 40, 0);
        gl20.glVertexAttribPointer(aTranslation, 2, GL2ES2.GL_FLOAT, false, 40, 16);
        gl20.glVertexAttribPointer(aPosition, 2, GL2ES2.GL_FLOAT, false, 40, 24);
        if (aTexture != -1)
          gl20.glVertexAttribPointer(aTexture, 2, GL2ES2.GL_FLOAT, false, 40, 32);

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
      gl20.glBufferData(GL2ES2.GL_ARRAY_BUFFER, vertexOffset, FloatBuffer.wrap(vertexSubarray),
          GL2ES2.GL_STREAM_DRAW);
      gl20.glBufferData(GL2ES2.GL_ELEMENT_ARRAY_BUFFER, elementOffset,
          IntBuffer.wrap(elementSubarray), GL2ES2.GL_STREAM_DRAW);

      gl20.glDrawElements(GL2ES2.GL_TRIANGLES, elementOffset, GL2ES2.GL_UNSIGNED_SHORT, 0);
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

    void buildVertex(InternalTransform local, float dx, float dy) {
      buildVertex(local, dx, dy, 0, 0);
    }

    void buildVertex(InternalTransform local, float dx, float dy, float sx, float sy) {
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
      shader = gl20.glCreateShader(type);
      if (shader == 0)
        return 0;

      // Load the shader source
      gl20.glShaderSource(shader, shaderSource);

      // Compile the shader
      gl20.glCompileShader(shader);

      IntBuffer compiled = IntBuffer.allocate(1);
      gl20.glGetShaderiv(shader, GL2ES2.GL_COMPILE_STATUS, compiled);

      if (compiled.array()[0] == 0) { // Same as gfx.GL_FALSE
        Log.e(this.getClass().getName(), "Could not compile shader " + type + ":");
        Log.e(this.getClass().getName(), gl20.glGetShaderInfoLog(shader));
        gl20.glDeleteShader(shader);
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
      int program = gl20.glCreateProgram();
      if (vertexShader == 0 || fragmentShader == 0 || program == 0)
        return 0;

      if (program != 0) {
        gl20.glAttachShader(program, vertexShader);
        checkGlError("glAttachShader");
        gl20.glAttachShader(program, fragmentShader);
        checkGlError("glAttachShader");
        gl20.glLinkProgram(program);
        IntBuffer linkStatus = IntBuffer.allocate(1);
        gl20.glGetProgramiv(program, GL2ES2.GL_LINK_STATUS, linkStatus);
        if (linkStatus.array()[0] != GL2ES2.GL_TRUE) {
          Log.e(this.getClass().getName(), "Could not link program: ");
          Log.e(this.getClass().getName(), gl20.glGetProgramInfoLog(program));
          gl20.glDeleteProgram(program);
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
      uTexture = gl20.glGetUniformLocation(program, "u_Texture");
      uAlpha = gl20.glGetUniformLocation(program, "u_Alpha");
    }

    @Override
    void flush() {
      gl20.glBindTexture(GL2ES2.GL_TEXTURE_2D, lastTex);
      super.flush();
    }

    void prepare(int tex, float alpha) {
      if (super.prepare()) {
        gl20.glActiveTexture(GL2ES2.GL_TEXTURE0);
        gl20.glUniform1i(uTexture, 0);
      }

      if (tex == lastTex && alpha == lastAlpha)
        return;
      flush();

      gl20.glUniform1f(uAlpha, alpha);
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
      uColor = gl20.glGetUniformLocation(program, "u_Color");
      uAlpha = gl20.glGetUniformLocation(program, "u_Alpha");
    }

    void prepare(int color, float alpha) {
      super.prepare();

      if (color == lastColor && alpha == lastAlpha)
        return;
      flush();

      gl20.glUniform1f(uAlpha, alpha);
      lastAlpha = alpha;
      setColor(color);
    }

    private void setColor(int color) {
      float[] colorsArray = colors.array();
      colorsArray[3] = (float) ((color >> 24) & 0xff) / 255;
      colorsArray[0] = (float) ((color >> 16) & 0xff) / 255;
      colorsArray[1] = (float) ((color >> 8) & 0xff) / 255;
      colorsArray[2] = (float) ((color >> 0) & 0xff) / 255;
      gl20.glUniform4fv(uColor, colors.capacity(), colors);

      lastColor = color;
    }
  }

  protected final AndroidGL20 gl20;
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
    this.gl20 = gfx;
    gameView = AndroidPlatform.instance.activity.gameView();
    rootLayer = new AndroidGroupLayer(this);
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
    return new AndroidCanvasLayer(this, width, height, true);
  }

  public CanvasLayer createCanvasLayer(int width, int height, boolean alpha) {
    return new AndroidCanvasLayer(this, width, height, alpha);
  }

  @Override
  public GroupLayer createGroupLayer() {
    return new AndroidGroupLayer(this);
  }

  @Override
  public ImageLayer createImageLayer() {
    return new AndroidImageLayer(this);
  }

  @Override
  public ImageLayer createImageLayer(Image image) {
    return new AndroidImageLayer(this, (AndroidImage) image);
  }

  @Override
  public SurfaceLayer createSurfaceLayer(int width, int height) {
    return new AndroidSurfaceLayer(this, width, height);
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
  
  //TODO (jonagill) make sure that -1 is default fbuf
  void bindFramebuffer() {
    bindFramebuffer(-1, width(), height());
  }

  void bindFramebuffer(int frameBuffer, int width, int height) {
    bindFramebuffer(frameBuffer, width, height, false);
  }

  void bindFramebuffer(int frameBuffer, int width, int height, boolean force) {
    if (force || lastFrameBuffer != frameBuffer) {
      flush();

      lastFrameBuffer = frameBuffer;
      gl20.glBindFramebuffer(GL2ES2.GL_FRAMEBUFFER, frameBuffer);
      gl20.glViewport(0, 0, width, height);
      screenWidth = width;
      screenHeight = height;
    }
  }

  int createTexture(boolean repeatX, boolean repeatY) {
    IntBuffer texBuffer = IntBuffer.allocate(1);
    gl20.glGenTextures(1, texBuffer);
    int texture = texBuffer.get();
    gl20.glBindTexture(GL2ES2.GL_TEXTURE_2D, texture);
    gl20.glTexParameterf(GL2ES2.GL_TEXTURE_2D, GL2ES2.GL_TEXTURE_MAG_FILTER, GL2ES2.GL_LINEAR);
    gl20.glTexParameterf(GL2ES2.GL_TEXTURE_2D, GL2ES2.GL_TEXTURE_MIN_FILTER, GL2ES2.GL_LINEAR);
    gl20.glTexParameterf(GL2ES2.GL_TEXTURE_2D, GL2ES2.GL_TEXTURE_WRAP_S, repeatX ? GL2ES2.GL_REPEAT
        : GL2ES2.GL_CLAMP_TO_EDGE);
    gl20.glTexParameterf(GL2ES2.GL_TEXTURE_2D, GL2ES2.GL_TEXTURE_WRAP_T, repeatY ? GL2ES2.GL_REPEAT
        : GL2ES2.GL_CLAMP_TO_EDGE);
    ++texCount;
    return texture;
  }

  void destroyTexture(int texture) {
    gl20.glDeleteTextures(1, IntBuffer.wrap(new int[] {texture}));
  }

  void updateLayers() {
    // TODO(jonagill): Is -1 appropriate?
    bindFramebuffer(-1, width, height);

    // Clear to transparent
    gl20.glClear(GL2ES2.GL_COLOR_BUFFER_BIT);

    // Paint all the layers
    // TODO(jonagill): Must update AndroidGroupLayer for this to work
    //rootLayer.paint(gfx, Transform.IDENTITY, 1);

    // Guarantee a flush
    useShader(null);
  }

  void updateTexture(int texture, Bitmap image) {
    int width = image.getWidth();
    int height = image.getHeight();
    ByteBuffer pixels = ByteBuffer.allocateDirect(
        width * height * INT_SIZE_BYTES).order(ByteOrder.nativeOrder());
    pixels.position(0);
    image.copyPixelsToBuffer(pixels);
    gl20.glBindTexture(GL2ES2.GL_TEXTURE_2D, texture);
    gl20.glTexImage2D(GL2ES2.GL_TEXTURE_2D, 0, GL2ES2.GL_RGBA, width, height, 0,
        GL2ES2.GL_RGBA, GL2ES2.GL_BYTE, pixels);
  }

  void drawTexture(int texture, float texWidth, float texHeight, InternalTransform local, float dw,
      float dh, boolean repeatX, boolean repeatY, float alpha) {
    drawTexture(texture, texWidth, texHeight, local, 0, 0, dw, dh, repeatX, repeatY, alpha);
  }

  void drawTexture(int texture, float texWidth, float texHeight, InternalTransform local, float dx,
      float dy, float dw, float dh, boolean repeatX, boolean repeatY, float alpha) {
    float sw = repeatX ? dw : texWidth, sh = repeatY ? dh : texHeight;
    drawTexture(texture, texWidth, texHeight, local, dx, dy, dw, dh, 0, 0, sw, sh, alpha);
  }

  void drawTexture(int texture, float texWidth, float texHeight, InternalTransform local, float dx,
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

  void fillRect(InternalTransform local, float dx, float dy, float dw, float dh, float texWidth,
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

  void fillRect(InternalTransform local, float dx, float dy, float dw, float dh, int color, float alpha) {
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
  void fillPoly(InternalTransform local, float[] positions, int color, float alpha) {
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
    
    gl20.glDisable(GL2ES2.GL_CULL_FACE);
    gl20.glEnable(GL2ES2.GL_BLEND);
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
    while ((error = gl20.glGetError()) != GL2ES2.GL_NO_ERROR) {
      Log.e(this.getClass().getName(), op + ": glError " + error);
      throw new RuntimeException(op + ": glError " + error);
    }
  }
}
