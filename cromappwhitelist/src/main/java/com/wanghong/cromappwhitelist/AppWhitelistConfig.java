/*
 * Copyright (C) 2020 mutter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wanghong.cromappwhitelist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppWhitelistConfig {

    private static final String TAG = "AppWhitelistConfig";
    public static final String JSON_KEY_AUTO_START = "auto_start";
    public static final String JSON_KEY_BATTERY_SAVER = "battery_saver";
    public static final String JSON_KEY_MEM_ACC = "mem_acc";
    public static final String JSON_KEY_NOTIFICATION = "notification";

    private static final String DEFAULT_CONFIG = "default_appwhitelist.json";
    private static final String USER_CONFIG = "appwhitelist.json";

    private Map<String, List<Component>> whiteListMap = new HashMap<>();

    AppWhitelistConfig(Context context) {
        if (context != null) {
            appendJsonConfigToMap(manufactureJsonConfigFromAsset(context, USER_CONFIG));
            appendJsonConfigToMap(manufactureJsonConfigFromAsset(context, DEFAULT_CONFIG));
        }
    }

    private void appendJsonConfigToMap(JSONObject jsonObject) {
        if (jsonObject == null) {
            Log.w(TAG, "appendJsonConfigToMap: no config provide for "
                    + Build.MANUFACTURER.toLowerCase());
            return;
        }

        for (String key : Arrays.asList(JSON_KEY_AUTO_START, JSON_KEY_BATTERY_SAVER,
                JSON_KEY_MEM_ACC, JSON_KEY_NOTIFICATION)) {
            if (whiteListMap.get(key) == null) {
                whiteListMap.put(key, new ArrayList<Component>());
            }

            List<Component> componentList = whiteListMap.get(key);
            if (componentList != null) {
                JSONArray array = jsonObject.optJSONArray(key);
                if (array != null) {
                    final int size = array.length();
                    for (int i = 0; i < size; i++) {
                        try {
                            final JSONObject component = array.getJSONObject(i);
                            if (component != null) {
                                componentList.add(
                                        new Component(component.optString("pkg"),
                                                component.optString("cls")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private JSONObject manufactureJsonConfigFromAsset(Context context, String assetName) {
        InputStream in = null;
        try {
            in = context.getAssets().open(assetName);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            CharBuffer charBuffer = CharBuffer.allocate(in.available());
            if (reader.read(charBuffer) > 0) {
                charBuffer.flip();
                reader.close();
                return new JSONObject(charBuffer.toString());
            } else {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private ComponentName resolvableComponentNameForKey(final String key, Context context) {
        final List<Component> componentList = whiteListMap.get(key);
        if (componentList != null && context != null) {
            final PackageManager pm = context.getPackageManager();
            for (Component component : componentList) {
                ComponentName cn = new ComponentName(component.getPkg(), component.getCls());
                final Intent intent = new Intent();
                intent.setComponent(cn);
                if (pm.resolveActivity(intent, 0) != null) {
                    return cn;
                }
            }
        }
        return null;
    }

    ComponentName componentNameForAutoStart(Context context) {
        return resolvableComponentNameForKey(JSON_KEY_AUTO_START, context);
    }

    ComponentName componentNameForBatterySaver(Context context) {
        return resolvableComponentNameForKey(JSON_KEY_BATTERY_SAVER, context);
    }

    ComponentName componentNameForMemAcc(Context context) {
        return resolvableComponentNameForKey(JSON_KEY_MEM_ACC, context);
    }

    ComponentName componentNameForNotification(Context context) {
        return resolvableComponentNameForKey(JSON_KEY_NOTIFICATION, context);
    }
}
