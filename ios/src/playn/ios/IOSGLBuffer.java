/**
 * Copyright 2012 The PlayN Authors
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
package playn.ios;

import cli.System.IntPtr;
import cli.System.Runtime.InteropServices.GCHandle;
import cli.System.Runtime.InteropServices.GCHandleType;

import cli.OpenTK.Graphics.ES20.All;
import cli.OpenTK.Graphics.ES20.BeginMode;
import cli.OpenTK.Graphics.ES20.BufferTarget;
import cli.OpenTK.Graphics.ES20.BufferUsage;
import cli.OpenTK.Graphics.ES20.DrawElementsType;
import cli.OpenTK.Graphics.ES20.GL;

import playn.core.InternalTransform;
import playn.core.gl.GLBuffer;

public abstract class IOSGLBuffer implements GLBuffer {

  public static class FloatImpl extends IOSGLBuffer implements GLBuffer.Float {
    protected float[] data;
    protected GCHandle handle;

    public FloatImpl(int capacity) {
      expand(capacity);
    }

    @Override
    public Float add(float value) {
      data[position++] = value;
      return this;
    }

    @Override
    public Float add(InternalTransform xform) {
      // TODO: optimize?
      return add(xform.m00(), xform.m01(), xform.m10(), xform.m11(), xform.tx(), xform.ty());
    }

    @Override
    public Float add(float x, float y) {
      data[position++] = x;
      data[position++] = y;
      return this;
    }

    @Override
    public Float add(float m00, float m01, float m10, float m11, float tx, float ty) {
      data[position++] = m00;
      data[position++] = m01;
      data[position++] = m10;
      data[position++] = m11;
      data[position++] = tx;
      data[position++] = ty;
      return this;
    }

    @Override
    public Float add(float[] data, int offset, int length) {
      System.arraycopy(data, offset, this.data, position, length);
      position += length;
      return this;
    }

    @Override
    public int capacity() {
      return data.length;
    }

    @Override
    public void expand(int capacity) {
      if (handle != null)
        handle.Free();
      data = new float[capacity];
      handle = GCHandle.Alloc(data, GCHandleType.wrap(GCHandleType.Pinned));
    }

    @Override
    public int byteSize() {
      return position() * 4;
    }

    @Override
    public void skip(int count) {
      position += count;
    }

    @Override
    IntPtr pointer() {
      return handle.AddrOfPinnedObject();
    }
  }

  /** A buffer of 16-bit unsigned integers. */
  public static class ShortImpl extends IOSGLBuffer implements GLBuffer.Short {
    protected short[] data;
    protected GCHandle handle;

    public ShortImpl(int capacity) {
      expand(capacity);
    }

    @Override
    public Short add(int value) {
      data[position++] = (short) value;
      return this;
    }

    @Override
    public void drawElements(int mode, int count) {
      GL.DrawElements(BeginMode.wrap(mode), count,
                      DrawElementsType.wrap(DrawElementsType.UnsignedShort), new IntPtr(0));
    }

    @Override
    public int capacity() {
      return data.length;
    }

    @Override
    public void expand(int capacity) {
      if (handle != null)
        handle.Free();
      data = new short[capacity];
      handle = GCHandle.Alloc(data, GCHandleType.wrap(GCHandleType.Pinned));
    }

    @Override
    public int byteSize() {
      return position() * 2;
    }

    @Override
    public void skip(int count) {
      position += count;
    }

    @Override
    IntPtr pointer() {
      return handle.AddrOfPinnedObject();
    }
  }

  private final int bufferId;
  protected int position;

  @Override
  public int position() {
    return position;
  }

  @Override
  public void bind(int target) {
    GL.BindBuffer(BufferTarget.wrap(target), bufferId);
  }

  @Override
  public int send(int target, int usage) {
    // TODO: why is byteSize an IntPtr? File MonoTouch bug?
    GL.BufferData(BufferTarget.wrap(target), new IntPtr(byteSize()), pointer(),
                  BufferUsage.wrap(usage));
    int oposition = position;
    position = 0;
    return oposition;
  }

  @Override
  public void destroy() {
    GL.DeleteBuffers(1, new int[] { bufferId });
  }

  protected IOSGLBuffer() {
    int[] buffers = new int[1];
    GL.GenBuffers(1, buffers);
    this.bufferId = buffers[0];
  }

  abstract IntPtr pointer();
}
