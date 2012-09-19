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

import cli.System.DateTime;
import cli.System.Drawing.RectangleF;
import cli.System.EventArgs;

import cli.OpenTK.FrameEventArgs;
import cli.OpenTK.Graphics.ES20.All;
import cli.OpenTK.Graphics.ES20.GL;
import cli.OpenTK.Graphics.ES20.FramebufferTarget;
import cli.OpenTK.Platform.iPhoneOS.iPhoneOSGameView;

import cli.MonoTouch.CoreAnimation.CAEAGLLayer;
import cli.MonoTouch.Foundation.ExportAttribute;
import cli.MonoTouch.Foundation.NSNotification;
import cli.MonoTouch.Foundation.NSNotificationCenter;
import cli.MonoTouch.Foundation.NSSet;
import cli.MonoTouch.OpenGLES.EAGLColorFormat;
import cli.MonoTouch.OpenGLES.EAGLRenderingAPI;
import cli.MonoTouch.UIKit.UIDevice;
import cli.MonoTouch.UIKit.UIEvent;

public class IOSGameView extends iPhoneOSGameView {

  private static final float MAX_DELTA = 100;

  private final IOSPlatform platform;
  private DateTime lastUpdate = DateTime.get_Now();
  private boolean paused;

  public IOSGameView(IOSPlatform platform, RectangleF bounds, float scale) {
    super(bounds);
    this.platform = platform;

    // TODO: I assume we want to manually manage loss of EGL context
    set_LayerRetainsBacking(false);
    set_ContentScaleFactor(scale);
    set_MultipleTouchEnabled(true);
    set_AutoResize(false);
    set_LayerColorFormat(EAGLColorFormat.RGBA8);
    // TODO: support OpenGL ES 1.1?
    set_ContextRenderingApi(EAGLRenderingAPI.wrap(EAGLRenderingAPI.OpenGLES2));

    NSNotificationCenter.get_DefaultCenter().AddObserver(
      UIDevice.get_OrientationDidChangeNotification(),
      new cli.System.Action$$00601_$$$_Lcli__MonoTouch__Foundation__NSNotification_$$$$_(new cli.System.Action$$00601_$$$_Lcli__MonoTouch__Foundation__NSNotification_$$$$_.Method() {
        @Override
        public void Invoke(NSNotification nf) {
          IOSGameView.this.platform.onOrientationChange(
            UIDevice.get_CurrentDevice().get_Orientation());
        }}));
  }

  // called when we're put into the background; normally we'll stop executing shortly after this,
  // but if the game requests additional time in the background, we need to stop rendering while
  // we're in the background
  void onPause() {
    paused = true;
  }

  void onResume() {
    paused = false;
  }

  @Override
  protected void ConfigureLayer (CAEAGLLayer eaglLayer) {
    eaglLayer.set_Opaque(true);
    super.ConfigureLayer(eaglLayer);
  }

  @Override
  protected void CreateFrameBuffer() {
    super.CreateFrameBuffer();
    // now that we're loaded, initialize the GL subsystem
    platform.viewDidInit(get_Framebuffer());
  }

  @Override
  protected void OnClosed(EventArgs e) {
    super.OnClosed(e);
  }

  @Override
  protected void OnDisposed(EventArgs e) {
    super.OnDisposed(e);
  }

  @Override
  protected void OnLoad(EventArgs e) {
    super.OnLoad(e);
    UIDevice.get_CurrentDevice().BeginGeneratingDeviceOrientationNotifications();

    // run a single frame so that we have something in our framebuffer when iOS stops displaying
    // our splash screen and starts displaying our app
    platform.update(0);
    GL.BindFramebuffer(FramebufferTarget.wrap(FramebufferTarget.Framebuffer), get_Framebuffer());
    MakeCurrent();
    platform.paint();
    SwapBuffers();
  }

  @Override
  protected void OnUnload(EventArgs e) {
    super.OnUnload(e);
    UIDevice.get_CurrentDevice().EndGeneratingDeviceOrientationNotifications();
  }

  @Override
  protected void OnRenderFrame(FrameEventArgs e) {
    super.OnRenderFrame(e);

    if (!paused) {
      MakeCurrent();
      platform.paint();
      SwapBuffers();
    }
  }

  @Override
  protected void OnResize(EventArgs e) {
    super.OnResize(e);
  }

  @Override
  protected void OnTitleChanged(EventArgs e) {
    super.OnTitleChanged(e);
  }

  @Override
  protected void OnUpdateFrame(FrameEventArgs e) {
    super.OnUpdateFrame(e);

    DateTime now = DateTime.get_Now();
    float delta = Math.min(MAX_DELTA, (now.get_Ticks() - lastUpdate.get_Ticks())/10000f);
    lastUpdate = now;
    platform.update(delta);
  }

  @Override
  public void TouchesBegan(NSSet touches, UIEvent event) {
    super.TouchesBegan(touches, event);
    platform.touch().onTouchesBegan(touches, event);
    platform.pointer().onTouchesBegan(touches, event);
  }

  @Override
  public void TouchesMoved(NSSet touches, UIEvent event) {
    super.TouchesMoved(touches, event);
    platform.touch().onTouchesMoved(touches, event);
    platform.pointer().onTouchesMoved(touches, event);
  }

  @Override
  public void TouchesEnded(NSSet touches, UIEvent event) {
    super.TouchesEnded(touches, event);
    platform.touch().onTouchesEnded(touches, event);
    platform.pointer().onTouchesEnded(touches, event);
  }

  @Override
  public void TouchesCancelled(NSSet touches, UIEvent event) {
    super.TouchesCancelled(touches, event);
    platform.touch().onTouchesCancelled(touches, event);
    platform.pointer().onTouchesCancelled(touches, event);
  }

  @ExportAttribute.Annotation("layerClass")
  static cli.MonoTouch.ObjCRuntime.Class LayerClass() {
    return iPhoneOSGameView.GetLayerClass();
  }
}
