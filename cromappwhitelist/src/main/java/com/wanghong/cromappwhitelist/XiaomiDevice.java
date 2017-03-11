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

/**
 * Created by wanghong on 3/2/17.
 */

public class XiaomiDevice extends AbstractDevice {

    @Override
    protected ComponentName componentForAutoStartSetting() {
        return new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
    }

    @Override
    protected ComponentName componentForBatterySaverSetting() {
        return new ComponentName("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity");
    }

    @Override
    protected ComponentName componentForMemoryAccelerationSetting() {
        return null;
    }

    @Override
    protected ComponentName componentForNotificationSetting() {
        return null;
    }
}
