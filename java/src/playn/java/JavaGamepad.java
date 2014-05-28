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

import org.lwjgl.input.Controller;
import playn.core.Gamepad;

/**
 *
 * @author devnewton <devnewton@bci.im>
 */
class JavaGamepad implements Gamepad {

    private final Controller controller;
    final boolean[] axisReady;
    
    JavaGamepad(Controller controller) {
        this.controller = controller;
        axisReady = new boolean[controller.getAxisCount()];
    }

    @Override
    public String name() {
        return controller.getName();
    }

    @Override
    public int buttonCount() {
        return controller.getButtonCount();
    }

    @Override
    public float buttonValue(int index) {
        return controller.isButtonPressed(index) ? 1.0f : 0.0f;
    }

    @Override
    public boolean isButtonInDeadZone(int index) {
        return buttonValue(index) < 0.1f;
    }

    @Override
    public int axisCount() {
        return controller.getAxisCount();
    }

    @Override
    public float axisValue(int index) {
        return axisReady[index] ? controller.getAxisValue(index) : 0f;
    }

    @Override
    public boolean isAxisInDeadZone(int index) {
        float deadZone = controller.getDeadZone(index);
        float value = axisValue(index);
        return value < deadZone && value > -deadZone;
    }

}
