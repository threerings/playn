/**
 * Copyright 2010 The PlayN Authors
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
package playn.android;

import android.util.Log;

import playn.core.LogImpl;

class AndroidLog extends LogImpl {

  @Override
  protected void logImpl(Level level, String msg, Throwable e) {
    switch (level) {
    case DEBUG: Log.d("playn", msg, e); break;
    default:    Log.i("playn", msg, e); break;
    case  WARN: Log.w("playn", msg, e); break;
    case ERROR: Log.e("playn", msg, e); break;
    }
  }
}
