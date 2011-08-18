package playn.android;

import javax.media.opengl.GL2ES2;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import playn.core.Platform;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameViewGL extends GLSurfaceView implements GameView, SurfaceHolder.Callback {

  public final AndroidGL20 gl20;
  private final SurfaceHolder holder;
  private GameLoop loop;
  private final GameActivity activity;
  private boolean gameInitialized = false;
  public  boolean gameSizeSet = false;  //Set by AndroidGraphics
 
  
  public GameViewGL(AndroidGL20 _gl20, GameActivity activity, Context context) {
    super(context);
    this.gl20 = _gl20;
    this.activity = activity;
    holder = getHolder();
    holder.addCallback(this);
    setFocusable(true);
    setEGLContextClientVersion(2); 
    this.setRenderer(new AndroidRendererGL());
    setRenderMode(RENDERMODE_CONTINUOUSLY);
  }
  
  @Override
  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //Default to filling all the available space when the game is first loads
    Platform platform = activity.platform();
    if (platform != null && gameSizeSet) {
      int width = platform.graphics().width();
      int height = platform.graphics().height();
      if (width == 0 || height == 0) {
        Log.e("playn", "Invalid game size set: (" + width + " , " + height + ")");
      }else {
        int minWidth = getSuggestedMinimumWidth();
        int minHeight = getSuggestedMinimumHeight();
        width = width > minWidth ? width : minWidth;
        height = height > minHeight ? height : minHeight;
        setMeasuredDimension(width, height);
        Log.i("playn", "Using game-specified sizing. (" + width + " , " + height + ")");
        return;
      }
    }
    
    Log.i("playn", "Using default sizing.");
    super.onMeasure(widthMeasureSpec, heightMeasureSpec); 
  }
  
  @Override
  public void notifyVisibilityChanged(int visibility) {
    Log.i("playn", "notifyVisibilityChanged: " + visibility);
    if (visibility == INVISIBLE) {
      onPause();
      if (loop != null) loop.pause();
    }else {
      onResume();
      if (loop != null) loop.start();
    }
  }
  
  public void gameInitialized() {
    gameInitialized = true;
  }
  
  private class AndroidRendererGL implements Renderer {    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
      gl20.glClearColor(1,1,1,1);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
      gl20.glViewport(0, 0, width, height);
      Log.i("playn", "Surface dimensions changed to ( " + width + " , " + height + ")");
    }

    @Override
    public void onDrawFrame(GL10 gl) {
      //Wait until onDrawFrame to make sure all the metrics
      //are in place at this point.
      if (!gameInitialized) {
        AndroidPlatform.register(gl20, activity);
        activity.main();
        loop = new GameLoop();
        loop.start();
        gameInitialized = true;
      }
      if (loop.running() && gameInitialized) loop.run();  //Handles updating, clearing the screen, and drawing
    }
  }
}
