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

import java.util.ArrayList;

import playn.core.Storage;
import android.app.Activity;
import android.content.SharedPreferences;

public class AndroidStorage implements Storage {
  private static final String PREFS_NAME = "playn";
  private final Activity activity;
  private SharedPreferences settings;

  public AndroidStorage(Activity activity) {
    this.activity = activity;
  }

  @Override
  public void setItem(String key, String data) throws RuntimeException {
    getSettings().edit().putString(key, data).commit();
  }

  @Override
  public void removeItem(String key) {
    getSettings().edit().remove(key).commit();
  }

  @Override
  public String getItem(String key) {
    return getSettings().getString(key, null);
  }

  @Override
  public Iterable<String> keys() {
    return new ArrayList<String>(getSettings().getAll().keySet());
  }

  @Override
  public boolean isPersisted() {
    return true;
  }

  private SharedPreferences getSettings() {
    if (settings == null) {
      settings = activity.getSharedPreferences(PREFS_NAME, 0);
    }
    return settings;
  }
}
