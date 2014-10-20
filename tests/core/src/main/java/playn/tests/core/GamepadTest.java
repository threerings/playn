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

package playn.tests.core;

import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Gamepad;
import playn.core.ImageLayer;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.gamepads;

/**
 *
 * @author devnewton <devnewton@bci.im>
 */
class GamepadTest extends Test {

    private ImageLayer movable;

    @Override
    public String getName() {
        return "Gamepad";
    }

    @Override
    public String getDescription() {
        return "Tests support for gamepads";
    }

    @Override
    public void init() {
        CanvasImage image = graphics().createImage(32, 32);
        image.canvas().setFillColor(Color.rgb(100, 100, 100));
        image.canvas().fillRect(0, 0, image.width(), image.height());
        movable = graphics().createImageLayer(image);
        movable.setTranslation(graphics().width() / 2.0f, graphics().height() / 2.0f);
        graphics().rootLayer().add(movable);
    }

    @Override
    public void update(int delta) {
        if (gamepads().hasGamepads()) {
            for (Gamepad gamepad : gamepads().plugged()) {
                if (null != gamepad) {
                    if (gamepad.axisCount() > 0) {
                        if (!gamepad.isAxisInDeadZone(0)) {
                            float value = gamepad.axisValue(0);
                            movable.transform().translateX(value);
                        }
                    }
                    if (gamepad.axisCount() > 1) {
                        if (!gamepad.isAxisInDeadZone(1)) {
                            movable.transform().translateY(gamepad.axisValue(1));
                        }
                    }
                    if (gamepad.buttonCount() > 0) {
                        if (!gamepad.isButtonInDeadZone(0)) {
                            movable.setTint(Color.rgb(255, 0, 0));
                        } else {
                            movable.setTint(Color.rgb(255, 255, 255));
                        }
                    }
                }
            }
        }
    }
}
