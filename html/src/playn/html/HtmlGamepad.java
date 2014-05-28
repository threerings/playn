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

import playn.core.Gamepad;

/**
 *
 * @author devnewton <devnewton@bci.im>
 */
class HtmlGamepad implements Gamepad {

    private final HtmlGamepadJavascriptObject js;

    HtmlGamepad(HtmlGamepadJavascriptObject js) {
        this.js = js;
    }

    @Override
    public String name() {
        return js.name();
    }

    @Override
    public int buttonCount() {
        return js.buttonCount();
    }

    @Override
    public float buttonValue(int index) {
        return js.buttonValue(index);
    }

    @Override
    public boolean isButtonInDeadZone(int index) {
        return js.isButtonInDeadZone(index);
    }

    @Override
    public int axisCount() {
        return js.axisCount();
    }

    @Override
    public float axisValue(int index) {
        return js.axisValue(index);
    }

    @Override
    public boolean isAxisInDeadZone(int index) {
        return js.isAxisInDeadZone(index);
    }

}
