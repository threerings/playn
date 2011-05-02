/**
 * Copyright 2011 The ForPlay Authors
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
package forplay.sample.noise.html;

import forplay.core.ForPlay;
import forplay.html.HtmlAssetManager;
import forplay.html.HtmlGame;
import forplay.html.HtmlPlatform;
import forplay.sample.noise.core.NoiseGame;
import forplay.sample.noise.resources.NoiseAutoBundle;

public class NoiseGameHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    
    HtmlAssetManager assets = platform.assetManager();
    assets.addClientBundle("^", NoiseAutoBundle.INSTANCE);

    ForPlay.run(new NoiseGame());
  }
}
