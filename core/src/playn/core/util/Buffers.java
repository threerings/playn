package playn.core.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class Buffers {

  public static ByteBuffer allocateNativeByteBuffer(int size) {
    return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
  }
  
  public static IntBuffer allocateNativeIntBuffer(int size) {
    return allocateNativeByteBuffer(size * 4).asIntBuffer();
  }

  public static FloatBuffer allocateNativeFloatBuffer(int size) {
    return allocateNativeByteBuffer(size * 4).asFloatBuffer();
  }

  public static ShortBuffer allocateNativeShortBuffer(int size) {
    return allocateNativeByteBuffer(size * 2).asShortBuffer();
  }

  public static int getElementSize(Buffer buffer) {
      if ((buffer instanceof FloatBuffer) || (buffer instanceof IntBuffer)) {
          return 4;
      } else if (buffer instanceof ShortBuffer) {
          return 2;
      } else if (buffer instanceof ByteBuffer) {
          return 1;
      } else {
          throw new RuntimeException("Unrecognized buffer type: " + buffer.getClass());
      }
  }

  public static String toString(FloatBuffer buf) {
    StringBuilder sb = new StringBuilder("[");
    int pos = buf.position();
    int count = buf.remaining();
    if (count > 100) {
      count = 100;
    }
    for (int i = 0; i < count; i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(buf.get(pos + i));
    }
    if (count != buf.remaining()) {
      sb.append("...");
    }
    sb.append("]");
    return sb.toString();
  }

  public static String toString(ShortBuffer buf) {
    StringBuilder sb = new StringBuilder("[");
    int pos = buf.position();
    int count = buf.remaining();
    if (count > 100) {
      count = 100;
    }
    for (int i = 0; i < count; i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(buf.get(pos + i));
    }
    if (count != buf.remaining()) {
      sb.append("...");
    }
    sb.append("]");
    return sb.toString();
  }


}
