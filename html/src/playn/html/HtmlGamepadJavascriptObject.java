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

package playn.html;

import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 * @author devnewton <devnewton@bci.im>
 */
public final class HtmlGamepadJavascriptObject extends JavaScriptObject {

    protected HtmlGamepadJavascriptObject() {

    }

    public native String name() /*-{
     return this.id;
     }-*/;

    public native int buttonCount() /*-{
     return this.buttons.length;
     }-*/;

    public native float buttonValue(int index) /*-{
     return this.buttons[index].value;
     }-*/;

    public native boolean isButtonInDeadZone(int index) /*-{
     return !this.buttons[index].pressed;
     }-*/;

    public native int axisCount() /*-{
     return this.axes.length;
     }-*/;

    public native float axisValue(int index) /*-{
     return this.axes[index];
     }-*/;

    public boolean isAxisInDeadZone(int index) {
        float value = axisValue(index);
        return value < 0.1f && value > -0.1f;
    }

}
