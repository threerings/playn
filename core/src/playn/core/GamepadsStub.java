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

package playn.core;

import java.util.Collections;
import java.util.List;

/**
 * A NOOP gamepads service for use on platforms that don't support gamepad
 * interaction.
 *
 * @author devnewton <devnewton@bci.im>
 */
public class GamepadsStub implements Gamepads {

    @Override
    public boolean hasGamepads() {
        return false;
    }

    @Override
    public List<Gamepad> plugged() {
        return Collections.emptyList();
    }

}
