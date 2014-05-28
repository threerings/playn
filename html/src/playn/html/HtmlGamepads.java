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

import com.google.gwt.core.client.JsArray;
import java.util.ArrayList;
import java.util.List;
import playn.core.Gamepad;
import playn.core.Gamepads;

/**
 *
 * @author devnewton <devnewton@bci.im>
 */
public class HtmlGamepads implements Gamepads {

    private final List<Gamepad> gamepads = new ArrayList<Gamepad>();

    @Override
    public native boolean hasGamepads() /*-{
     return ('getGamepads' in navigator);
     }-*/;

    @Override
    public List<Gamepad> plugged() {
        if (hasGamepads()) {
            updateGamepads();
        }
        return gamepads;
    }

    private void updateGamepads() {
        JsArray<HtmlGamepadJavascriptObject> gamepadArray = nativePlugged();
        if (gamepadArray.length() != gamepads.size()) {
            gamepads.clear();
            for (int i = 0, n = gamepadArray.length(); i < n; ++i) {
                HtmlGamepadJavascriptObject js = gamepadArray.get(i);
                if(null != js) {
                    final HtmlGamepad gamepad = new HtmlGamepad(js);
                    gamepads.add(gamepad);
                }
            }
        }
    }

    private static native JsArray<HtmlGamepadJavascriptObject> nativePlugged() /*-{
     return navigator.getGamepads();
     }-*/;

}
