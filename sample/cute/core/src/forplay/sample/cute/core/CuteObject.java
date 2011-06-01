/**
 * Copyright 2010 The ForPlay Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package forplay.sample.cute.core;

import forplay.core.Image;
import forplay.sample.cute.core.CuteWorld.Stack;

public class CuteObject {

  public Image img;
  public double oldx, oldy, oldz;
  public double x, y, z;
  public double vx, vy, vz;
  public double ax, ay, az;
  public double r;

  Stack stack;
  int lastUpdated;
  boolean resting;

  public CuteObject(Image img) {
    this.img = img;
  }

  public boolean isResting() {
    return resting;
  }

  public void setAcceleration(double ax, double ay, double az) {
    this.ax = ax;
    this.ay = ay;
    this.az = az;
  }

  public void setPos(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void setVelocity(double vx, double vy, double vz) {
    this.vx = vx;
    this.vy = vy;
    this.vz = vz;
  }

  public void saveOldPos() {
    this.oldx = x;
    this.oldy = y;
    this.oldz = z;
  }

  public double x(double alpha) {
    return x * alpha + oldx * (1.0f - alpha);
  }

  public double y(double alpha) {
    return y * alpha + oldy * (1.0f - alpha);
  }

  public double z(double alpha) {
    return z * alpha + oldz * (1.0f - alpha);
  }
}
