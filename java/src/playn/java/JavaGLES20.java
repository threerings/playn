/**
 * Copyright 2011 The PlayN Authors
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
package playn.java;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengles.GLES20;

import playn.core.gl.GL20;

public class JavaGLES20 implements GL20 {

  // Sizes based on LWJGL's APIUtil
  private IntBuffer intBuffer = BufferUtils.createIntBuffer(32);
  private FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(32);
  private ByteBuffer byteBuffer = BufferUtils.createByteBuffer(256);

  private void setIntBuffer(final int[] source, final int offset, final int length) {
    resizeIntBuffer(length);
    intBuffer.put(source, offset, length);
    intBuffer.rewind();
  }

  private void setFloatBuffer(final float[] source, final int offset, final int length) {
    resizeFloatBuffer(length);
    floatBuffer.put(source, offset, length);
    floatBuffer.rewind();
  }

  private void setByteBuffer(final byte[] source, final int offset, final int length) {
    resizeByteBuffer(length);
    byteBuffer.put(source, offset, length);
    byteBuffer.rewind();
  }

  private void resizeByteBuffer(final int length) {
    final int cap = byteBuffer.capacity();
    if (cap < length) {
      int newLength = cap << 1;
      while (newLength < length) {
        newLength <<= 1;
      }
      byteBuffer = BufferUtils.createByteBuffer(newLength);
    } else {
      byteBuffer.position(0);
    }
    byteBuffer.limit(length);
  }

  private void resizeIntBuffer(final int length) {
    final int cap = intBuffer.capacity();
    if (cap < length) {
      int newLength = cap << 1;
      while (newLength < length) {
        newLength <<= 1;
      }
      intBuffer = BufferUtils.createIntBuffer(newLength);
    } else {
      intBuffer.position(0);
    }
    intBuffer.limit(length);
  }

  private void resizeFloatBuffer(final int length) {
    final int cap = floatBuffer.capacity();
    if (cap < length) {
      int newLength = cap << 1;
      while (newLength < length) {
        newLength <<= 1;
      }
      floatBuffer = BufferUtils.createFloatBuffer(newLength);
    } else {
      floatBuffer.position(0);
    }
    floatBuffer.limit(length);
  }

  @Override
  public String getPlatformGLExtensions() {
    String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
    return extensions;
  }

  @Override
  public int getSwapInterval() {
    return 0;
  }

  @Override
  public void glActiveTexture(int texture) {
    GLES20.glActiveTexture(texture);
  }

  @Override
  public void glAttachShader(int program, int shader) {
    GLES20.glAttachShader(program, shader);
  }

  @Override
  public void glBindAttribLocation(int program, int index, String name) {
    GLES20.glBindAttribLocation(program, index, name);
  }

  @Override
  public void glBindBuffer(int target, int buffer) {
    GLES20.glBindBuffer(target, buffer);
  }

  @Override
  public void glBindFramebuffer(int target, int framebuffer) {
    GLES20.glBindFramebuffer(target, framebuffer);
  }

  @Override
  public void glBindRenderbuffer(int target, int renderbuffer) {
    GLES20.glBindRenderbuffer(target, renderbuffer);
  }

  @Override
  public void glBindTexture(int target, int texture) {
    GLES20.glBindTexture(target, texture);
  }

  @Override
  public void glBlendColor(float red, float green, float blue, float alpha) {
    GLES20.glBlendColor(red, green, blue, alpha);
  }

  @Override
  public void glBlendEquation(int mode) throws RuntimeException {
    GLES20.glBlendEquation(mode);
  }

  @Override
  public void glBlendEquationSeparate(int modeRGB, int modeAlpha) throws RuntimeException {
    GLES20.glBlendEquationSeparate(modeRGB, modeAlpha);
  }

  @Override
  public void glBlendFunc(int sfactor, int dfactor) {
    GLES20.glBlendFunc(sfactor, dfactor);
  }

  @Override
  public void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha)
      throws RuntimeException {
    GLES20.glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
  }

  @Override
  public void glBufferData(int target, int size, Buffer data, int usage) {
    if (data instanceof ByteBuffer)
      GLES20.glBufferData(target, (ByteBuffer) data, usage);
    else if (data instanceof IntBuffer)
      GLES20.glBufferData(target, (IntBuffer) data, usage);
    else if (data instanceof FloatBuffer)
      GLES20.glBufferData(target, (FloatBuffer) data, usage);
    else if (data instanceof DoubleBuffer)
      throw new RuntimeException("Unsupported glBufferData with DoubleBuffer");
    else if (data instanceof ShortBuffer) //
      GLES20.glBufferData(target, (ShortBuffer) data, usage);

  }

  @Override
  public void glBufferSubData(int target, int offset, int size, Buffer data) {
    if (data instanceof ByteBuffer)
      GLES20.glBufferSubData(target, offset, (ByteBuffer) data);
    else if (data instanceof IntBuffer)
      GLES20.glBufferSubData(target, offset, (IntBuffer) data);
    else if (data instanceof FloatBuffer)
      GLES20.glBufferSubData(target, offset, (FloatBuffer) data);
    else if (data instanceof DoubleBuffer)
      throw new RuntimeException("Unsupported glBufferSubData with DoubleBuffer");
    else if (data instanceof ShortBuffer)
      GLES20.glBufferSubData(target, offset, (ShortBuffer) data);

  }

  @Override
  public int glCheckFramebufferStatus(int target) {
    return GLES20.glCheckFramebufferStatus(target);
  }

  @Override
  public void glClear(int mask) {
    GLES20.glClear(mask);

  }

  @Override
  public void glClearColor(float red, float green, float blue, float alpha) {
    GLES20.glClearColor(red, green, blue, alpha);
  }

  @Override
  public void glClearDepth(double depth) {
    GLES20.glClearDepthf((float) depth);

  }

  @Override
  public void glClearDepthf(float depth) {
    GLES20.glClearDepthf(depth);

  }

  @Override
  public void glClearStencil(int s) {
    GLES20.glClearStencil(s);
  }

  @Override
  public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
    GLES20.glColorMask(red, green, blue, alpha);
  }

  @Override
  public void glCompileShader(int shader) {
    GLES20.glCompileShader(shader);
  }

  @Override
  public void glCompressedTexImage2D(int target, int level, int internalformat, int width,
      int height, int border, int imageSize, Buffer data) {
    GLES20.glCompressedTexImage2D(target, level, internalformat, width, height, border,
        (ByteBuffer) data);
  }

  @Override
  public void glCompressedTexImage2D(int target, int level, int internalformat, int width,
      int height, int border, int data_imageSize, int data) throws RuntimeException {
    throw new RuntimeException("glCompressedTexImage2D not supported.");
  }

  @Override
  public void glCompressedTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5,
      int arg6, int arg7, Buffer arg8) {
    throw new RuntimeException("glCompressedTexImage3D not supported.");
  }

  @Override
  public void glCompressedTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5,
      int arg6, int arg7, int arg8) throws RuntimeException {
    throw new RuntimeException("glCompressedTexImage3D not supported.");
  }

  @Override
  public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width,
      int height, int format, int imageSize, Buffer data) {
    GLES20.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format,
        (ByteBuffer) data);
  }

  @Override
  public void glCompressedTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5,
      int arg6, int arg7, int arg8) throws RuntimeException {
    throw new RuntimeException(
        "glCompressedSubTexImage2D(int, int, int, int, int, int, int, int, int)" + "not supported.");
  }

  @Override
  public void glCompressedTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5,
      int arg6, int arg7, int arg8, int arg9, Buffer arg10) throws RuntimeException {
    throw new RuntimeException("glCompressedTexSubImage3D not supported.");
  }

  @Override
  public void glCompressedTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5,
      int arg6, int arg7, int arg8, int arg9, int arg10) throws RuntimeException {
    throw new RuntimeException("glCompressedTexSubImage3D not supported.");
  }

  @Override
  public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width,
      int height, int border) {
    GLES20.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
  }

  @Override
  public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y,
      int width, int height) {
    GLES20.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
  }

  @Override
  public void glCopyTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5,
      int arg6, int arg7, int arg8) throws RuntimeException {
    throw new RuntimeException("glCopyTexSubImage3D not supported.");
  }

  @Override
  public int glCreateProgram() {
    return GLES20.glCreateProgram();
  }

  @Override
  public int glCreateShader(int type) {
    return GLES20.glCreateShader(type);
  }

  @Override
  public void glCullFace(int mode) {
    GLES20.glCullFace(mode);
  }

  @Override
  public void glDeleteBuffers(int n, int[] buffers, int offset) {
    setIntBuffer(buffers, offset, n);
    GLES20.glDeleteBuffers(intBuffer);
  }

  @Override
  public void glDeleteBuffers(int n, IntBuffer buffers) {
    GLES20.glDeleteBuffers(buffers);
  }

  @Override
  public void glDeleteFramebuffers(int n, int[] framebuffers, int offset) {
    setIntBuffer(framebuffers, offset, n);
    GLES20.glDeleteFramebuffers(intBuffer);
  }

  @Override
  public void glDeleteFramebuffers(int n, IntBuffer framebuffers) {
    GLES20.glDeleteFramebuffers(framebuffers);
  }

  @Override
  public void glDeleteProgram(int program) {
    GLES20.glDeleteProgram(program);
  }

  @Override
  public void glDeleteRenderbuffers(int n, int[] renderbuffers, int offset) {
    setIntBuffer(renderbuffers, offset, n);
    GLES20.glDeleteRenderbuffers(intBuffer);
  }

  @Override
  public void glDeleteRenderbuffers(int n, IntBuffer renderbuffers) {
    GLES20.glDeleteRenderbuffers(renderbuffers);
  }

  @Override
  public void glDeleteShader(int shader) {
    GLES20.glDeleteShader(shader);
  }

  @Override
  public void glDeleteTextures(int n, int[] textures, int offset) {
    setIntBuffer(textures, offset, n);
    GLES20.glDeleteTextures(intBuffer);
  }

  @Override
  public void glDeleteTextures(int n, IntBuffer textures) {
    GLES20.glDeleteTextures(textures);
  }

  @Override
  public void glDepthFunc(int func) {
    GLES20.glDepthFunc(func);
  }

  @Override
  public void glDepthMask(boolean flag) {
    GLES20.glDepthMask(flag);
  }

  @Override
  public void glDepthRange(double zNear, double zFar) {
    GLES20.glDepthRangef((float) zNear, (float) zFar);

  }

  @Override
  public void glDepthRangef(float zNear, float zFar) {
    GLES20.glDepthRangef(zNear, zFar);
  }

  @Override
  public void glDetachShader(int program, int shader) {
    GLES20.glDetachShader(program, shader);
  }

  @Override
  public void glDisable(int cap) {
    GLES20.glDisable(cap);
  }

  @Override
  public void glDisableVertexAttribArray(int index) {
    GLES20.glDisableVertexAttribArray(index);
  }

  @Override
  public void glDrawArrays(int mode, int first, int count) {
    GLES20.glDrawArrays(mode, first, count);
  }

  @Override
  public void glDrawElements(int mode, int count, int type, Buffer indices) {
    if (indices instanceof ShortBuffer && type == GL_UNSIGNED_SHORT)
      GLES20.glDrawElements(mode, (ShortBuffer) indices);
    else if (indices instanceof ByteBuffer && type == GL_UNSIGNED_SHORT)
      GLES20.glDrawElements(mode, ((ByteBuffer) indices).asShortBuffer()); // FIXME
    // yay...
    else if (indices instanceof ByteBuffer && type == GL_UNSIGNED_BYTE)
      GLES20.glDrawElements(mode, (ByteBuffer) indices);
    else
      throw new RuntimeException("Can't use " + indices.getClass().getName()
          + " with this method. Use ShortBuffer or ByteBuffer instead. Blame LWJGL");
  }

  @Override
  public void glDrawElements(int mode, int count, int type, int offset) {
    GLES20.glDrawElements(mode, count, type, offset);
  }

  @Override
  public void glEnable(int cap) {
    GLES20.glEnable(cap);
  }

  @Override
  public void glEnableVertexAttribArray(int index) {
    GLES20.glEnableVertexAttribArray(index);
  }

  @Override
  public void glFinish() {
    GLES20.glFinish();
  }

  @Override
  public void glFlush() {
    GLES20.glFlush();
  }

  @Override
  public void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget,
      int renderbuffer) {
    GLES20.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
  }

  @Override
  public void glFramebufferTexture2D(int target, int attachment, int textarget, int texture,
      int level) {
    GLES20.glFramebufferTexture2D(target, attachment, textarget, texture, level);
  }

  @Override
  public void glFramebufferTexture3D(int target, int attachment, int textarget, int texture,
      int level, int zoffset) {
    GLES20.glFramebufferTexture2D(target, attachment, textarget, texture, level);
  }

  @Override
  public void glFrontFace(int mode) {
    GLES20.glFrontFace(mode);
  }

  @Override
  public void glGenBuffers(int n, int[] buffers, int offset) {
    resizeIntBuffer(n);
    GLES20.glGenBuffers(intBuffer);
    intBuffer.get(buffers, offset, n);
  }

  @Override
  public void glGenBuffers(int n, IntBuffer buffers) {
    GLES20.glGenBuffers(buffers);
  }

  @Override
  public void glGenerateMipmap(int target) {
    GLES20.glGenerateMipmap(target);
  }

  @Override
  public void glGenFramebuffers(int n, int[] framebuffers, int offset) {
    resizeIntBuffer(n);
    GLES20.glGenFramebuffers(intBuffer);
    intBuffer.get(framebuffers, offset, n);
  }

  @Override
  public void glGenFramebuffers(int n, IntBuffer framebuffers) {
    GLES20.glGenFramebuffers(framebuffers);
  }

  @Override
  public void glGenRenderbuffers(int n, int[] renderbuffers, int offset) {
    resizeIntBuffer(n);
    GLES20.glGenRenderbuffers(intBuffer);
    intBuffer.get(renderbuffers, offset, n);
  }

  @Override
  public void glGenRenderbuffers(int n, IntBuffer renderbuffers) {
    GLES20.glGenRenderbuffers(renderbuffers);
  }

  @Override
  public void glGenTextures(int n, int[] textures, int offset) {
    resizeIntBuffer(n);
    GLES20.glGenTextures(intBuffer);
    intBuffer.get(textures, offset, n);

  }

  @Override
  public void glGenTextures(int n, IntBuffer textures) {
    GLES20.glGenTextures(textures);
  }

  @Override
  public void glGetActiveAttrib(int program, int index, int bufsize, int[] length,
      int lengthOffset, int[] size, int sizeOffset, int[] type, int typeOffset, byte[] name,
      int nameOffset) {
    // http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetActiveAttrib.xml
    // Returns length, size, type, name
    resizeIntBuffer(2);

    // Return name, length
    final String nameString = GLES20.glGetActiveAttrib(program, index, bufsize, intBuffer);
    try {
      final byte[] nameBytes = nameString.getBytes("UTF-8");
      final int nameLength = nameBytes.length - nameOffset;
      setByteBuffer(nameBytes, nameOffset, nameLength);
      byteBuffer.get(name, nameOffset, nameLength);
      length[lengthOffset] = nameLength;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    // Return size, type
    intBuffer.get(size, 0, 1);
    intBuffer.get(type, 0, 1);
  }

  @Override
  public void glGetActiveAttrib(int program, int index, int bufsize, IntBuffer length,
      IntBuffer size, IntBuffer type, ByteBuffer name) {
    IntBuffer typeTmp = BufferUtils.createIntBuffer(2);
    GLES20.glGetActiveAttrib(program, index, 256, typeTmp);
    type.put(typeTmp.get(0));
    type.rewind();
  }

  @Override
  public void glGetActiveUniform(int program, int index, int bufsize, int[] length,
      int lengthOffset, int[] size, int sizeOffset, int[] type, int typeOffset, byte[] name,
      int nameOffset) {
    resizeIntBuffer(2);

    // Return name, length
    final String nameString = GLES20.glGetActiveUniform(program, index, 256, intBuffer);
    try {
      final byte[] nameBytes = nameString.getBytes("UTF-8");
      final int nameLength = nameBytes.length - nameOffset;
      setByteBuffer(nameBytes, nameOffset, nameLength);
      byteBuffer.get(name, nameOffset, nameLength);
      length[lengthOffset] = nameLength;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    // Return size, type
    intBuffer.get(size, 0, 1);
    intBuffer.get(type, 0, 1);
  }

  @Override
  public void glGetActiveUniform(int program, int index, int bufsize, IntBuffer length,
      IntBuffer size, IntBuffer type, ByteBuffer name) {
    IntBuffer typeTmp = BufferUtils.createIntBuffer(2);
    GLES20.glGetActiveAttrib(program, index, 256, typeTmp);
    type.put(typeTmp.get(0));
    type.rewind();
  }

  @Override
  public void glGetAttachedShaders(int program, int maxcount, int[] count, int countOffset,
      int[] shaders, int shadersOffset) {
    final int countLength = count.length - countOffset;
    resizeIntBuffer(countLength);

    final int shadersLength = shaders.length - shadersOffset;
    final IntBuffer intBuffer2 = BufferUtils.createIntBuffer(shadersLength);
    GLES20.glGetAttachedShaders(program, intBuffer, intBuffer2);

    // Return count, shaders
    intBuffer.get(count, countOffset, countLength);
    intBuffer2.get(shaders, shadersOffset, shadersLength);
  }

  @Override
  public void glGetAttachedShaders(int program, int maxcount, IntBuffer count, IntBuffer shaders) {
    GLES20.glGetAttachedShaders(program, count, shaders);
  }

  @Override
  public int glGetAttribLocation(int program, String name) {
    return GLES20.glGetAttribLocation(program, name);
  }

  @Override
  public boolean glGetBoolean(int pname) {
    byte[] out = new byte[1];
    glGetBooleanv(pname, out, 0);
    return out[0] != GL_FALSE;
  }

  @Override
  public void glGetBooleanv(int pname, byte[] params, int offset) {
    // TODO(jonagill): Test!
    ByteBuffer buffer = ByteBuffer.wrap(params, offset, params.length - offset);
    GLES20.glGetBoolean(pname, buffer);
  }

  @Override
  public void glGetBooleanv(int pname, ByteBuffer params) {
    GLES20.glGetBoolean(pname, params);
  }

  @Override
  public int glGetBoundBuffer(int arg0) throws RuntimeException {
    throw new RuntimeException("glGetBoundBuffer not supported in GLES 2.0");
  }

  @Override
  public void glGetBufferParameteriv(int target, int pname, int[] params, int offset) {
    final int length = params.length - offset;
    resizeIntBuffer(length);
    GLES20.glGetBufferParameter(target, pname, intBuffer);
    intBuffer.get(params, offset, length);
  }

  @Override
  public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
    GLES20.glGetBufferParameter(target, pname, params);
  }

  @Override
  public int glGetError() {
    return GLES20.glGetError();
  }

  @Override
  public float glGetFloat(int pname) {
    return GLES20.glGetFloat(pname);
  }

  @Override
  public void glGetFloatv(int pname, float[] params, int offset) {
    final int length = params.length - offset;
    resizeFloatBuffer(length);
    GLES20.glGetFloat(pname, floatBuffer);
    floatBuffer.get(params, offset, length);
  }

  @Override
  public void glGetFloatv(int pname, FloatBuffer params) {
    GLES20.glGetFloat(pname, params);
  }

  @Override
  public void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname,
      int[] params, int offset) {
    final int length = params.length - offset;
    resizeIntBuffer(length);
    GLES20.glGetFramebufferAttachmentParameter(target, attachment, pname, intBuffer);
    intBuffer.get(params, offset, length);
  }

  @Override
  public void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname,
      IntBuffer params) {
    GLES20.glGetFramebufferAttachmentParameter(target, attachment, pname, params);
  }

  @Override
  public int glGetInteger(int pname) {
    return GLES20.glGetInteger(pname);
  }

  @Override
  public void glGetIntegerv(int pname, int[] params, int offset) {
    final int length = params.length - offset;
    resizeIntBuffer(length);
    GLES20.glGetInteger(pname, intBuffer);
    intBuffer.get(params, offset, length);
  }

  @Override
  public void glGetIntegerv(int pname, IntBuffer params) {
    GLES20.glGetInteger(pname, params);
  }

  @Override
  public void glGetProgramBinary(int arg0, int arg1, int[] arg2, int arg3, int[] arg4, int arg5,
      Buffer arg6) throws RuntimeException {
    throw new RuntimeException("glGetProgramBinary not supported.");
  }

  @Override
  public void glGetProgramBinary(int arg0, int arg1, IntBuffer arg2, IntBuffer arg3, Buffer arg4)
      throws RuntimeException {
    throw new RuntimeException("glGetProgramBinary not supported.");
  }

  @Override
  public void glGetProgramInfoLog(int program, int bufsize, int[] length, int lengthOffset,
      byte[] infolog, int infologOffset) {
    final int intLength = length.length - lengthOffset;
    resizeIntBuffer(intLength);

    final int byteLength = bufsize - infologOffset;
    resizeByteBuffer(byteLength);

    GLES20.glGetProgramInfoLog(program, intBuffer, byteBuffer);
    // length is the length of the infoLog string being returned
    intBuffer.get(length, lengthOffset, intLength);
    // infoLog is the char array of the infoLog
    byteBuffer.get(infolog, byteLength, infologOffset);
  }

  @Override
  public void glGetProgramInfoLog(int program, int bufsize, IntBuffer length, ByteBuffer infolog) {
    glGetProgramInfoLog(program, bufsize, length.array(), infolog.position(), infolog.array(),
        infolog.position());
  }

  @Override
  public String glGetProgramInfoLog(int program) {
    ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 10);
    buffer.order(ByteOrder.nativeOrder());
    ByteBuffer tmp = ByteBuffer.allocateDirect(4);
    tmp.order(ByteOrder.nativeOrder());
    IntBuffer intBuffer = tmp.asIntBuffer();

    GLES20.glGetProgramInfoLog(program, intBuffer, buffer);
    int numBytes = intBuffer.get(0);
    byte[] bytes = new byte[numBytes];
    buffer.get(bytes);
    return new String(bytes);
  }

  @Override
  public void glGetProgramiv(int program, int pname, int[] params, int offset) {
    final int length = params.length - offset;
    resizeIntBuffer(length);
    GLES20.glGetProgram(program, pname, intBuffer);
    intBuffer.get(params, offset, length);
  }

  @Override
  public void glGetProgramiv(int program, int pname, IntBuffer params) {
    GLES20.glGetProgram(program, pname, params);
  }

  @Override
  public void glGetRenderbufferParameteriv(int target, int pname, int[] params, int offset) {
    final int length = params.length - offset;
    resizeIntBuffer(length);
    GLES20.glGetRenderbufferParameter(target, pname, intBuffer);
    intBuffer.get(params, offset, length);
  }

  @Override
  public void glGetRenderbufferParameteriv(int target, int pname, IntBuffer params) {
    GLES20.glGetRenderbufferParameter(target, pname, params);
  }

  @Override
  public void glGetShaderInfoLog(int shader, int bufsize, int[] length, int lengthOffset,
      byte[] infolog, int infologOffset) {
    final int intLength = length.length - lengthOffset;
    resizeIntBuffer(intLength);
    final int byteLength = bufsize - infologOffset;
    resizeByteBuffer(byteLength);
    GLES20.glGetShaderInfoLog(shader, intBuffer, byteBuffer);
    // length is the length of the infoLog string being returned
    intBuffer.get(length, lengthOffset, intLength);
    // infoLog is the char array of the infoLog
    byteBuffer.get(infolog, byteLength, infologOffset);
  }

  @Override
  public void glGetShaderInfoLog(int shader, int bufsize, IntBuffer length, ByteBuffer infolog) {
    glGetShaderInfoLog(shader, bufsize, length.array(), length.position(), infolog.array(),
        infolog.position());
  }

  @Override
  public String glGetShaderInfoLog(int shader) {
    ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 10);
    buffer.order(ByteOrder.nativeOrder());
    ByteBuffer tmp = ByteBuffer.allocateDirect(4);
    tmp.order(ByteOrder.nativeOrder());
    IntBuffer intBuffer = tmp.asIntBuffer();

    GLES20.glGetShaderInfoLog(shader, intBuffer, buffer);
    int numBytes = intBuffer.get(0);
    byte[] bytes = new byte[numBytes];
    buffer.get(bytes);
    return new String(bytes);
  }

  @Override
  public void glGetShaderiv(int shader, int pname, int[] params, int offset) {
    final int length = params.length - offset;
    resizeIntBuffer(length);
    GLES20.glGetShader(shader, pname, intBuffer);
    intBuffer.get(params, offset, length);
  }

  @Override
  public void glGetShaderiv(int shader, int pname, IntBuffer params) {
    GLES20.glGetShader(shader, pname, params);
  }

  @Override
  public void glGetShaderPrecisionFormat(int shadertype, int precisiontype, int[] range,
      int rangeOffset, int[] precision, int precisionOffset) {
    throw new UnsupportedOperationException("NYI");
  }

  @Override
  public void glGetShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range,
      IntBuffer precision) {
    GLES20.glGetShaderPrecisionFormat(shadertype, precisiontype, range, precision);
  }

  @Override
  public void glGetShaderSource(int shader, int bufsize, int[] length, int lengthOffset,
      byte[] source, int sourceOffset) {
    throw new UnsupportedOperationException("NYI");
  }

  @Override
  public void glGetShaderSource(int shader, int bufsize, IntBuffer length, ByteBuffer source) {
    throw new UnsupportedOperationException("NYI");
  }

  @Override
  public String glGetString(int name) {
    return GLES20.glGetString(name);
  }

  @Override
  public void glGetTexParameterfv(int target, int pname, float[] params, int offset) {
    final int length = params.length - offset;
    resizeFloatBuffer(length);
    GLES20.glGetTexParameter(target, pname, floatBuffer);
    floatBuffer.get(params, offset, length);
  }

  @Override
  public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
    GLES20.glGetTexParameter(target, pname, params);
  }

  @Override
  public void glGetTexParameteriv(int target, int pname, int[] params, int offset) {
    final int length = params.length - offset;
    resizeIntBuffer(length);
    GLES20.glGetTexParameter(target, pname, intBuffer);
    intBuffer.get(params, offset, length);
  }

  @Override
  public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
    GLES20.glGetTexParameter(target, pname, params);
  }

  @Override
  public void glGetUniformfv(int program, int location, float[] params, int offset) {
    final int length = params.length - offset;
    resizeFloatBuffer(length);
    GLES20.glGetUniform(program, location, floatBuffer);
    floatBuffer.get(params, offset, length);
  }

  @Override
  public void glGetUniformfv(int program, int location, FloatBuffer params) {
    GLES20.glGetUniform(program, location, params);
  }

  @Override
  public void glGetUniformiv(int program, int location, int[] params, int offset) {
    final int length = params.length - offset;
    resizeIntBuffer(length);
    GLES20.glGetUniform(program, location, intBuffer);
    intBuffer.get(params, offset, length);
  }

  @Override
  public void glGetUniformiv(int program, int location, IntBuffer params) {
    GLES20.glGetUniform(program, location, params);
  }

  @Override
  public int glGetUniformLocation(int program, String name) {
    return GLES20.glGetUniformLocation(program, name);
  }

  @Override
  public void glGetVertexAttribfv(int index, int pname, float[] params, int offset) {
    final int length = params.length - offset;
    resizeFloatBuffer(length);
    GLES20.glGetVertexAttrib(index, pname, floatBuffer);
    floatBuffer.get(params, offset, length);
  }

  @Override
  public void glGetVertexAttribfv(int index, int pname, FloatBuffer params) {
    GLES20.glGetVertexAttrib(index, pname, params);
  }

  @Override
  public void glGetVertexAttribiv(int index, int pname, int[] params, int offset) {
    final int length = params.length - offset;
    resizeIntBuffer(length);
    GLES20.glGetVertexAttrib(index, pname, intBuffer);
    intBuffer.get(params, offset, length);
  }

  @Override
  public void glGetVertexAttribiv(int index, int pname, IntBuffer params) {
    GLES20.glGetVertexAttrib(index, pname, params);
  }

  @Override
  public void glHint(int target, int mode) {
    GLES20.glHint(target, mode);
  }

  @Override
  public boolean glIsBuffer(int buffer) {
    return GLES20.glIsBuffer(buffer);
  }

  @Override
  public boolean glIsEnabled(int cap) {
    return GLES20.glIsEnabled(cap);
  }

  @Override
  public boolean glIsFramebuffer(int framebuffer) {
    return GLES20.glIsFramebuffer(framebuffer);
  }

  @Override
  public boolean glIsProgram(int program) {
    return GLES20.glIsProgram(program);
  }

  @Override
  public boolean glIsRenderbuffer(int renderbuffer) {
    return GLES20.glIsRenderbuffer(renderbuffer);
  }

  @Override
  public boolean glIsShader(int shader) {
    return GLES20.glIsShader(shader);
  }

  @Override
  public boolean glIsTexture(int texture) {
    return GLES20.glIsTexture(texture);
  }

  @Override
  public boolean glIsVBOArrayEnabled() {
    // TODO(jonagill) Test!
    return isExtensionAvailable("vertex_buffer_object");
  }

  @Override
  public boolean glIsVBOElementEnabled() {
    return glIsVBOArrayEnabled();
  }

  @Override
  public void glLineWidth(float width) {
    GLES20.glLineWidth(width);
  }

  @Override
  public void glLinkProgram(int program) {
    GLES20.glLinkProgram(program);
  }

  @Override
  public ByteBuffer glMapBuffer(int arg0, int arg1) throws RuntimeException {
    throw new RuntimeException("glMapBuffer() not supported.");
  }

  @Override
  public void glPixelStorei(int pname, int param) {
    GLES20.glPixelStorei(pname, param);
  }

  @Override
  public void glPolygonOffset(float factor, float units) {
    GLES20.glPolygonOffset(factor, units);
  }

  @Override
  public void glProgramBinary(int arg0, int arg1, Buffer arg2, int arg3) throws RuntimeException {
    throw new RuntimeException("glProgramBinary() not supported.");
  }

  @Override
  public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
    if (pixels instanceof ByteBuffer)
      GLES20.glReadPixels(x, y, width, height, format, type, (ByteBuffer) pixels);
    else if (pixels instanceof ShortBuffer)
      GLES20.glReadPixels(x, y, width, height, format, type, (ShortBuffer) pixels);
    else if (pixels instanceof IntBuffer)
      GLES20.glReadPixels(x, y, width, height, format, type, (IntBuffer) pixels);
    else if (pixels instanceof FloatBuffer)
      GLES20.glReadPixels(x, y, width, height, format, type, (FloatBuffer) pixels);
    else
      throw new RuntimeException("Can't use " + pixels.getClass().getName()
          + " with this method. Use ByteBuffer, "
          + "ShortBuffer, IntBuffer or FloatBuffer instead. Blame LWJGL");
  }

  @Override
  public void glReadPixels(int x, int y, int width, int height, int format, int type,
      int pixelsBufferOffset) throws RuntimeException {
    throw new RuntimeException(
        "Not supported.  Please call glReadPixels(int, int, int, int, int, int, Buffer) instead");
  }

  @Override
  public void glReleaseShaderCompiler() {
    GLES20.glReleaseShaderCompiler();
  }

  @Override
  public void glRenderbufferStorage(int target, int internalformat, int width, int height) {
    GLES20.glRenderbufferStorage(target, internalformat, width, height);
  }

  @Override
  public void glSampleCoverage(float value, boolean invert) {
    GLES20.glSampleCoverage(value, invert);
  }

  @Override
  public void glScissor(int x, int y, int width, int height) {
    GLES20.glScissor(x, y, width, height);
  }

  @Override
  public void glShaderBinary(int n, int[] shaders, int offset, int binaryformat, Buffer binary,
      int length) {
    throw new UnsupportedOperationException("NYI");
  }

  @Override
  public void glShaderBinary(int n, IntBuffer shaders, int binaryformat, Buffer binary, int length) {
    throw new UnsupportedOperationException("unsupported, won't implement");
  }

  @Override
  public void glShaderSource(int shader, int count, String[] strings, int[] length, int lengthOffset) {
    int totalLength = 0;
    for (int i = lengthOffset; i < length.length; i++) {
      totalLength += length[i];
    }
    StringBuilder builder = new StringBuilder(totalLength);

    for (int j = 0; j < count; j++) {
      builder.append(strings[j], 0, length[j]);
    }

    GLES20.glShaderSource(shader, builder.toString());
    // TODO(jonagill): Test me!
  }

  @Override
  public void glShaderSource(int shader, int count, String[] strings, IntBuffer length) {
    glShaderSource(shader, count, strings, length.array(), 0);
  }

  @Override
  public void glShaderSource(int shader, String string) {
    GLES20.glShaderSource(shader, string);
  }

  @Override
  public void glStencilFunc(int func, int ref, int mask) {
    GLES20.glStencilFunc(func, ref, mask);

  }

  @Override
  public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
    GLES20.glStencilFuncSeparate(face, func, ref, mask);

  }

  @Override
  public void glStencilMask(int mask) {
    GLES20.glStencilMask(mask);
  }

  @Override
  public void glStencilMaskSeparate(int face, int mask) {
    GLES20.glStencilMaskSeparate(face, mask);
  }

  @Override
  public void glStencilOp(int fail, int zfail, int zpass) {
    GLES20.glStencilOp(fail, zfail, zpass);
  }

  @Override
  public void glStencilOpSeparate(int face, int fail, int zfail, int zpass) {
    GLES20.glStencilOpSeparate(face, fail, zfail, zpass);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height,
      int border, int format, int type, Buffer pixels) {
    if (pixels instanceof ByteBuffer || pixels == null)
      GLES20.glTexImage2D(target, level, internalformat, width, height, border, format, type,
          (ByteBuffer) pixels);
    else if (pixels instanceof ShortBuffer)
      GLES20.glTexImage2D(target, level, internalformat, width, height, border, format, type,
          (ShortBuffer) pixels);
    else if (pixels instanceof IntBuffer)
      GLES20.glTexImage2D(target, level, internalformat, width, height, border, format, type,
          (IntBuffer) pixels);
    else if (pixels instanceof FloatBuffer)
      GLES20.glTexImage2D(target, level, internalformat, width, height, border, format, type,
          (FloatBuffer) pixels);
    else
      throw new RuntimeException("Can't use " + pixels.getClass().getName()
          + " with this method. Use ByteBuffer, "
          + "ShortBuffer, IntBuffer, FloatBuffer instead. Blame LWJGL");
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height,
      int border, int format, int type, int pixels) {
    throw new RuntimeException(
        "Not supported.  Please call glTexImage2D(int, int, int, int, int, int, int, int, Buffer) instead");
  }

  @Override
  public void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6,
      int arg7, int arg8, Buffer arg9) throws RuntimeException {
    throw new RuntimeException("glTexImage3D not supported!");
  }

  @Override
  public void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6,
      int arg7, int arg8, int arg9) throws RuntimeException {
    throw new RuntimeException("glTexImage3D not supported!");
  }

  @Override
  public void glTexParameterf(int target, int pname, float param) {
    GLES20.glTexParameterf(target, pname, param);
  }

  @Override
  public void glTexParameterfv(int target, int pname, float[] params, int offset) {
    final int length = params.length - offset;
    setFloatBuffer(params, offset, length);
    GLES20.glTexParameter(target, pname, floatBuffer);
  }

  @Override
  public void glTexParameterfv(int target, int pname, FloatBuffer params) {
    GLES20.glTexParameter(target, pname, params);
  }

  @Override
  public void glTexParameteri(int target, int pname, int param) {
    GLES20.glTexParameteri(target, pname, param);
  }

  @Override
  public void glTexParameteriv(int target, int pname, int[] params, int offset) {
    final int length = params.length - offset;
    setIntBuffer(params, offset, length);
    GLES20.glTexParameter(target, pname, intBuffer);
  }

  @Override
  public void glTexParameteriv(int target, int pname, IntBuffer params) {
    GLES20.glTexParameter(target, pname, params);
  }

  @Override
  public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width,
      int height, int format, int type, Buffer pixels) {
    if (pixels instanceof ByteBuffer)
      GLES20.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
          (ByteBuffer) pixels);
    else if (pixels instanceof ShortBuffer)
      GLES20.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
          (ShortBuffer) pixels);
    else if (pixels instanceof IntBuffer)
      GLES20.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
          (IntBuffer) pixels);
    else if (pixels instanceof FloatBuffer)
      GLES20.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type,
          (FloatBuffer) pixels);
    else
      throw new RuntimeException("Can't use " + pixels.getClass().getName()
          + " with this method. Use ByteBuffer, " //
          + "ShortBuffer, IntBuffer or FloatBuffer instead. Blame LWJGL");
  }

  @Override
  public void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6,
      int arg7, int arg8) throws RuntimeException {
    throw new RuntimeException("glTexSubImage2D(int, int, int, int, int, int,"
        + " int, int, int) not supported.");
  }

  @Override
  public void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6,
      int arg7, int arg8, int arg9, Buffer arg10) throws RuntimeException {
    throw new RuntimeException("glTexSubImage3D not supported!");

  }

  @Override
  public void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6,
      int arg7, int arg8, int arg9, int arg10) throws RuntimeException {
    throw new RuntimeException("glTexSubImage3D not supported!");

  }

  @Override
  public void glUniform1f(int location, float x) {
    GLES20.glUniform1f(location, x);
  }

  @Override
  public void glUniform1fv(int location, int count, float[] v, int offset) {
    setFloatBuffer(v, offset, count);
    GLES20.glUniform1(location, floatBuffer);
  }

  @Override
  public void glUniform1fv(int location, int count, FloatBuffer v) {
    GLES20.glUniform1(location, v);
  }

  @Override
  public void glUniform1i(int location, int x) {
    GLES20.glUniform1i(location, x);
  }

  @Override
  public void glUniform1iv(int location, int count, int[] v, int offset) {
    setIntBuffer(v, offset, count);
    GLES20.glUniform1(location, intBuffer);
  }

  @Override
  public void glUniform1iv(int location, int count, IntBuffer v) {
    GLES20.glUniform1(location, v);
  }

  @Override
  public void glUniform2f(int location, float x, float y) {
    GLES20.glUniform2f(location, x, y);
  }

  @Override
  public void glUniform2fv(int location, int count, float[] v, int offset) {
    setFloatBuffer(v, offset, count);
    GLES20.glUniform2(location, floatBuffer);
  }

  @Override
  public void glUniform2fv(int location, int count, FloatBuffer v) {
    GLES20.glUniform2(location, v);
  }

  @Override
  public void glUniform2i(int location, int x, int y) {
    GLES20.glUniform2i(location, x, y);
  }

  @Override
  public void glUniform2iv(int location, int count, int[] v, int offset) {
    setIntBuffer(v, offset, count);
    GLES20.glUniform2(location, intBuffer);
  }

  @Override
  public void glUniform2iv(int location, int count, IntBuffer v) {
    GLES20.glUniform2(location, v);
  }

  @Override
  public void glUniform3f(int location, float x, float y, float z) {
    GLES20.glUniform3f(location, x, y, z);
  }

  @Override
  public void glUniform3fv(int location, int count, float[] v, int offset) {
    setFloatBuffer(v, offset, count);
    GLES20.glUniform3(location, floatBuffer);
  }

  @Override
  public void glUniform3fv(int location, int count, FloatBuffer v) {
    GLES20.glUniform3(location, v);
  }

  @Override
  public void glUniform3i(int location, int x, int y, int z) {
    GLES20.glUniform3i(location, x, y, z);
  }

  @Override
  public void glUniform3iv(int location, int count, int[] v, int offset) {
    setIntBuffer(v, offset, count);
    GLES20.glUniform3(location, intBuffer);
  }

  @Override
  public void glUniform3iv(int location, int count, IntBuffer v) {
    GLES20.glUniform3(location, v);
  }

  @Override
  public void glUniform4f(int location, float x, float y, float z, float w) {
    GLES20.glUniform4f(location, x, y, z, w);
  }

  @Override
  public void glUniform4fv(int location, int count, float[] v, int offset) {
    setFloatBuffer(v, offset, count);
    GLES20.glUniform4(location, floatBuffer);
  }

  @Override
  public void glUniform4fv(int location, int count, FloatBuffer v) {
    GLES20.glUniform4(location, v);
  }

  @Override
  public void glUniform4i(int location, int x, int y, int z, int w) {
    GLES20.glUniform4i(location, x, y, z, w);
  }

  @Override
  public void glUniform4iv(int location, int count, int[] v, int offset) {
    setIntBuffer(v, offset, count);
    GLES20.glUniform4(location, intBuffer);
  }

  @Override
  public void glUniform4iv(int location, int count, IntBuffer v) {
    GLES20.glUniform4(location, v);
  }

  @Override
  public void glUniformMatrix2fv(int location, int count, boolean transpose, float[] value,
      int offset) {
    setFloatBuffer(value, offset, count);
    GLES20.glUniformMatrix2(location, transpose, floatBuffer);
  }

  @Override
  public void glUniformMatrix2fv(int location, int count, boolean transpose, FloatBuffer value) {
    GLES20.glUniformMatrix2(location, transpose, value);
  }

  @Override
  public void glUniformMatrix3fv(int location, int count, boolean transpose, float[] value,
      int offset) {
    setFloatBuffer(value, offset, count);
    // glUniformMatrix3 calls nglUniformMatrix3fv
    GLES20.glUniformMatrix3(location, transpose, floatBuffer);
  }

  @Override
  public void glUniformMatrix3fv(int location, int count, boolean transpose, FloatBuffer value) {
    GLES20.glUniformMatrix3(location, transpose, value);
  }

  @Override
  public void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value,
      int offset) {
    setFloatBuffer(value, offset, count);
    GLES20.glUniformMatrix4(location, transpose, floatBuffer);
  }

  @Override
  public void glUniformMatrix4fv(int location, int count, boolean transpose, FloatBuffer value) {
    GLES20.glUniformMatrix4(location, transpose, value);
  }

  @Override
  public boolean glUnmapBuffer(int arg0) throws RuntimeException {
    throw new RuntimeException("glUnmapBuffer() not supported.");
  }

  @Override
  public void glUseProgram(int program) {
    GLES20.glUseProgram(program);
  }

  @Override
  public void glValidateProgram(int program) {
    GLES20.glValidateProgram(program);
  }

  @Override
  public void glVertexAttrib1f(int indx, float x) {
    GLES20.glVertexAttrib1f(indx, x);
  }

  @Override
  public void glVertexAttrib1fv(int indx, float[] values, int offset) {
    GLES20.glVertexAttrib1f(indx, values[indx + offset]);

  }

  @Override
  public void glVertexAttrib1fv(int indx, FloatBuffer values) {
    GLES20.glVertexAttrib1f(indx, values.get());
  }

  @Override
  public void glVertexAttrib2f(int indx, float x, float y) {
    GLES20.glVertexAttrib2f(indx, x, y);
  }

  @Override
  public void glVertexAttrib2fv(int indx, float[] values, int offset) {
    GLES20.glVertexAttrib2f(indx, values[indx + offset], values[indx + 1 + offset]);
  }

  @Override
  public void glVertexAttrib2fv(int indx, FloatBuffer values) {
    GLES20.glVertexAttrib2f(indx, values.get(), values.get());
  }

  @Override
  public void glVertexAttrib3f(int indx, float x, float y, float z) {
    GLES20.glVertexAttrib3f(indx, x, y, z);
  }

  @Override
  public void glVertexAttrib3fv(int indx, float[] values, int offset) {
    GLES20.glVertexAttrib3f(indx, values[indx + offset], values[indx + 1 + offset], values[indx + 2
        + offset]);
  }

  @Override
  public void glVertexAttrib3fv(int indx, FloatBuffer values) {
    GLES20.glVertexAttrib3f(indx, values.get(), values.get(), values.get());
  }

  @Override
  public void glVertexAttrib4f(int indx, float x, float y, float z, float w) {
    GLES20.glVertexAttrib4f(indx, x, y, z, w);
  }

  @Override
  public void glVertexAttrib4fv(int indx, float[] values, int offset) {
    GLES20.glVertexAttrib4f(indx, values[indx + offset], values[indx + 1 + offset], values[indx + 2
        + offset], values[indx + 3 + offset]);
  }

  @Override
  public void glVertexAttrib4fv(int indx, FloatBuffer values) {
    GLES20.glVertexAttrib4f(indx, values.get(), values.get(), values.get(), values.get());
  }

  @Override
  public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride,
      Buffer ptr) {
    // GL20.glVertexAttribPointer(indx, size, type, normalized, stride,
    // BufferUtils.getOffset(ptr));
    if (ptr instanceof FloatBuffer) {
      GLES20.glVertexAttribPointer(indx, size, normalized, stride, (FloatBuffer) ptr);
    } else if (ptr instanceof ByteBuffer) {
      switch (type) {
        case GL_BYTE:
          GLES20.glVertexAttribPointer(indx, size, false, normalized, stride, ((ByteBuffer) ptr));
          break;
        case GL_FLOAT:
          GLES20.glVertexAttribPointer(indx, size, normalized, stride,
              ((ByteBuffer) ptr).asFloatBuffer());
          break;
        case GL_UNSIGNED_BYTE:
          GLES20.glVertexAttribPointer(indx, size, true, normalized, stride, (ByteBuffer) ptr);
          break;
        case GL_UNSIGNED_SHORT:
          GLES20.glVertexAttribPointer(indx, size, true, normalized, stride, (ByteBuffer) ptr);
          break;
        case GL_SHORT:
          GLES20.glVertexAttribPointer(indx, size, false, normalized, stride, (ByteBuffer) ptr);
          break;
        default:
          throw new RuntimeException("NYI for type " + Integer.toHexString(type));
      }
    } else if (ptr instanceof ShortBuffer) {
      throw new RuntimeException("LWJGL does not support short buffers in glVertexAttribPointer.");
    } else {
      throw new RuntimeException("NYI for " + ptr.getClass());
    }
  }

  @Override
  public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride,
      int ptr) {
    GLES20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
  }

  @Override
  public void glViewport(int x, int y, int width, int height) {
    GLES20.glViewport(x, y, width, height);
  }

  @Override
  public boolean hasGLSL() {
    return true;
  }

  @Override
  public boolean isExtensionAvailable(String extension) {
    String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
    return extensions.contains(extension);
  }

  @Override
  public boolean isFunctionAvailable(String function) {
    Method[] functions = this.getClass().getMethods();
    for (int i = 0; i < functions.length; i++) {
      if (function == functions[i].getName())
        return true;
    }
    return false;
    // TODO(jonagill): Test!
  }
}
