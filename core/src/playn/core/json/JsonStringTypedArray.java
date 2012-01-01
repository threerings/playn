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
package playn.core.json;

import java.util.ArrayList;
import java.util.Collection;

import playn.core.Json;

class JsonStringTypedArray extends ArrayList<String> implements Json.TypedArray<String> {
  private static final long serialVersionUID = 1L;

  public JsonStringTypedArray(Collection<String> contents) {
    super(contents);
  }
  
  @Override
  public int length() {
    return size();
  }

  @Override
  public String get(int index, String dflt) {
    String s = get(index);
    if (s == null)
      return dflt;
    
    return s;
  }
}
