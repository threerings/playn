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
package playn.showcase.core.peas.entities;

import playn.showcase.core.peas.PeaWorld;

public class Cloud3 extends Cloud1 {
  @SuppressWarnings("hiding")
  public static String TYPE = "Cloud3";

  public Cloud3(PeaWorld peaWorld) {
    super(peaWorld);
  }

  @Override
  float getVelocity() {
    return 0.002f;
  }

  @Override
  String getImageName() {
    return "Cloud3.png";
  }
}
