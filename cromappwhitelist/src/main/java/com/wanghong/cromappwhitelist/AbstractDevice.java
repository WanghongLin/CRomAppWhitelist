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
    private Intent extrasHolderIntent = new Intent();
    private Context context;
    private AppWhitelistConfig whitelistConfig;

    private void ensureWhitelistConfig() {
        if (whitelistConfig == null) {
            whitelistConfig = new AppWhitelistConfig(context);
        }
    }

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
        this.context = context;
        ensureWhitelistConfig();
        performStartActivity(context, componentForAutoStartSetting());
    }

    /**
     * Perform battery saving settings
     * <ul>
     *     <li>Meizu: Settings -> Battery -> More modes -> Standby management</li>
     *     <li>Oppo: Settings -> Battery -> Energy Saver</li>
     *     <li>Xiaomi: Settings -> Battery & performance -> Power -> App battery saver</li>
     *     <li>Huawei: Settings -> Protected apps</li>
     * </ul>
     * @param context the activity context
     */
    public void performBatterySaverSetting(Context context) {
        this.context = context;
        ensureWhitelistConfig();
        performStartActivity(context, componentForBatterySaverSetting());
    }

    /**
     * Performance memory acceleration settings, make your app won't kill when user click clean memory
     * @param context the activity context
     */
    public void performMemoryAccelerationSetting(Context context) {
        this.context = context;
        ensureWhitelistConfig();
        performStartActivity(context, componentForMemoryAccelerationSetting());
    }

    /**
     * Perform notification settings, make you app can show notification
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
        this.context = context;
        ensureWhitelistConfig();
        performStartActivity(context, componentForNotificationSetting());
    }

    private void performStartActivity(Context context, ComponentName component) {
        if (context != null && component != null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(component);
            intent.putExtras(getExtrasHolderIntent());
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
            } else {
                Log.e(TAG, "performStartActivity: could not resolve the component " + component.toShortString());
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

    public boolean hasAutoStartSetting(Context context) {
        this.context = context;
        ensureWhitelistConfig();
        return this.componentForAutoStartSetting() != null;
    }

    public boolean hasBatterySaverSetting(Context context) {
        this.context = context;
        ensureWhitelistConfig();
        return this.componentForBatterySaverSetting() != null;
    }

    public boolean hasMemoryAccelerationSetting(Context context) {
        this.context = context;
        ensureWhitelistConfig();
        return this.componentForMemoryAccelerationSetting() != null;
    }

    public boolean hasNotificationSetting(Context context) {
        this.context = context;
        ensureWhitelistConfig();
        return this.componentForNotificationSetting() != null;
    }

    /**
     * Provide component for auto start setting
     * @return a component or null if this feature is absent in the implemented device
     */
    protected ComponentName componentForAutoStartSetting() {
        return whitelistConfig.componentNameForAutoStart(context);
    }

    /**
     * Special case: it's called "Protected Apps" in Huawei device
     * @return a component to start activity
     */
    protected ComponentName componentForBatterySaverSetting() {
        return whitelistConfig.componentNameForBatterySaver(context);
    }

    /**
     * Provide component for memory acceleration setting
     * @return a component to start the settings or null if this feature is absent in the implemented device
     */
    protected ComponentName componentForMemoryAccelerationSetting() {
        return whitelistConfig.componentNameForMemAcc(context);
    }

    /**
     * Provide component for notification settings
     * @return a component to perform notification setting or null if such feature is not available
     */
    protected ComponentName componentForNotificationSetting() {
        return whitelistConfig.componentNameForNotification(context);
    }

    Intent getExtrasHolderIntent() {
        return extrasHolderIntent;
    }

    Context getContext() {
        return context;
    }
}
