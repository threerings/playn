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

import javax.media.opengl.GL2;
import java.nio.*;

final class JavaJOGL20 implements playn.core.gl.GL20 {

  private final GL2 gl2;

  public JavaJOGL20(GL2 gl2) {
    this.gl2 = gl2;
  }

  @Override
  public String getPlatformGLExtensions() {
    return (String) gl2.getPlatformGLExtensions();
  }

  @Override
  public int getSwapInterval() {
    return gl2.getSwapInterval();
  }

  @Override
  public void glActiveTexture(int texture) {
    gl2.glActiveTexture(texture);
  }

  @Override
  public void glAttachShader(int program, int shader) {
    gl2.glAttachShader(program, shader);
  }

  @Override
  public void glBindAttribLocation(int program, int index, String name) {
    gl2.glBindAttribLocation(program, index, name);
  }

  @Override
  public void glBindBuffer(int target, int buffer) {
    gl2.glBindBuffer(target, buffer);
  }

  @Override
  public void glBindFramebuffer(int target, int framebuffer) {
    gl2.glBindFramebuffer(target, framebuffer);
  }

  @Override
  public void glBindRenderbuffer(int target, int renderbuffer) {
    gl2.glBindRenderbuffer(target, renderbuffer);
  }

  @Override
  public void glBindTexture(int target, int texture) {
    gl2.glBindTexture(target, texture);
  }

  @Override
  public void glBlendColor(float red, float green, float blue, float alpha) {
    gl2.glBlendColor(red, green, blue, alpha);
  }

  @Override
  public void glBlendEquation(int mode) {
    gl2.glBlendEquation(mode);
  }

  @Override
  public void glBlendEquationSeparate(int modeRGB, int modeAlpha) {
    gl2.glBlendEquationSeparate(modeRGB, modeAlpha);
  }

  @Override
  public void glBlendFunc(int sfactor, int dfactor) {
    gl2.glBlendFunc(sfactor, dfactor);
  }

  @Override
  public void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
    gl2.glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
  }

  @Override
  public void glBufferData(int target, int size, Buffer data, int usage) {
    gl2.glBufferData(target, size, data, usage);
  }

  @Override
  public void glBufferSubData(int target, int offset, int size, Buffer data) {
    gl2.glBufferSubData(target, offset, size, data);
  }

  @Override
  public int glCheckFramebufferStatus(int target) {
    return gl2.glCheckFramebufferStatus(target);
  }

  @Override
  public void glClear(int mask) {
    gl2.glClear(mask);
  }

  @Override
  public void glClearColor(float red, float green, float blue, float alpha) {
    gl2.glClearColor(red, green, blue, alpha);
  }

  @Override
  public void glClearDepth(double depth) {
    gl2.glClearDepth(depth);
  }

  @Override
  public void glClearDepthf(float depth) {
    gl2.glClearDepthf(depth);
  }

  @Override
  public void glClearStencil(int s) {
    gl2.glClearStencil(s);
  }

  @Override
  public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
    gl2.glColorMask(red, green, blue, alpha);
  }

  @Override
  public void glCompileShader(int shader) {
    gl2.glCompileShader(shader);
  }

  @Override
  public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize, Buffer data) {
    gl2.glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data);
  }

  @Override
  public void glCompressedTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
    gl2.glCompressedTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
  }

  @Override
  public void glCompressedTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, Buffer arg8) {
    gl2.glCompressedTexImage3D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
  }

  @Override
  public void glCompressedTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8) {
    gl2.glCompressedTexImage3D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
  }

  @Override
  public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, Buffer data) {
    gl2.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);
  }

  @Override
  public void glCompressedTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8) {
    gl2.glCompressedTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
  }

  @Override
  public void glCompressedTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, Buffer arg10) {
    gl2.glCompressedTexSubImage3D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
  }

  @Override
  public void glCompressedTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10) {
    gl2.glCompressedTexSubImage3D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
  }

  @Override
  public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border) {
    gl2.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
  }

  @Override
  public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
    gl2.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
  }

  @Override
  public void glCopyTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8) {
    gl2.glCopyTexSubImage3D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
  }

  @Override
  public int glCreateProgram() {
    return gl2.glCreateProgram();
  }

  @Override
  public int glCreateShader(int type) {
    return gl2.glCreateShader(type);
  }

  @Override
  public void glCullFace(int mode) {
    gl2.glCullFace(mode);
  }

  @Override
  public void glDeleteBuffers(int n, int[] buffers, int offset) {
    gl2.glDeleteBuffers(n, buffers, offset);
  }

  @Override
  public void glDeleteBuffers(int n, IntBuffer buffers) {
    gl2.glDeleteBuffers(n, buffers);
  }

  @Override
  public void glDeleteFramebuffers(int n, int[] framebuffers, int offset) {
    gl2.glDeleteFramebuffers(n, framebuffers, offset);
  }

  @Override
  public void glDeleteFramebuffers(int n, IntBuffer framebuffers) {
    gl2.glDeleteFramebuffers(n, framebuffers);
  }

  @Override
  public void glDeleteProgram(int program) {
    gl2.glDeleteProgram(program);
  }

  @Override
  public void glDeleteRenderbuffers(int n, int[] renderbuffers, int offset) {
    gl2.glDeleteRenderbuffers(n, renderbuffers, offset);
  }

  @Override
  public void glDeleteRenderbuffers(int n, IntBuffer renderbuffers) {
    gl2.glDeleteRenderbuffers(n, renderbuffers);
  }

  @Override
  public void glDeleteShader(int shader) {
    gl2.glDeleteShader(shader);
  }

  @Override
  public void glDeleteTextures(int n, int[] textures, int offset) {
    gl2.glDeleteTextures(n, textures, offset);
  }

  @Override
  public void glDeleteTextures(int n, IntBuffer textures) {
    gl2.glDeleteTextures(n, textures);
  }

  @Override
  public void glDepthFunc(int func) {
    gl2.glDepthFunc(func);
  }

  @Override
  public void glDepthMask(boolean flag) {
    gl2.glDepthMask(flag);
  }

  @Override
  public void glDepthRange(double zNear, double zFar) {
    gl2.glDepthRange(zNear, zFar);
  }

  @Override
  public void glDepthRangef(float zNear, float zFar) {
    gl2.glDepthRangef(zNear, zFar);
  }

  @Override
  public void glDetachShader(int program, int shader) {
    gl2.glDetachShader(program, shader);
  }

  @Override
  public void glDisable(int cap) {
    gl2.glDisable(cap);
  }

  @Override
  public void glDisableVertexAttribArray(int index) {
    gl2.glDisableVertexAttribArray(index);
  }

  @Override
  public void glDrawArrays(int mode, int first, int count) {
    gl2.glDrawArrays(mode, first, count);
  }

  @Override
  public void glDrawElements(int mode, int count, int type, Buffer indices) {
    gl2.glDrawElements(mode, count, type, indices);
  }

  @Override
  public void glDrawElements(int mode, int count, int type, int offset) {
    gl2.glDrawElements(mode, count, type, offset);
  }

  @Override
  public void glEnable(int cap) {
    gl2.glEnable(cap);
  }

  @Override
  public void glEnableVertexAttribArray(int index) {
    gl2.glEnableVertexAttribArray(index);
  }

  @Override
  public void glFinish() {
    gl2.glFinish();
  }

  @Override
  public void glFlush() {
    gl2.glFlush();
  }

  @Override
  public void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
    gl2.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
  }

  @Override
  public void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
    gl2.glFramebufferTexture2D(target, attachment, textarget, texture, level);
  }

  @Override
  public void glFramebufferTexture3D(int target, int attachment, int textarget, int texture, int level, int zoffset) {
    gl2.glFramebufferTexture3D(target, attachment, textarget, texture, level, zoffset);
  }

  @Override
  public void glFrontFace(int mode) {
    gl2.glFrontFace(mode);
  }

  @Override
  public void glGenBuffers(int n, int[] buffers, int offset) {
    gl2.glGenBuffers(n, buffers, offset);
  }

  @Override
  public void glGenBuffers(int n, IntBuffer buffers) {
    gl2.glGenBuffers(n, buffers);
  }

  @Override
  public void glGenerateMipmap(int target) {
    gl2.glGenerateMipmap(target);
  }

  @Override
  public void glGenFramebuffers(int n, int[] framebuffers, int offset) {
    gl2.glGenFramebuffers(n, framebuffers, offset);
  }

  @Override
  public void glGenFramebuffers(int n, IntBuffer framebuffers) {
    gl2.glGenFramebuffers(n, framebuffers);
  }

  @Override
  public void glGenRenderbuffers(int n, int[] renderbuffers, int offset) {
    gl2.glGenRenderbuffers(n, renderbuffers, offset);
  }

  @Override
  public void glGenRenderbuffers(int n, IntBuffer renderbuffers) {
    gl2.glGenRenderbuffers(n, renderbuffers);
  }

  @Override
  public void glGenTextures(int n, int[] textures, int offset) {
    gl2.glGenTextures(n, textures, offset);
  }

  @Override
  public void glGenTextures(int n, IntBuffer textures) {
    gl2.glGenTextures(n, textures);
  }

  @Override
  public void glGetActiveAttrib(int program, int index, int bufsize, int[] length, int lengthOffset, int[] size, int sizeOffset, int[] type, int typeOffset, byte[] name, int nameOffset) {
    gl2.glGetActiveAttrib(program, index, bufsize, length, lengthOffset, size, sizeOffset, type, typeOffset, name, nameOffset);
  }

  @Override
  public void glGetActiveAttrib(int program, int index, int bufsize, IntBuffer length, IntBuffer size, IntBuffer type, ByteBuffer name) {
    gl2.glGetActiveAttrib(program, index, bufsize, length, size, type, name);
  }

  @Override
  public void glGetActiveUniform(int program, int index, int bufsize, int[] length, int lengthOffset, int[] size, int sizeOffset, int[] type, int typeOffset, byte[] name, int nameOffset) {
    gl2.glGetActiveUniform(program, index, bufsize, length, lengthOffset, size, sizeOffset, type, typeOffset, name, nameOffset);
  }

  @Override
  public void glGetActiveUniform(int program, int index, int bufsize, IntBuffer length, IntBuffer size, IntBuffer type, ByteBuffer name) {
    gl2.glGetActiveUniform(program, index, bufsize, length, size, type, name);
  }

  @Override
  public void glGetAttachedShaders(int program, int maxcount, int[] count, int countOffset, int[] shaders, int shadersOffset) {
    gl2.glGetAttachedShaders(program, maxcount, count, countOffset, shaders, shadersOffset);
  }

  @Override
  public void glGetAttachedShaders(int program, int maxcount, IntBuffer count, IntBuffer shaders) {
    gl2.glGetAttachedShaders(program, maxcount, count, shaders);
  }

  @Override
  public int glGetAttribLocation(int program, String name) {
    return gl2.glGetAttribLocation(program, name);
  }

  @Override
  public boolean glGetBoolean(int pname) {
    byte[] out = new byte[1];
    gl2.glGetBooleanv(pname, out, 0);
    return out[0] != 0;
  }

  @Override
  public void glGetBooleanv(int pname, byte[] params, int offset) {
    gl2.glGetBooleanv(pname, params, offset);
  }

  @Override
  public void glGetBooleanv(int pname, ByteBuffer params) {
    gl2.glGetBooleanv(pname, params);
  }

  @Override
  public int glGetBoundBuffer(int arg0) {
    return gl2.getBoundBuffer(arg0);
  }

  @Override
  public void glGetBufferParameteriv(int target, int pname, int[] params, int offset) {
    gl2.glGetBufferParameteriv(target, pname, params, offset);
  }

  @Override
  public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
    gl2.glGetBufferParameteriv(target, pname, params);
  }

  @Override
  public int glGetError() {
    return gl2.glGetError();
  }

  @Override
  public float glGetFloat(int pname) {
    float[] out = new float[1];
    gl2.glGetFloatv(pname, out, 0);
    return out[0];
  }

  @Override
  public void glGetFloatv(int pname, float[] params, int offset) {
    gl2.glGetFloatv(pname, params, offset);
  }

  @Override
  public void glGetFloatv(int pname, FloatBuffer params) {
    gl2.glGetFloatv(pname, params);
  }

  @Override
  public void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, int[] params, int offset) {
    gl2.glGetFramebufferAttachmentParameteriv(target, attachment, pname, params, offset);
  }

  @Override
  public void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, IntBuffer params) {
    gl2.glGetFramebufferAttachmentParameteriv(target, attachment, pname, params);
  }

  @Override
  public int glGetInteger(int pname) {
    int[] out = new int[1];
    gl2.glGetIntegerv(pname, out, 0);
    return out[0];
  }

  @Override
  public void glGetIntegerv(int pname, int[] params, int offset) {
    gl2.glGetIntegerv(pname, params, offset);
  }

  @Override
  public void glGetIntegerv(int pname, IntBuffer params) {
    gl2.glGetIntegerv(pname, params);
  }

  @Override
  public void glGetProgramBinary(int arg0, int arg1, int[] arg2, int arg3, int[] arg4, int arg5, Buffer arg6) {
    gl2.glGetProgramBinary(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
  }

  @Override
  public void glGetProgramBinary(int arg0, int arg1, IntBuffer arg2, IntBuffer arg3, Buffer arg4) {
    gl2.glGetProgramBinary(arg0, arg1, arg2, arg3, arg4);
  }

  @Override
  public void glGetProgramInfoLog(int program, int bufsize, int[] length, int lengthOffset, byte[] infolog, int infologOffset) {
    gl2.glGetProgramInfoLog(program, bufsize, length, lengthOffset, infolog, infologOffset);
  }

  @Override
  public void glGetProgramInfoLog(int program, int bufsize, IntBuffer length, ByteBuffer infolog) {
    gl2.glGetProgramInfoLog(program, bufsize, length, infolog);
  }

  @Override
  public String glGetProgramInfoLog(int program) {
    ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 10);
    buffer.order(ByteOrder.nativeOrder());
    ByteBuffer tmp = ByteBuffer.allocateDirect(4);
    tmp.order(ByteOrder.nativeOrder());
    IntBuffer intBuffer = tmp.asIntBuffer();
    gl2.glGetProgramInfoLog(program, buffer.capacity(), intBuffer, buffer);
    int numBytes = intBuffer.get(0);
    byte[] bytes = new byte[numBytes];
    buffer.get(bytes);
    return new String(bytes);
  }

  @Override
  public void glGetProgramiv(int program, int pname, int[] params, int offset) {
    gl2.glGetProgramiv(program, pname, params, offset);
  }

  @Override
  public void glGetProgramiv(int program, int pname, IntBuffer params) {
    gl2.glGetProgramiv(program, pname, params);
  }

  @Override
  public void glGetRenderbufferParameteriv(int target, int pname, int[] params, int offset) {
    gl2.glGetRenderbufferParameteriv(target, pname, params, offset);
  }

  @Override
  public void glGetRenderbufferParameteriv(int target, int pname, IntBuffer params) {
    gl2.glGetRenderbufferParameteriv(target, pname, params);
  }

  @Override
  public void glGetShaderInfoLog(int shader, int bufsize, int[] length, int lengthOffset, byte[] infolog, int infologOffset) {
    gl2.glGetShaderInfoLog(shader, bufsize, length, lengthOffset, infolog, infologOffset);
  }

  @Override
  public void glGetShaderInfoLog(int shader, int bufsize, IntBuffer length, ByteBuffer infolog) {
    gl2.glGetShaderInfoLog(shader, bufsize, length, infolog);
  }

  @Override
  public String glGetShaderInfoLog(int shader) {
    ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 10);
    buffer.order(ByteOrder.nativeOrder());
    ByteBuffer tmp = ByteBuffer.allocateDirect(4);
    tmp.order(ByteOrder.nativeOrder());
    IntBuffer intBuffer = tmp.asIntBuffer();
    gl2.glGetShaderInfoLog(shader, buffer.capacity(), intBuffer, buffer);
    int numBytes = intBuffer.get(0);
    byte[] bytes = new byte[numBytes];
    buffer.get(bytes);
    return new String(bytes);
  }

  @Override
  public void glGetShaderiv(int shader, int pname, int[] params, int offset) {
    gl2.glGetShaderiv(shader, pname, params, offset);
  }

  @Override
  public void glGetShaderiv(int shader, int pname, IntBuffer params) {
    gl2.glGetShaderiv(shader, pname, params);
  }

  @Override
  public void glGetShaderPrecisionFormat(int shadertype, int precisiontype, int[] range, int rangeOffset, int[] precision, int precisionOffset) {
    gl2.glGetShaderPrecisionFormat(shadertype, precisiontype, range, rangeOffset, precision, precisionOffset);
  }

  @Override
  public void glGetShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
    gl2.glGetShaderPrecisionFormat(shadertype, precisiontype, range, precision);
  }

  @Override
  public void glGetShaderSource(int shader, int bufsize, int[] length, int lengthOffset, byte[] source, int sourceOffset) {
    gl2.glGetShaderSource(shader, bufsize, length, lengthOffset, source, sourceOffset);
  }

  @Override
  public void glGetShaderSource(int shader, int bufsize, IntBuffer length, ByteBuffer source) {
    gl2.glGetShaderSource(shader, bufsize, length, source);
  }

  @Override
  public String glGetString(int name) {
    return gl2.glGetString(name);
  }

  @Override
  public void glGetTexParameterfv(int target, int pname, float[] params, int offset) {
    gl2.glGetTexParameterfv(target, pname, params, offset);
  }

  @Override
  public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
    gl2.glGetTexParameterfv(target, pname, params);
  }

  @Override
  public void glGetTexParameteriv(int target, int pname, int[] params, int offset) {
    gl2.glGetTexParameteriv(target, pname, params, offset);
  }

  @Override
  public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
    gl2.glGetTexParameteriv(target, pname, params);
  }

  @Override
  public void glGetUniformfv(int program, int location, float[] params, int offset) {
    gl2.glGetUniformfv(program, location, params, offset);
  }

  @Override
  public void glGetUniformfv(int program, int location, FloatBuffer params) {
    gl2.glGetUniformfv(program, location, params);
  }

  @Override
  public void glGetUniformiv(int program, int location, int[] params, int offset) {
    gl2.glGetUniformiv(program, location, params, offset);
  }

  @Override
  public void glGetUniformiv(int program, int location, IntBuffer params) {
    gl2.glGetUniformiv(program, location, params);
  }

  @Override
  public int glGetUniformLocation(int program, String name) {
    return gl2.glGetUniformLocation(program, name);
  }

  @Override
  public void glGetVertexAttribfv(int index, int pname, float[] params, int offset) {
    gl2.glGetVertexAttribfv(index, pname, params, offset);
  }

  @Override
  public void glGetVertexAttribfv(int index, int pname, FloatBuffer params) {
    gl2.glGetVertexAttribfv(index, pname, params);
  }

  @Override
  public void glGetVertexAttribiv(int index, int pname, int[] params, int offset) {
    gl2.glGetVertexAttribiv(index, pname, params, offset);
  }

  @Override
  public void glGetVertexAttribiv(int index, int pname, IntBuffer params) {
    gl2.glGetVertexAttribiv(index, pname, params);
  }

  @Override
  public void glHint(int target, int mode) {
    gl2.glHint(target, mode);
  }

  @Override
  public boolean glIsBuffer(int buffer) {
    return gl2.glIsBuffer(buffer);
  }

  @Override
  public boolean glIsEnabled(int cap) {
    return gl2.glIsEnabled(cap);
  }

  @Override
  public boolean glIsFramebuffer(int framebuffer) {
    return gl2.glIsFramebuffer(framebuffer);
  }

  @Override
  public boolean glIsProgram(int program) {
    return gl2.glIsProgram(program);
  }

  @Override
  public boolean glIsRenderbuffer(int renderbuffer) {
    return gl2.glIsRenderbuffer(renderbuffer);
  }

  @Override
  public boolean glIsShader(int shader) {
    return gl2.glIsShader(shader);
  }

  @Override
  public boolean glIsTexture(int texture) {
    return gl2.glIsTexture(texture);
  }

  @Override
  public boolean glIsVBOArrayEnabled() {
    return gl2.isVBOArrayBound();
  }

  @Override
  public boolean glIsVBOElementEnabled() {
    return gl2.isVBOElementArrayBound();
  }

  @Override
  public void glLineWidth(float width) {
    gl2.glLineWidth(width);
  }

  @Override
  public void glLinkProgram(int program) {
    gl2.glLinkProgram(program);
  }

  @Override
  public ByteBuffer glMapBuffer(int target, int access) {
    return gl2.glMapBuffer(target, access);
  }

  @Override
  public void glPixelStorei(int pname, int param) {
    gl2.glPixelStorei(pname, param);
  }

  @Override
  public void glPolygonOffset(float factor, float units) {
    gl2.glPolygonOffset(factor, units);
  }

  @Override
  public void glProgramBinary(int arg0, int arg1, Buffer arg2, int arg3) {
    gl2.glProgramBinary(arg0, arg1, arg2, arg3);
  }

  @Override
  public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
    gl2.glReadPixels(x, y, width, height, format, type, pixels);
  }

  @Override
  public void glReadPixels(int x, int y, int width, int height, int format, int type, int pixelsBufferOffset) {
    gl2.glReadPixels(x, y, width, height, format, type, pixelsBufferOffset);
  }

  @Override
  public void glReleaseShaderCompiler() {
    gl2.glReleaseShaderCompiler();
  }

  @Override
  public void glRenderbufferStorage(int target, int internalformat, int width, int height) {
    gl2.glRenderbufferStorage(target, internalformat, width, height);
  }

  @Override
  public void glSampleCoverage(float value, boolean invert) {
    gl2.glSampleCoverage(value, invert);
  }

  @Override
  public void glScissor(int x, int y, int width, int height) {
    gl2.glScissor(x, y, width, height);
  }

  @Override
  public void glShaderBinary(int n, int[] shaders, int offset, int binaryformat, Buffer binary, int length) {
    gl2.glShaderBinary(n, shaders, offset, binaryformat, binary, length);
  }

  @Override
  public void glShaderBinary(int n, IntBuffer shaders, int binaryformat, Buffer binary, int length) {
    gl2.glShaderBinary(n, shaders, binaryformat, binary, length);
  }

  @Override
  public void glShaderSource(int shader, int count, String[] strings, int[] length, int lengthOffset) {
    gl2.glShaderSource(shader, count, strings, length, lengthOffset);
  }

  @Override
  public void glShaderSource(int shader, int count, String[] strings, IntBuffer length) {
    gl2.glShaderSource(shader, count, strings, length);
  }

  @Override
  public void glShaderSource(int shader, String string) {
    gl2.glShaderSource(shader, 1, new String[]{string}, null, 0);
  }

  @Override
  public void glStencilFunc(int func, int ref, int mask) {
    gl2.glStencilFunc(func, ref, mask);
  }

  @Override
  public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
    gl2.glStencilFuncSeparate(face, func, ref, mask);
  }

  @Override
  public void glStencilMask(int mask) {
    gl2.glStencilMask(mask);
  }

  @Override
  public void glStencilMaskSeparate(int face, int mask) {
    gl2.glStencilMaskSeparate(face, mask);
  }

  @Override
  public void glStencilOp(int fail, int zfail, int zpass) {
    gl2.glStencilOp(fail, zfail, zpass);
  }

  @Override
  public void glStencilOpSeparate(int face, int fail, int zfail, int zpass) {
    gl2.glStencilOpSeparate(face, fail, zfail, zpass);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels) {
    gl2.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  public void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8) {
    gl2.glTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
  }

  @Override
  public void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, Buffer arg9) {
    gl2.glTexImage3D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
  }

  @Override
  public void glTexImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9) {
    gl2.glTexImage3D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
  }

  @Override
  public void glTexParameterf(int target, int pname, float param) {
    gl2.glTexParameterf(target, pname, param);
  }

  @Override
  public void glTexParameterfv(int target, int pname, float[] params, int offset) {
    gl2.glTexParameterfv(target, pname, params, offset);
  }

  @Override
  public void glTexParameterfv(int target, int pname, FloatBuffer params) {
    gl2.glTexParameterfv(target, pname, params);
  }

  @Override
  public void glTexParameteri(int target, int pname, int param) {
    gl2.glTexParameteri(target, pname, param);
  }

  @Override
  public void glTexParameteriv(int target, int pname, int[] params, int offset) {
    gl2.glTexParameteriv(target, pname, params, offset);
  }

  @Override
  public void glTexParameteriv(int target, int pname, IntBuffer params) {
    gl2.glTexParameteriv(target, pname, params);
  }

  @Override
  public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels) {
    gl2.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
  }

  @Override
  public void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8) {
    gl2.glTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
  }

  @Override
  public void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, Buffer arg10) {
    gl2.glTexSubImage3D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
  }

  @Override
  public void glTexSubImage3D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10) {
    gl2.glTexSubImage3D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
  }

  @Override
  public void glUniform1f(int location, float x) {
    gl2.glUniform1f(location, x);
  }

  @Override
  public void glUniform1fv(int location, int count, float[] v, int offset) {
    gl2.glUniform1fv(location, count, v, offset);
  }

  @Override
  public void glUniform1fv(int location, int count, FloatBuffer v) {
    gl2.glUniform1fv(location, count, v);
  }

  @Override
  public void glUniform1i(int location, int x) {
    gl2.glUniform1i(location, x);
  }

  @Override
  public void glUniform1iv(int location, int count, int[] v, int offset) {
    gl2.glUniform1iv(location, count, v, offset);
  }

  @Override
  public void glUniform1iv(int location, int count, IntBuffer v) {
    gl2.glUniform1iv(location, count, v);
  }

  @Override
  public void glUniform2f(int location, float x, float y) {
    gl2.glUniform2f(location, x, y);
  }

  @Override
  public void glUniform2fv(int location, int count, float[] v, int offset) {
    gl2.glUniform2fv(location, count, v, offset);
  }

  @Override
  public void glUniform2fv(int location, int count, FloatBuffer v) {
    gl2.glUniform2fv(location, count, v);
  }

  @Override
  public void glUniform2i(int location, int x, int y) {
    gl2.glUniform2i(location, x, y);
  }

  @Override
  public void glUniform2iv(int location, int count, int[] v, int offset) {
    gl2.glUniform2iv(location, count, v, offset);
  }

  @Override
  public void glUniform2iv(int location, int count, IntBuffer v) {
    gl2.glUniform2iv(location, count, v);
  }

  @Override
  public void glUniform3f(int location, float x, float y, float z) {
    gl2.glUniform3f(location, x, y, z);
  }

  @Override
  public void glUniform3fv(int location, int count, float[] v, int offset) {
    gl2.glUniform3fv(location, count, v, offset);
  }

  @Override
  public void glUniform3fv(int location, int count, FloatBuffer v) {
    gl2.glUniform3fv(location, count, v);
  }

  @Override
  public void glUniform3i(int location, int x, int y, int z) {
    gl2.glUniform3i(location, x, y, z);
  }

  @Override
  public void glUniform3iv(int location, int count, int[] v, int offset) {
    gl2.glUniform3iv(location, count, v, offset);
  }

  @Override
  public void glUniform3iv(int location, int count, IntBuffer v) {
    gl2.glUniform3iv(location, count, v);
  }

  @Override
  public void glUniform4f(int location, float x, float y, float z, float w) {
    gl2.glUniform4f(location, x, y, z, w);
  }

  @Override
  public void glUniform4fv(int location, int count, float[] v, int offset) {
    gl2.glUniform4fv(location, count, v, offset);
  }

  @Override
  public void glUniform4fv(int location, int count, FloatBuffer v) {
    gl2.glUniform4fv(location, count, v);
  }

  @Override
  public void glUniform4i(int location, int x, int y, int z, int w) {
    gl2.glUniform4i(location, x, y, z, w);
  }

  @Override
  public void glUniform4iv(int location, int count, int[] v, int offset) {
    gl2.glUniform4iv(location, count, v, offset);
  }

  @Override
  public void glUniform4iv(int location, int count, IntBuffer v) {
    gl2.glUniform4iv(location, count, v);
  }

  @Override
  public void glUniformMatrix2fv(int location, int count, boolean transpose, float[] value, int offset) {
    gl2.glUniformMatrix2fv(location, count, transpose, value, offset);
  }

  @Override
  public void glUniformMatrix2fv(int location, int count, boolean transpose, FloatBuffer value) {
    gl2.glUniformMatrix2fv(location, count, transpose, value);
  }

  @Override
  public void glUniformMatrix3fv(int location, int count, boolean transpose, float[] value, int offset) {
    gl2.glUniformMatrix3fv(location, count, transpose, value, offset);
  }

  @Override
  public void glUniformMatrix3fv(int location, int count, boolean transpose, FloatBuffer value) {
    gl2.glUniformMatrix3fv(location, count, transpose, value);
  }

  @Override
  public void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset) {
    gl2.glUniformMatrix4fv(location, count, transpose, value, offset);
  }

  @Override
  public void glUniformMatrix4fv(int location, int count, boolean transpose, FloatBuffer value) {
    gl2.glUniformMatrix4fv(location, count, transpose, value);
  }

  @Override
  public boolean glUnmapBuffer(int arg0) {
    return gl2.glUnmapBuffer(arg0);
  }

  @Override
  public void glUseProgram(int program) {
    gl2.glUseProgram(program);
  }

  @Override
  public void glValidateProgram(int program) {
    gl2.glValidateProgram(program);
  }

  @Override
  public void glVertexAttrib1f(int indx, float x) {
    gl2.glVertexAttrib1f(indx, x);
  }

  @Override
  public void glVertexAttrib1fv(int indx, float[] values, int offset) {
    gl2.glVertexAttrib1fv(indx, values, offset);
  }

  @Override
  public void glVertexAttrib1fv(int indx, FloatBuffer values) {
    gl2.glVertexAttrib1fv(indx, values);
  }

  @Override
  public void glVertexAttrib2f(int indx, float x, float y) {
    gl2.glVertexAttrib2f(indx, x, y);
  }

  @Override
  public void glVertexAttrib2fv(int indx, float[] values, int offset) {
    gl2.glVertexAttrib2fv(indx, values, offset);
  }

  @Override
  public void glVertexAttrib2fv(int indx, FloatBuffer values) {
    gl2.glVertexAttrib2fv(indx, values);
  }

  @Override
  public void glVertexAttrib3f(int indx, float x, float y, float z) {
    gl2.glVertexAttrib3f(indx, x, y, z);
  }

  @Override
  public void glVertexAttrib3fv(int indx, float[] values, int offset) {
    gl2.glVertexAttrib3fv(indx, values, offset);
  }

  @Override
  public void glVertexAttrib3fv(int indx, FloatBuffer values) {
    gl2.glVertexAttrib3fv(indx, values);
  }

  @Override
  public void glVertexAttrib4f(int indx, float x, float y, float z, float w) {
    gl2.glVertexAttrib4f(indx, x, y, z, w);
  }

  @Override
  public void glVertexAttrib4fv(int indx, float[] values, int offset) {
    gl2.glVertexAttrib4fv(indx, values, offset);
  }

  @Override
  public void glVertexAttrib4fv(int indx, FloatBuffer values) {
    gl2.glVertexAttrib4fv(indx, values);
  }

  @Override
  public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr) {
    gl2.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
  }

  @Override
  public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, int ptr) {
    gl2.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
  }

  @Override
  public void glViewport(int x, int y, int width, int height) {
    gl2.glViewport(x, y, width, height);
  }

  @Override
  public boolean hasGLSL() {
    return gl2.hasGLSL();
  }

  @Override
  public boolean isExtensionAvailable(String extension) {
    return gl2.isExtensionAvailable(extension);
  }

  @Override
  public boolean isFunctionAvailable(String function) {
    return gl2.isFunctionAvailable(function);
  }
}
