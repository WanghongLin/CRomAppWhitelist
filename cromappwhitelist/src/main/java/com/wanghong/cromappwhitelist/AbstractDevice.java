/*
 * Copyright (C) 2017 wanghong
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

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wanghong on 3/2/17.
 */

public abstract class AbstractDevice {

    private static final String TAG = "AbstractDevice";

    /**
     * Perform auto start settings<br/>
     * <br/>
     * Setup from menu manually
     * <br/>
     * <ul>
     *     <li>Samsung: Settings -> Security -> Application permissions -> click app's icon</li>
     *     <li>Meizu: Settings -> Apps -> click app's icon -> Permissions</li>
     *     <li>Huawei: Settings -> Startup manager</li>
     *     <li>Xiaomi: Settings -> Permissions -> Autostart</li>
     * </ul>
     * <br/>
     * Special case:
     * <ul>
     *     <li>in Huawei device, will show "Startup manager", and linked with {@link Intent#ACTION_BOOT_COMPLETED}</li>
     *     <li>in Meizu device, also linked with {@link Intent#ACTION_BOOT_COMPLETED}, but not implemented yet.</li>
     *     <li>in Samsung device, will show "Application permissions", it's managed under <b>com.android.settings/.SubSettings</b>, not implemented yet.</li>
     * </ul>
     * @param context the activity context
     */
    public void performAutoStartSetting(Context context) {
        performStartActivity(context, componentForAutoStartSetting());
    }

    /**
     * <ul>
     *     <li>Meizu: Settings -> Battery -> More modes -> Standby management</li>
     *     <li>Oppo: Settings -> Battery -> Energy Saver</li>
     *     <li>Xiaomi: Settings -> Battery & performance -> Power -> App battery saver</li>
     *     <li>Huawei: Settings -> Protected apps</li>
     * </ul>
     * @param context the activity context
     */
    public void performBatterySaverSetting(Context context) {
        performStartActivity(context, componentForBatterySaverSetting());
    }

    public void performMemoryAccelerationSetting(Context context) {
        performStartActivity(context, componentForMemoryAccelerationSetting());
    }

    /**
     * <ul>
     *     <li>Xiaomi: no way to set, it's managed under <b>com.android.settings/.SubSettings</b></li>
     *     <li>Oppo: Settings -> Notification and status bar -> Notification manager</li>
     *     <li>Meizu: no way to set, it's managed under <b>com.android.settings/.SubSettings</b>,
     *     <br/>Settings -> Notification and status bar -> Manage app notifications</li>
     *     <li>Samsung: not implemented yet, it's managed under<b>com.android.settings/.SubSettings</b>
     *     <br/>Settings -> Sounds and notifications -> Application notifications</li>
     * </ul>
     * @param context the activity context
     */
    public void performNotificationSetting(Context context) {
        performStartActivity(context, componentForNotificationSetting());
    }

    private void performStartActivity(Context context, ComponentName component) {
        if (context != null && component != null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(component);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "performStartActivity: no such activity");
                    e.printStackTrace();
                } catch (SecurityException e) {
                    Log.e(TAG, "performStartActivity: not exported");
                    e.printStackTrace();
                }
            }
        } else {
            if (component == null) {
                Log.w(TAG, "performStartActivity: this feature not implemented in this device");
                if (context != null && BuildConfig.DEBUG) {
                    Toast.makeText(context, R.string.not_implemented, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    abstract protected ComponentName componentForAutoStartSetting();

    /**
     * Special case: it's called "Protected Apps" in Huawei device
     * @return a component to start activity
     */
    abstract protected ComponentName componentForBatterySaverSetting();

    abstract protected ComponentName componentForMemoryAccelerationSetting();

    abstract protected ComponentName componentForNotificationSetting();
}
