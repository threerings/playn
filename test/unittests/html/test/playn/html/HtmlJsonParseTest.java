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
package playn.html;

import playn.core.Json;
import playn.shared.json.JsonImplParseTest;

import com.google.gwt.junit.client.GWTTestCase;

public class HtmlJsonParseTest extends GWTTestCase {
  private class BaseArrayTest extends JsonImplParseTest {
    @Override
    protected Json json() {
      return new HtmlJson();
    }
  }
  
  public void testParseArray() {
    new BaseArrayTest().testParseArray();
  }

  public void testParseObject() {
    new BaseArrayTest().testParseObject();
  }

  @Override
  public String getModuleName() {
    return "playn.html.HtmlTests";
  }
}
