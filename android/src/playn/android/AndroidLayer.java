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

import playn.core.Canvas;
import playn.core.Transform;
import playn.core.gl.LayerGL;

public abstract class AndroidLayer extends LayerGL {

  protected final AndroidGL20 gfx;
  
  protected AndroidLayer(AndroidGL20 gfx) {
    super();
    this.gfx = gfx;
  }

  void transform(Canvas canvas) {
    canvas.translate(originX, originY);
    canvas.transform(transform.m00(), transform.m01(), transform.m10(),
        transform.m11(), transform.tx() - originX, transform.ty() - originY);
    canvas.translate(-originX, -originY);
  }
  
  @Override
  //TODO (jonagill): Actually be able to paint SurfaceLayers
  public void paint(Transform parentTransform, float parentAlpha) { }
  
//  public abstract void paint(Transform parentTransform, float parentAlpha);
//abstract void paint(AndroidCanvas canvas);
}
