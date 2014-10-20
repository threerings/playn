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

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import playn.core.Gamepad;
import playn.core.Gamepads;
import playn.core.Platform;

/**
 *
 * @author devnewton <devnewton@bci.im>
 */
class JavaGamepads implements Gamepads {

    private final List<Gamepad> gamepads = new ArrayList<Gamepad>();
    private final Platform platform;
    
    public JavaGamepads(Platform platform) {
        this.platform = platform;
    }

    public void init() {
        gamepads.clear();
        try {
            Controllers.create();
            for (int i = 0, n = Controllers.getControllerCount(); i < n; ++i) {
                gamepads.add(new JavaGamepad(Controllers.getController(i)));
            }
        } catch (LWJGLException e) {
            platform.log().error("Cannot init gamepads service", e);
        }

    }

    @Override
    public boolean hasGamepads() {
        return true;
    }

    @Override
    public List<Gamepad> plugged() {
        return gamepads;
    }

    public void update() {
        while(Controllers.next()) {
            if(Controllers.isEventAxis()) {
                int index = Controllers.getEventSource().getIndex();
                JavaGamepad gamepad = (JavaGamepad) plugged().get(index);
                gamepad.axisReady[Controllers.getEventControlIndex()] = true;
            }
        }
    }

}
