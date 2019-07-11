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

public class HuaweiDevice extends AbstractDevice {

    @Override
    protected ComponentName componentForAutoStartSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity");
        }
        return new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
    }

    @Override
    protected ComponentName componentForBatterySaverSetting() {
        return new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
    }

    @Override
    protected ComponentName componentForMemoryAccelerationSetting() {
        return null;
    }

    @Override
    protected ComponentName componentForNotificationSetting() {
        return new ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity");
    }
}
