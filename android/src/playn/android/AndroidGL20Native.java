package playn.android;


/**
 * Fix for the two missing function calls in
 * SDK 2.2 Froyo's OpenGL API.
 */
public class AndroidGL20Native extends AndroidGL20 {

  @Override
  native public void glDrawElements(int mode, int count, int type, int offset);
  
  @Override
  native public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride,
      int ptr);
  
  AndroidGL20Native() {}
  static {
      System.loadLibrary("playn-android");
  }
}
