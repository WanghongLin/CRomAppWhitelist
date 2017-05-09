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

import android.content.ComponentName;
import android.os.Build;

/**
 * Created by wanghong on 3/2/17.
 */

public class MeizuDevice extends AbstractDevice {
    @Override
    protected ComponentName componentForAutoStartSetting() {
        return new ComponentName("com.meizu.safe", "com.meizu.safe.permission.SmartBGActivity");
    }

    @Override
    protected ComponentName componentForBatterySaverSetting() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && Build.MODEL.equals("MX4")) {
            return new ComponentName("com.meizu.safe", "com.meizu.safe.powerui.PowerAppPermissionActivity");
        }
        getExtrasHolderIntent().putExtra("packageName", getContext().getPackageName());
        return new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
    }

    @Override
    protected ComponentName componentForMemoryAccelerationSetting() {
        return new ComponentName("com.meizu.safe", "com.meizu.safe.ramcleaner.RAMCleanerWhiteList");
    }

    @Override
    protected ComponentName componentForNotificationSetting() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && Build.MODEL.equals("MX4")) {
            return new ComponentName("com.meizu.safe", "com.meizu.safe.permission.NotificationActivity");
        }
        getExtrasHolderIntent().putExtra("packageName", getContext().getPackageName());
        return new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
    }
}
