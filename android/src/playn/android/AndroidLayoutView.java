package playn.android;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class AndroidLayoutView extends LinearLayout {
  private GameViewGL gameView;
  
  public AndroidLayoutView(Activity activity) {
    super(activity);
    setBackgroundColor(0xFF000000);
    setGravity(Gravity.CENTER);
  }
  
  @Override
  public void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    Log.i("playn", "Screen sized changed to ( " + w + " , " + h + ")");
    AndroidPlatform platform = AndroidPlatform.instance;
    if (platform != null) {
      if (platform.graphics() != null) platform.graphics().refreshScreenSize();
    }else {
      AndroidGraphics.setStartingScreenSize(w,h);
    }
  }
  
  @Override
  public void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
  }
  
  public void setGameView(GameViewGL gameView) {
    this.gameView = gameView;
  }
  
}
